package top.lemonsoda.gunners.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import top.lemonsoda.gunners.R;
import top.lemonsoda.gunners.utils.Constants;

/**
 * Created by Chuan on 17/03/2017.
 */

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private CheckBoxPreference prefNotify;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_main);
        prefNotify = (CheckBoxPreference) findPreference(Constants.PREF_KEY_NOTIFY);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Constants.PREF_KEY_NOTIFY)) {
            Toast.makeText(getActivity(),
                    "key: " + key + ", values: " + sharedPreferences.getBoolean(key, false),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
