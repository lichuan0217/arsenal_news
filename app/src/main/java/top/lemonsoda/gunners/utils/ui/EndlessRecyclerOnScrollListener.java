package top.lemonsoda.gunners.utils.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import javax.inject.Inject;

/**
 * Created by chuanl on 2/21/17.
 */

public class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;

    LinearLayoutManager linearLayoutManager;
    OnLoadMoreListener onLoadMoreListener;

    @Inject
    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        totalItemCount = linearLayoutManager.getItemCount();
        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

        if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            if (onLoadMoreListener != null) {
                onLoadMoreListener.onLoadMore();
            }
            isLoading = true;
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }

    public void setLoaded() {
        isLoading = false;
    }
}
