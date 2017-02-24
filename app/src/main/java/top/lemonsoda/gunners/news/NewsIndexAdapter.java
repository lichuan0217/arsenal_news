package top.lemonsoda.gunners.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.lemonsoda.gunners.R;
import top.lemonsoda.gunners.data.module.News;
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

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    @Inject
    public NewsIndexAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = layoutInflater.inflate(R.layout.item_news_list, parent, false);
            return new NewsItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = layoutInflater.inflate(R.layout.loading_item_news_list, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsItemViewHolder) {
            NewsItemViewHolder viewHolder = (NewsItemViewHolder) holder;
            viewHolder.title.setText(newsList.get(position).getHeader());
            viewHolder.src.setText(newsList.get(position).getSource());
            Glide.with(context)
                    .load(newsList.get(position).getThumbnail())
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
        return newsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return newsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
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
        notifyItemInserted(newsList.size() - 1);
    }

    public void removeNullData() {
        newsList.remove(newsList.size() - 1);
        notifyItemRemoved(newsList.size());
    }

    public void setNewsIndexItemClickListener(OnNewsIndexItemClickListener listener) {
        newsIndexItemClickListener = listener;
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
                    newsList.get(pos).getArticleId(),
                    newsList.get(pos).getHeader());
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
