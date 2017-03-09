package top.lemonsoda.gunners.newsdetail;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import top.lemonsoda.gunners.data.NewsRepository;
import top.lemonsoda.gunners.data.module.NewsDetail;
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

        loadSubscription = newsRepository.getNewsDetailById(articleId)
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
    public void start() {
        loadNewsDetail(articleId);
    }

    @Override
    public void destroy() {
        RxUtils.unsubscribe(loadSubscription);
    }
}
