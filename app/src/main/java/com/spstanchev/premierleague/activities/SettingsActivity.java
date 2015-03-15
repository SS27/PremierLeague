package com.spstanchev.premierleague.activities;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.spstanchev.premierleague.R;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();
    public static final String KEY_PREF_TIMEFRAME_RESULTS = "timeframe_results";
    public static final String KEY_PREF_TIMEFRAME_FIXTURES = "timeframe_fixtures";
    public static final String KEY_PREF_TEAMS_LAST_UPDATED = "teams_last_updated";
    public static final String KEY_PREF_RESULTS_LAST_UPDATED = "results_last_updated";
    public static final String KEY_PREF_FIXTURES_LAST_UPDATED = "fixtures_last_updated";
    public static final String KEY_PREF_TABLE_LAST_UPDATED = "table_last_updated";

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupSimplePreferencesScreen();
    }

    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    private void setupSimplePreferencesScreen() {

        // In the simplified UI, fragments are not used at all and we instead
        // use the older PreferenceActivity APIs.

        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to
        // their values. When their values change, their summaries are updated
        // to reflect the new value, per the Android Design guidelines.
        bindPreferenceSummaryToValue(findPreference(KEY_PREF_TIMEFRAME_RESULTS));
        bindPreferenceSummaryToValue(findPreference(KEY_PREF_TIMEFRAME_FIXTURES));
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();
        try {
            int intValue = Integer.parseInt(stringValue);
            if (intValue < 1 || intValue > 99){
                Toast.makeText(this, getString(R.string.toast_invalid_timeframe), Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Log.e (TAG, "Couldn't parse value to int!", e);
            return false;
        }

        // For all other preferences, set the summary to the value's
        // simple string representation.
        preference.setSummary(stringValue);

        return true;
    }
}
