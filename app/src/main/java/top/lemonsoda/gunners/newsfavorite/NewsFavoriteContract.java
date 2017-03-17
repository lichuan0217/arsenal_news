package top.lemonsoda.gunners.newsfavorite;

import java.util.List;

import top.lemonsoda.gunners.base.BasePresenter;
import top.lemonsoda.gunners.base.BaseView;
import top.lemonsoda.gunners.data.module.News;

/**
 * Created by Chuan on 09/03/2017.
 */

public interface NewsFavoriteContract {

    interface Presenter extends BasePresenter {
        void loadFavoriteNews(boolean forceUpdate);

        void updateFavoriteNews(boolean isFavorite, String articleId);
    }

    interface View extends BaseView<NewsFavoriteContract.Presenter> {
        void showLoading();

        void stopLoading();

        void showFavoriteNews(List<News> newsList);

        void showError(String error);

        void updateFavoriteNews(String articleId);
    }
}
