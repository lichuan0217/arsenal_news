package top.lemonsoda.gunners.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import top.lemonsoda.gunners.NewsApplication;
import top.lemonsoda.gunners.R;
import top.lemonsoda.gunners.base.BaseActivity;
import top.lemonsoda.gunners.data.module.User;
import top.lemonsoda.gunners.data.network.WeiboService;
import top.lemonsoda.gunners.data.user.AccessTokenKeeper;
import top.lemonsoda.gunners.data.user.BitmapSaver;
import top.lemonsoda.gunners.data.user.UserManager;
import top.lemonsoda.gunners.utils.Constants;
import top.lemonsoda.gunners.utils.ShareUtils;

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getCanonicalName();

    @BindView(R.id.btn_login_weibo)
    Button btnLogin;

    @BindView(R.id.rl_login)
    View loginView;

    @BindView(R.id.rl_logout)
    View logoutView;

    @BindView(R.id.img_login_avatar)
    CircleImageView imgAvatar;

    @BindView(R.id.tv_login_username)
    TextView tvLoginUsername;

    @Inject
    UserManager userManager;

    @Inject
    AccessTokenKeeper accessTokenKeeper;

    @Inject
    WeiboService weiboService;

    @Inject
    BitmapSaver bitmapSaver;

    private AuthInfo authInfo;
    private Oauth2AccessToken accessToken;
    private SsoHandler ssoHandler;

    private ProgressDialog progressDialog;

    @OnClick(R.id.btn_login_weibo)
    void onLogin() {
        ssoHandler.authorize(new AuthListener());
    }

    @OnClick(R.id.btn_logout_weibo)
    void onLogout() {
        accessTokenKeeper.clear();
        accessToken = new Oauth2AccessToken();
        userManager.clear();
        updateView(false, true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_login;
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DaggerLoginActivityComponent.builder()
                .newsApplicationComponent(NewsApplication.get(this).getComponent())
                .build().inject(this);

        authInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        ssoHandler = new SsoHandler(LoginActivity.this, authInfo);
        accessToken = accessTokenKeeper.readAccessToken();
        Log.d(TAG, "accessToke : " + accessToken.isSessionValid());
        updateView(accessToken.isSessionValid(), true);
    }

    private void changeLoginView(boolean isLogin) {
        loginView.setVisibility(isLogin ? View.GONE : View.VISIBLE);
        logoutView.setVisibility(isLogin ? View.VISIBLE : View.GONE);
    }

    private void updateView(boolean isLogin, boolean loadInfo) {
        changeLoginView(isLogin);
        if (isLogin && loadInfo) {
            userManager.getUserInfo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<User>() {
                        @Override
                        public void call(User user) {
                            if (user.getId() != 0)
                                tvLoginUsername.setText(user.getScreen_name());
                        }
                    });
            bitmapSaver.loadBitmap()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Bitmap>() {
                        @Override
                        public void call(Bitmap bitmap) {
                            if (bitmap != null)
                                imgAvatar.setImageBitmap(bitmap);
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
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

    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken.isSessionValid()) {
                progressDialog = ProgressDialog.show(LoginActivity.this, "Login", "Authenticating...", true);
                final long uid = Long.parseLong(accessToken.getUid());
                weiboService.getUser(uid, accessToken.getToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<User>() {
                            @Override
                            public void call(User user) {
                                tvLoginUsername.setText(user.getScreen_name());
                                userManager.writeUserInfo(user);
                                progressDialog.dismiss();
                                updateView(true, false);
                                accessTokenKeeper.writeAccessToken(accessToken);
                                Toast.makeText(LoginActivity.this, "Auth Success", Toast.LENGTH_SHORT).show();
                                Glide.with(LoginActivity.this)
                                        .load(user.getProfile_image_url())
                                        .asBitmap()
                                        .centerCrop()
                                        .into(new BitmapImageViewTarget(imgAvatar) {
                                            @Override
                                            public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                super.onResourceReady(resource, glideAnimation);
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        bitmapSaver.save(resource);
                                                    }
                                                }).start();
                                            }
                                        });
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Auth Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

            } else {
                String code = values.getString("code");
                String message = "Auth Failed.";
                if (!TextUtils.isEmpty(code)) {
                    message = message + " code: " + code;
                }
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.d(TAG, "Auth Exception: " + e.getMessage());
            Toast.makeText(LoginActivity.this,
                    "Auth Exception: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Log.d(TAG, "Auth Cancel");
            Toast.makeText(LoginActivity.this, "Auth Cancel", Toast.LENGTH_SHORT).show();
        }
    }
}
