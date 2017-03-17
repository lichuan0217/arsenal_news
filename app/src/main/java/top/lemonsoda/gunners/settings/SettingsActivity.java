package top.lemonsoda.gunners.settings;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import butterknife.BindView;
import top.lemonsoda.gunners.R;
import top.lemonsoda.gunners.base.BaseActivity;
import top.lemonsoda.gunners.utils.ActivityUtils;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.fl_news_setting)
    FrameLayout flSettingContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_setting;
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SettingsFragment settingsFragment =
                (SettingsFragment) getFragmentManager()
                        .findFragmentById(R.id.fl_news_setting);

        if (settingsFragment == null) {
            settingsFragment = new SettingsFragment();
            ActivityUtils.addFragmentToActivity(
                    getFragmentManager(), settingsFragment, R.id.fl_news_setting);
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
}
