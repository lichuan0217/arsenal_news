package top.lemonsoda.gunners.newsfavorite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.BindView;
import top.lemonsoda.gunners.NewsApplication;
import top.lemonsoda.gunners.R;
import top.lemonsoda.gunners.base.BaseActivity;
import top.lemonsoda.gunners.newsdetail.NewsDetailActivity;
import top.lemonsoda.gunners.utils.ActivityUtils;
import top.lemonsoda.gunners.utils.Constants;
import top.lemonsoda.gunners.utils.ui.OnNewsIndexItemClickListener;

public class NewsFavoriteActivity extends BaseActivity implements OnNewsIndexItemClickListener {
    private static final String TAG = NewsFavoriteActivity.class.getCanonicalName();

    @BindView(R.id.fl_news_favorite)
    FrameLayout flNewsFavoriteContainer;

    @Inject
    NewsFavoritePresenter newsFavoritePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_news_favorite;
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NewsFavoriteFragment fragment =
                (NewsFavoriteFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fl_news_favorite);
        if (fragment == null) {
            fragment = NewsFavoriteFragment.getInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.fl_news_favorite);
        }

        DaggerNewsFavoritePresenterComponent.builder()
                .newsFavoritePresenterModule(new NewsFavoritePresenterModule(fragment))
                .newsApplicationComponent(NewsApplication.get(this).getComponent())
                .build().inject(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(String articleId, String header) {
        Log.d(TAG, "onItemClick: " + articleId + ", " + header);
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_ARTICLE_ID, articleId);
        intent.putExtra(Constants.INTENT_EXTRA_HEADER, header);
        startActivity(intent);
    }
}
