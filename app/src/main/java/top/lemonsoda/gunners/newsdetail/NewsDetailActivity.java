package top.lemonsoda.gunners.newsdetail;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import top.lemonsoda.gunners.NewsApplication;
import top.lemonsoda.gunners.R;
import top.lemonsoda.gunners.SplashActivity;
import top.lemonsoda.gunners.base.BaseActivity;
import top.lemonsoda.gunners.data.user.UserManager;
import top.lemonsoda.gunners.utils.ActivityUtils;
import top.lemonsoda.gunners.utils.Constants;
import top.lemonsoda.gunners.utils.ShareUtils;
import top.lemonsoda.gunners.utils.Utils;

public class NewsDetailActivity extends BaseActivity {
    private static final String TAG = NewsDetailActivity.class.getCanonicalName();

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.iv_article_header)
    ImageView ivArticleHeader;

    @BindView(R.id.fab_favorite)
    FloatingActionButton fabFavorite;

    @OnClick(R.id.fab_favorite)
    void onFavoriteClick() {
        if (!userManager.isUserLogin()) {
            Snackbar.make(collapsingToolbarLayout,
                    R.string.favorite_login_prompt, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (isFavorite) {
            presenter.deleteFavorite(articleId, userManager.getCurrentUser().getId() + "");
        } else {
            presenter.postFavorite(articleId, userManager.getCurrentUser().getId() + "");
        }
    }

    private String articleId;
    private String header;
    private int idSourceActivity;
    private boolean isFavorite;

    @Inject
    UserManager userManager;

    @Inject
    NewsDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_news_detail;
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black_trans80));
        }

        // Get intent extra data
        if (getIntent() != null) {
            header = getIntent().getStringExtra(Constants.INTENT_EXTRA_HEADER);
            articleId = getIntent().getStringExtra(Constants.INTENT_EXTRA_ARTICLE_ID);
            idSourceActivity = getIntent().getIntExtra(Constants.INTENT_EXTRA_SOURCE, -1);
        }

        // Set title
        collapsingToolbarLayout.setTitle(header);

        // Create View
        NewsDetailFragment newsDetailFragment =
                (NewsDetailFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fl_news_detail);
        if (newsDetailFragment == null) {
            newsDetailFragment = NewsDetailFragment.newInstance(articleId, header);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    newsDetailFragment, R.id.fl_news_detail);
        }

        // Inject Presenter using Dagger
        DaggerNewsDetailPresenterComponent.builder()
                .newsApplicationComponent(NewsApplication.get(this).getComponent())
                .newsDetailPresenterModule(new NewsDetailPresenterModule(newsDetailFragment, articleId))
                .build().inject(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        if (shareItem != null) {
            Utils.tintMenuIcon(this, shareItem, android.R.color.white);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_share:
                ShareUtils.share(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        if (idSourceActivity == Constants.ID_NEWS_FAVORITE_ACTIVITY) {
            Log.d(TAG, "SetResult for Favorite Activity");
            Intent intent = new Intent();
            intent.putExtra(Constants.INTENT_EXTRA_IS_FAVORITE, isFavorite);
            intent.putExtra(Constants.INTENT_EXTRA_ARTICLE_ID, articleId);
            setResult(RESULT_OK, intent);
        } else if (idSourceActivity == Constants.ID_NEWS_RECEIVER) {
            Log.d(TAG, "Come From NewsReceiver");
            if (isTaskRoot()) {
                Intent intent = new Intent(NewsDetailActivity.this, SplashActivity.class);
                startActivity(intent);
            }
        }
        super.onBackPressed();
    }

    public void updateFABFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
        if (isFavorite) {
            fabFavorite.setImageResource(R.mipmap.ic_like_filled);
        } else {
            fabFavorite.setImageResource(R.mipmap.ic_like_outline);
        }
    }

}
