package top.lemonsoda.gunners.newsdetail;


import top.lemonsoda.gunners.base.BasePresenter;
import top.lemonsoda.gunners.base.BaseView;
import top.lemonsoda.gunners.data.module.NewsDetail;

/**
 * Created by chuanl on 2/21/17.
 */

public interface NewsDetailContract {

    interface Presenter extends BasePresenter {
        void loadNewsDetail(String articleId);
    }

    interface View extends BaseView<Presenter> {
        void showLoading();

        void stopLoading();

        void showError();

        void showNewsDetail(NewsDetail newsDetail);
    }
}
