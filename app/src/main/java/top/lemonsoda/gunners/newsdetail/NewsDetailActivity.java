package top.lemonsoda.gunners.newsdetail;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import javax.inject.Inject;

import butterknife.BindView;
import top.lemonsoda.gunners.NewsApplication;
import top.lemonsoda.gunners.R;
import top.lemonsoda.gunners.base.BaseActivity;
import top.lemonsoda.gunners.utils.ActivityUtils;
import top.lemonsoda.gunners.utils.Constants;
import top.lemonsoda.gunners.utils.ShareUtils;
import top.lemonsoda.gunners.utils.Utils;

public class NewsDetailActivity extends BaseActivity {

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.iv_article_header)
    ImageView ivArticleHeader;

    private String articleId;
    private String header;

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

}
