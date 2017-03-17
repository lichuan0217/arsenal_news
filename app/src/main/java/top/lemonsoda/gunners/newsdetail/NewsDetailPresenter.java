package top.lemonsoda.gunners.newsdetail;

import android.content.Context;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import top.lemonsoda.gunners.R;
import top.lemonsoda.gunners.data.NewsRepository;
import top.lemonsoda.gunners.data.module.NewsDetail;
import top.lemonsoda.gunners.data.module.RequestFavorite;
import top.lemonsoda.gunners.data.module.RequestUser;
import top.lemonsoda.gunners.data.module.ResponseFavorite;
import top.lemonsoda.gunners.data.module.User;
import top.lemonsoda.gunners.data.user.UserManager;
import top.lemonsoda.gunners.utils.RxUtils;

/**
 * Created by chuanl on 2/24/17.
 */

public class NewsDetailPresenter implements NewsDetailContract.Presenter {

    // View
    private NewsDetailContract.View newsDetailView;

    // Model
    private NewsRepository newsRepository;

    private String articleId;
    private Subscription loadSubscription;
    private Subscription postFavoriteSubscription;
    private Subscription deleteFavoriteSubscription;

    @Inject
    Context context;

    @Inject
    UserManager userManager;

    @Inject
    public NewsDetailPresenter(NewsDetailContract.View view, NewsRepository repository, String id) {
        this.newsDetailView = view;
        this.newsRepository = repository;
        this.articleId = id;
        this.newsDetailView.setPresenter(this);
    }


    @Override
    public void loadNewsDetail(String articleId) {
        newsDetailView.showLoading();
        Observable<NewsDetail> observable;
        if (userManager.isUserLogin()) {
            User user = userManager.getCurrentUser();
            RequestUser requestUser = new RequestUser();
            requestUser.setUser_id(user.getId() + "");
            observable = newsRepository.getNewsDetailByIdWithUserId(articleId, requestUser);
        } else {
            observable = newsRepository.getNewsDetailById(articleId);
        }

        loadSubscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsDetail>() {
                    @Override
                    public void call(NewsDetail newsDetail) {
                        newsDetailView.showNewsDetail(newsDetail);
                        newsDetailView.stopLoading();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        newsDetailView.showError();
                        newsDetailView.stopLoading();
                    }
                });
    }

    @Override
    public void postFavorite(String articleId, String userId) {
        RequestFavorite requestFavorite = new RequestFavorite();
        requestFavorite.setUser_id(userId);
        requestFavorite.setArticle_id(articleId);
        postFavoriteSubscription = newsRepository.postFavorite(requestFavorite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseFavorite>() {
                    @Override
                    public void call(ResponseFavorite responseFavorite) {
                        if (responseFavorite.getResponse_code() == 201) {
                            newsDetailView.updateFavorite(true);
                            newsDetailView.showMessage(context.getString(R.string.favorite_prompt));
                        } else {
                            newsDetailView.showMessage(responseFavorite.getResponse_msg());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        newsDetailView.showError();
                    }
                });
    }

    @Override
    public void deleteFavorite(String articleId, String userId) {
        deleteFavoriteSubscription = newsRepository.deleteFavorite(articleId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseFavorite>() {
                    @Override
                    public void call(ResponseFavorite responseFavorite) {
                        if (responseFavorite.getResponse_code() == 200) {
                            newsDetailView.updateFavorite(false);
                            newsDetailView.showMessage(context.getString(R.string.favorite_cancel_prompt));
                        } else {
                            newsDetailView.showMessage(responseFavorite.getResponse_msg());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        newsDetailView.showError();
                    }
                });
    }

    @Override
    public void start() {
        loadNewsDetail(articleId);
    }

    @Override
    public void destroy() {
        RxUtils.unsubscribe(loadSubscription, postFavoriteSubscription, deleteFavoriteSubscription);
    }
}
