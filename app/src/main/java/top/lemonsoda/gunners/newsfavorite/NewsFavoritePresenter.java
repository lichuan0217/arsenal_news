package top.lemonsoda.gunners.newsfavorite;

import android.content.Context;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import top.lemonsoda.gunners.R;
import top.lemonsoda.gunners.data.NewsRepository;
import top.lemonsoda.gunners.data.module.News;
import top.lemonsoda.gunners.data.user.UserManager;
import top.lemonsoda.gunners.utils.NetworkUtils;
import top.lemonsoda.gunners.utils.RxUtils;

/**
 * Created by Chuan on 09/03/2017.
 */

public class NewsFavoritePresenter implements NewsFavoriteContract.Presenter {
    private static final String TAG = NewsFavoritePresenter.class.getCanonicalName();

    private NewsFavoriteContract.View view;
    private NewsRepository newsRepository;
    private Context context;
    private boolean firstLoad = true;
    private Subscription loadFavoritesSubscription;

    @Inject
    UserManager userManager;

    @Inject
    public NewsFavoritePresenter(Context context,
                                 NewsFavoriteContract.View view, NewsRepository repository) {
        this.context = context;
        this.view = view;
        this.newsRepository = repository;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        Log.d(TAG, "Presenter starts work ...");
        loadFavoriteNews(firstLoad);
        firstLoad = false;
    }

    @Override
    public void destroy() {
        RxUtils.unsubscribe(loadFavoritesSubscription);
    }

    @Override
    public void loadFavoriteNews(boolean forceUpdate) {
        if (!forceUpdate) {
            return;
        }
        view.showLoading();
        if (NetworkUtils.isNetworkConnected(context)) {
            loadFavoritesSubscription =
                    newsRepository.getFavorites(userManager.getCurrentUser().getId() + "")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<List<News>>() {
                                @Override
                                public void call(List<News> newses) {
                                    view.showFavoriteNews(newses);
                                    view.stopLoading();
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    view.stopLoading();
                                    view.showError(context.getString(R.string.info_load_error));
                                }
                            });
        } else {
            view.showError(context.getString(R.string.info_no_network));
        }
    }
}
