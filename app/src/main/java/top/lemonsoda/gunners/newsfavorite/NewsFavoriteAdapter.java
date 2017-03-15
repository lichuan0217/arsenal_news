package top.lemonsoda.gunners.newsfavorite;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
 * Created by Chuan on 15/03/2017.
 */

public class NewsFavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<News> newsList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private OnNewsIndexItemClickListener onNewsIndexItemClickListener;

    @Inject
    public NewsFavoriteAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_item_favorites, parent, false);
        return new FavoriteNewsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FavoriteNewsItemViewHolder viewHolder = (FavoriteNewsItemViewHolder) holder;
        viewHolder.tvTitle.setText(newsList.get(position).getHeader());
        viewHolder.tvSubTitle.setText(newsList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
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

    public void setOnNewsItemClickListener(OnNewsIndexItemClickListener listener) {
        this.onNewsIndexItemClickListener = listener;
    }

    public class FavoriteNewsItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_subtitle)
        TextView tvSubTitle;

        @OnClick(R.id.cv_favorites_container)
        void onItemClick() {
            int pos = getLayoutPosition();
            onNewsIndexItemClickListener.onItemClick(
                    newsList.get(pos).getArticleId(),
                    newsList.get(pos).getHeader()
            );
        }

        public FavoriteNewsItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
