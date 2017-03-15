package top.lemonsoda.gunners.news;

import android.content.Context;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import top.lemonsoda.gunners.R;
import top.lemonsoda.gunners.data.NewsRepository;
import top.lemonsoda.gunners.data.module.News;
import top.lemonsoda.gunners.utils.NetworkUtils;
import top.lemonsoda.gunners.utils.RxUtils;

/**
 * Created by chuanl on 2/24/17.
 */

public class NewsIndexPresenter implements NewsIndexContract.Presenter {

    private static final String TAG = NewsIndexPresenter.class.getCanonicalName();

    // View
    private final NewsIndexContract.View newsIndexView;
    // Model
    private final NewsRepository newsRepository;

    private final Context context;
    private boolean firstLoad = true;

    private Subscription loadSubscription;

    @Inject
    public NewsIndexPresenter(Context context, NewsRepository newsRepository,
                              NewsIndexContract.View newsIndexView) {
        this.context = context;
        this.newsRepository = newsRepository;
        this.newsIndexView = newsIndexView;
        this.newsIndexView.setPresenter(this);
    }

    @Override
    public void start() {
        // When first time, force to load data from network
        loadNews(firstLoad);
        firstLoad = false;
    }

    @Override
    public void destroy() {
        RxUtils.unsubscribe(loadSubscription);
    }


    @Override
    public void loadNews(boolean forceUpdate) {

        if (!forceUpdate) {
            return;
        }

        newsIndexView.showLoading();

        if (NetworkUtils.isNetworkConnected(context)) {
            // Load data from network
            loadSubscription = newsRepository.getNewsFromNetwork(0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<News>>() {
                        @Override
                        public void call(List<News> newsList) {
                            Log.d(TAG, "Get News From Network Done");
                            newsIndexView.showNewsIndex(newsList);
                            newsIndexView.stopLoading();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            newsIndexView.stopLoading();
                        }
                    });
        } else {
            // Load data from local database
            loadSubscription = newsRepository.getNewsFromLocal()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<News>>() {
                        @Override
                        public void call(List<News> newsList) {
                            Log.d(TAG, "Get News From Local Done");
                            newsIndexView.showNewsIndex(newsList);
                            newsIndexView.showError(context.getString(R.string.info_no_network));
                            newsIndexView.stopLoading();
                        }
                    });
        }
    }

    @Override
    public void loadMoreNews(int page) {
        if (NetworkUtils.isNetworkConnected(context)) {
            // Load more data from network
            loadSubscription = newsRepository.getNewsFromNetwork(page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<News>>() {
                        @Override
                        public void call(List<News> newsList) {
                            Log.d(TAG, "Get More News From Network Done");
                            newsIndexView.showMoreNewsIndex(newsList);
                            newsIndexView.stopLoading();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            newsIndexView.showLoadMoreError(context.getString(R.string.info_load_error));
                            newsIndexView.stopLoading();
                        }
                    });
        } else {
            newsIndexView.showLoadMoreError(context.getString(R.string.info_no_network));
        }

    }
}
