package top.lemonsoda.gunners.newsabout;

import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import top.lemonsoda.gunners.R;
import top.lemonsoda.gunners.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_about;
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black_trans80));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvVersion.setText(getString(R.string.app_version));
        collapsingToolbarLayout.setTitle(getString(R.string.title_activity_about));
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
}
