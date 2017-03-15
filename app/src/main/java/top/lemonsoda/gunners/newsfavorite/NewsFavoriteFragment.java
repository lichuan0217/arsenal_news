package top.lemonsoda.gunners.newsfavorite;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.lemonsoda.gunners.R;
import top.lemonsoda.gunners.data.module.News;
import top.lemonsoda.gunners.utils.ui.EmptyRecyclerView;
import top.lemonsoda.gunners.utils.ui.OnNewsIndexItemClickListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsFavoriteFragment extends Fragment implements NewsFavoriteContract.View {
    private static final String TAG = NewsFavoriteFragment.class.getCanonicalName();

    @BindView(R.id.fl_news_favorite_container)
    FrameLayout container;

    @BindView(R.id.rv_news_favorite_list)
    EmptyRecyclerView rvNewsFavoriteList;

    @BindView(R.id.favorites_empty_view)
    View emptyView;

    @Inject
    NewsFavoriteAdapter newsFavoriteAdapter;

    @Inject
    LinearLayoutManager linearLayoutManager;

    private NewsFavoriteContract.Presenter presenter;
    private OnNewsIndexItemClickListener newsIndexItemClickListener;

    public static NewsFavoriteFragment getInstance() {
        return new NewsFavoriteFragment();
    }

    public NewsFavoriteFragment() {
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

        DaggerNewsFavoriteFragmentComponent.builder()
                .newsFavoriteFragmentModule(new NewsFavoriteFragmentModule(getActivity()))
                .build().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, view);

        rvNewsFavoriteList.setEmptyView(emptyView);
        rvNewsFavoriteList.setLayoutManager(linearLayoutManager);
        newsFavoriteAdapter.setOnNewsItemClickListener(newsIndexItemClickListener);
        rvNewsFavoriteList.setAdapter(newsFavoriteAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.start();
        }
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
        presenter.destroy();
    }

    @Override
    public void setPresenter(NewsFavoriteContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading() {
        Log.d(TAG, "loading...");
    }

    @Override
    public void stopLoading() {
        Log.d(TAG, "loading finish...");
    }

    @Override
    public void showFavoriteNews(List<News> newsList) {
        Log.d(TAG, "Favorites: " + newsList);
        newsFavoriteAdapter.swapData(newsList);
    }

    @Override
    public void showError(String error) {
        Snackbar.make(container, error, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
