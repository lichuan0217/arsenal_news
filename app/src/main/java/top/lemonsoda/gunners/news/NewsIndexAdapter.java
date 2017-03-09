package top.lemonsoda.gunners.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.lemonsoda.gunners.R;
import top.lemonsoda.gunners.data.module.News;
import top.lemonsoda.gunners.utils.ui.NewsPagerView;
import top.lemonsoda.gunners.utils.ui.OnNewsIndexItemClickListener;

/**
 * Created by chuanl on 2/24/17.
 */

public class NewsIndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = NewsIndexAdapter.class.getCanonicalName();

    private Context context;
    private List<News> newsList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private OnNewsIndexItemClickListener newsIndexItemClickListener;

    private final int VIEW_TYPE_HEADER = 0;
    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_LOADING = 2;

    private final int HEADER_NEWS_LIST_SIZE = 5;

    @Inject
    public NewsIndexAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = layoutInflater.inflate(R.layout.layout_header_item_news_list, parent, false);
            return new NewsHeaderViewHolder(view);
        } else if (viewType == VIEW_TYPE_ITEM) {
            View view = layoutInflater.inflate(R.layout.layout_item_news_list, parent, false);
            return new NewsItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = layoutInflater.inflate(R.layout.layout_loading_item_news_list, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsHeaderViewHolder) {
            NewsHeaderViewHolder viewHolder = (NewsHeaderViewHolder) holder;
            if (newsList.size() > 5) {
                viewHolder.newsPagerView.setNewsList(getHeaderSubList());
                viewHolder.newsPagerView.setOnNewsIndexItemClickListener(newsIndexItemClickListener);
            }
        } else if (holder instanceof NewsItemViewHolder) {
            NewsItemViewHolder viewHolder = (NewsItemViewHolder) holder;
            viewHolder.title.setText(newsList.get(position - 1).getHeader());
            viewHolder.src.setText(newsList.get(position - 1).getSource());
            Glide.with(context)
                    .load(newsList.get(position - 1).getThumbnail())
                    .centerCrop()
                    .placeholder(R.mipmap.placeholder)
                    .thumbnail(0.5f)
                    .into(viewHolder.photo);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
            viewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        }
        return newsList.get(position - 1) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void swapData(Collection<News> newsCollection) {
        newsList.clear();
        addData(newsCollection);
    }

    public void addData(Collection<News> newsCollection) {
        if (newsCollection != null) {
            newsList.addAll(newsCollection);
        }
        notifyDataSetChanged();
    }

    public void addNullData() {
        newsList.add(null);
        notifyItemInserted(newsList.size());
    }

    public void removeNullData() {
        newsList.remove(newsList.size() - 1);
        notifyItemRemoved(newsList.size() + 1);
    }

    public void setNewsIndexItemClickListener(OnNewsIndexItemClickListener listener) {
        newsIndexItemClickListener = listener;
    }

    private List<News> getHeaderSubList() {
        ArrayList<News> headList = new ArrayList<>();
        boolean[] flags = new boolean[HEADER_NEWS_LIST_SIZE];
        int[] tmp = new int[HEADER_NEWS_LIST_SIZE];
        Random random = new Random();
        int num;
        for (int i = 0; i < HEADER_NEWS_LIST_SIZE; i++) {
            do {
                num = random.nextInt(newsList.size());
            } while (flags[i]);
            flags[i] = true;
            tmp[i] = num;
            headList.add(newsList.get(num));
        }

        Log.d(TAG, "HeadList: " + Arrays.toString(tmp));
        return headList;
    }


    public class NewsHeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.new_pager_view)
        NewsPagerView newsPagerView;

        public NewsHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class NewsItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_news_list_title)
        public TextView title;

        @BindView(R.id.tv_news_list_src)
        public TextView src;

        @BindView(R.id.iv_news_list_photo)
        public ImageView photo;

        @OnClick(R.id.cv_news_list_container)
        void onItemClick() {
            int pos = getLayoutPosition();
            newsIndexItemClickListener.onItemClick(
                    newsList.get(pos - 1).getArticleId(),
                    newsList.get(pos - 1).getHeader());
        }

        public NewsItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.progressBar1)
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
