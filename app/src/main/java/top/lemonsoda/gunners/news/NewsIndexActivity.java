package top.lemonsoda.gunners.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import top.lemonsoda.gunners.NewsApplication;
import top.lemonsoda.gunners.R;
import top.lemonsoda.gunners.base.BaseActivity;
import top.lemonsoda.gunners.data.module.User;
import top.lemonsoda.gunners.data.user.BitmapSaver;
import top.lemonsoda.gunners.data.user.UserManager;
import top.lemonsoda.gunners.login.LoginActivity;
import top.lemonsoda.gunners.newsabout.AboutActivity;
import top.lemonsoda.gunners.newsdetail.NewsDetailActivity;
import top.lemonsoda.gunners.newsfavorite.NewsFavoriteActivity;
import top.lemonsoda.gunners.utils.ActivityUtils;
import top.lemonsoda.gunners.utils.Constants;
import top.lemonsoda.gunners.utils.ui.OnNewsIndexItemClickListener;

public class NewsIndexActivity extends BaseActivity implements OnNewsIndexItemClickListener {
    private static final String TAG = NewsIndexActivity.class.getCanonicalName();

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.fl_news_index)
    FrameLayout fl_news_index_container;

    CircleImageView imAvatar;
    TextView tvUsername;

    @Inject
    NewsIndexPresenter newsPresenter;

    @Inject
    UserManager userManager;

    @Inject
    BitmapSaver bitmapSaver;

    private boolean isAvatarLoaded;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_news_index;
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // Bind NavigationView's header view
        View headView = navigationView.getHeaderView(0);
        imAvatar = ButterKnife.findById(headView, R.id.img_avatar);
        tvUsername = ButterKnife.findById(headView, R.id.tv_user_name);

        // Setup NavigationDrawer
        setupNavigationDrawer();

        // Create View
        NewsIndexFragment newsIndexFragment =
                (NewsIndexFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fl_news_index);
        if (newsIndexFragment == null) {
            newsIndexFragment = NewsIndexFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    newsIndexFragment, R.id.fl_news_index);
        }

        // Create Presenter
        DaggerNewsIndexPresenterComponent.builder()
                .newsApplicationComponent(NewsApplication.get(this).getComponent())
                .newsIndexPresenterModule(new NewsIndexPresenterModule(newsIndexFragment))
                .build()
                .inject(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userManager.isUserLogin() && !isAvatarLoaded) {
            Log.d(TAG, "User logged in, update UI");
            userManager.getUserInfo()
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<User>() {
                        @Override
                        public void call(User user) {
                            if (user.getId() != 0)
                                tvUsername.setText(user.getScreen_name());
                        }
                    });
            bitmapSaver.loadBitmap()
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Bitmap>() {
                        @Override
                        public void call(Bitmap bitmap) {
                            if (bitmap != null) {
                                imAvatar.setImageBitmap(bitmap);
                                isAvatarLoaded = true;
                            }
                        }
                    });
        }
        if (!userManager.isUserLogin() && isAvatarLoaded) {
            Log.d(TAG, "User logged out, update UI");
            tvUsername.setText("Arsenal Fans");
            imAvatar.setImageResource(R.mipmap.avatar);
            isAvatarLoaded = false;
        }
    }

    @Override
    public void onItemClick(String articleId, String header) {
        Log.d(TAG, "Item clicked: " + articleId + ", " + header);
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_ARTICLE_ID, articleId);
        intent.putExtra(Constants.INTENT_EXTRA_HEADER, header);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        if ((System.currentTimeMillis() - exitTime) > 3000) {
            Snackbar.make(
                    fl_news_index_container,
                    getString(R.string.snake_exit_once_more),
                    Snackbar.LENGTH_LONG).show();
            exitTime = System.currentTimeMillis();
            return;
        } else {
            finish();
        }
        super.onBackPressed();
    }

    private void setupNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        drawerLayout.closeDrawers();
                        switch (item.getItemId()) {
                            case R.id.nav_favorite:
                                if (!userManager.isUserLogin()) {
                                    Snackbar.make(
                                            fl_news_index_container,
                                            R.string.favorite_login_prompt,
                                            Snackbar.LENGTH_SHORT)
                                            .setAction("Action", null).show();
                                    return true;
                                }
                                startActivity(new Intent(NewsIndexActivity.this, NewsFavoriteActivity.class));
                                return true;
//                                Bundle bundle = new Bundle();
//                                bundle.putString(Constants.INTENT_EXTRA_USER_ID, user.getId() + "");
                            case R.id.nav_about:
                                startActivity(new Intent(NewsIndexActivity.this, AboutActivity.class));
                                return true;
//                            case R.id.nav_settings:
//                                return switchActivity(SettingsActivity.class);
                        }
                        return true;
                    }
                });
        setupToggle();
        setupHeaderListener();
    }

    private void setupToggle() {
        // Initializing Drawer Layout and ActionBarToggle
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void setupHeaderListener() {
        imAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsIndexActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
