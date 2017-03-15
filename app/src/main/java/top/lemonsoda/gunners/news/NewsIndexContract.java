package top.lemonsoda.gunners.news;

import java.util.List;

import top.lemonsoda.gunners.base.BasePresenter;
import top.lemonsoda.gunners.base.BaseView;
import top.lemonsoda.gunners.data.module.News;


/**
 * Created by chuanl on 2/10/17.
 */

public interface NewsIndexContract {

    interface Presenter extends BasePresenter {
        void loadNews(boolean forceUpdate);

        void loadMoreNews(int page);
    }

    interface View extends BaseView<Presenter> {
        void showLoading();

        void stopLoading();

        void showNewsIndex(List<News> newsList);

        void showMoreNewsIndex(List<News> newsList);

        void showError(String msg);

        void showLoadMoreError(String msg);
    }
}
