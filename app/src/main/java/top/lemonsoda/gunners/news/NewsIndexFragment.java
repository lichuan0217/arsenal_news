package top.lemonsoda.gunners.news;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.lemonsoda.gunners.R;
import top.lemonsoda.gunners.data.module.News;
import top.lemonsoda.gunners.utils.ui.EmptyRecyclerView;
import top.lemonsoda.gunners.utils.ui.EndlessRecyclerOnScrollListener;
import top.lemonsoda.gunners.utils.ui.NewsPagerView;
import top.lemonsoda.gunners.utils.ui.OnLoadMoreListener;
import top.lemonsoda.gunners.utils.ui.OnNewsIndexItemClickListener;

/**
 * Created by chuanl on 2/24/17.
 */

public class NewsIndexFragment extends Fragment implements NewsIndexContract.View,
        SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    private static final String TAG = NewsIndexFragment.class.getCanonicalName();

    @BindView(R.id.rv_news_list)
    EmptyRecyclerView newsListRecyclerView;

    @BindView(R.id.empty_view)
    View llEmptyView;

    @BindView(R.id.srl_news_item)
    SwipeRefreshLayout refreshLayout;

    @Inject
    NewsIndexAdapter newsIndexAdapter;

    @Inject
    LinearLayoutManager linearLayoutManager;

    @Inject
    EndlessRecyclerOnScrollListener scrollListener;

    private int currentPage = 0;
    private boolean noMoreData = false;
    private OnNewsIndexItemClickListener newsIndexItemClickListener;

    private NewsIndexContract.Presenter presenter;

    public static NewsIndexFragment newInstance() {
        return new NewsIndexFragment();
    }

    public NewsIndexFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewsIndexItemClickListener) {
            newsIndexItemClickListener = (OnNewsIndexItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNewsIndexItemClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject newsIndexAdapter and linearLayoutManager
        DaggerNewsIndexFragmentComponent.builder()
                .newsIndexFragmentModule(new NewsIndexFragmentModule(getActivity()))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_index, container, false);
        ButterKnife.bind(this, view);

        // Initialize SwipeRefreshLayout
        refreshLayout.setOnRefreshListener(this);

        // Initialize newsListRecyclerView
        newsListRecyclerView.setEmptyView(llEmptyView);
        newsListRecyclerView.setLayoutManager(linearLayoutManager);
        newsIndexAdapter.setNewsIndexItemClickListener(newsIndexItemClickListener);
        newsListRecyclerView.setAdapter(newsIndexAdapter);
        scrollListener.setOnLoadMoreListener(this);
        newsListRecyclerView.addOnScrollListener(scrollListener);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.presenter != null) {
            presenter.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
        presenter.destroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        // Stop the AutoScrollTask in NewsPagerView when destroy the fragment
        RecyclerView.ViewHolder viewHolder =
                newsListRecyclerView.getChildViewHolder(newsListRecyclerView.getChildAt(0));
        NewsIndexAdapter.NewsHeaderViewHolder holder =
                (NewsIndexAdapter.NewsHeaderViewHolder) viewHolder;
        holder.newsPagerView.stopAutoPlay();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }

    @Override
    public void setPresenter(NewsIndexContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading() {
        Log.d(TAG, "loading...");
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void stopLoading() {
        Log.d(TAG, "stop loading...");
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showNewsIndex(List<News> newsList) {
        currentPage++;
        newsIndexAdapter.swapData(newsList);
    }

    @Override
    public void showMoreNewsIndex(List<News> newsList) {
        // If newsList's size is 0, this means no more data
        if (newsList.size() == 0) {
            Toast.makeText(getActivity(),
                    getString(R.string.info_news_list_load_no_more), Toast.LENGTH_SHORT).show();
            noMoreData = true;
            // Remove load-more-progress-bar
            newsIndexAdapter.removeNullData();
            return;
        }

        // Add currentPage
        currentPage++;
        // Remove load-more-progress-bar
        newsIndexAdapter.removeNullData();
        // Add new data to view
        newsIndexAdapter.addData(newsList);
        // Set scrollListener's status to "loaded"
        scrollListener.setLoaded();
    }

    @Override
    public void onRefresh() {
        currentPage = 0;
        noMoreData = false;
        presenter.loadNews(true);
    }

    @Override
    public void onLoadMore() {
        // If no more data, just return without request the internet
        if (noMoreData) {
            return;
        }

        // To show load-more-progress-bar, we need to add a NULL item.
        newsListRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                newsIndexAdapter.addNullData();
            }
        });

        // Load more data
        presenter.loadMoreNews(currentPage);
    }
}
