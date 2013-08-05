package com.andrada.sitracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.andrada.sitracker.events.AuthorSortMethodChanged;
import com.andrada.sitracker.tasks.UpdateAuthorsTask_;
import com.google.analytics.tracking.android.EasyTracker;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.SystemService;

import de.greenrobot.event.EventBus;

/**
 * Created by ggodonoga on 22/07/13.
 */
@EActivity
public class SettingsActivity extends SherlockPreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    @SystemService
    AlarmManager alarmManager;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            addPreferencesFromResource(R.xml.preferences);
        } else {
            addPreferencesFromResource(R.xml.preferences_no3g);
        }

        setUpdateIntervalSummary();
        setAuthorSortSummary();
        ActionBar actionBar = getSherlock().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        EasyTracker.getInstance().activityStop(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Boolean isSyncing = sharedPreferences.getBoolean(Constants.UPDATE_PREFERENCE_KEY, true);
        Intent intent = UpdateAuthorsTask_.intent(this.getApplicationContext()).get();
        PendingIntent pi = PendingIntent.getService(this.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pi);
        long updateInterval = Long.parseLong(sharedPreferences.getString(Constants.UPDATE_INTERVAL_KEY, "14400000"));
        if (isSyncing) {
            alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), updateInterval, pi);
        }

        if (key.equals(Constants.AUTHOR_SORT_TYPE_KEY)) {
            EventBus.getDefault().post(new AuthorSortMethodChanged());
            setAuthorSortSummary();
        }

        if (key.equals(Constants.UPDATE_INTERVAL_KEY)) {
            setUpdateIntervalSummary();
            EasyTracker.getTracker().sendEvent(
                    Constants.GA_UI_CATEGORY,
                    Constants.GA_EVENT_CHANGED_UPDATE_INTERVAL,
                    Constants.GA_EVENT_CHANGED_UPDATE_INTERVAL, updateInterval);
            EasyTracker.getInstance().dispatch();
        }
    }

    private void setUpdateIntervalSummary() {
        ListPreference updateInterval = (ListPreference) findPreference(Constants.UPDATE_INTERVAL_KEY);
        // Set summary to be the user-description for the selected value
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int index = updateInterval.findIndexOfValue(sharedPreferences.getString(Constants.UPDATE_INTERVAL_KEY, ""));
        if (index >= 0 && index < updateInterval.getEntries().length)
            updateInterval.setSummary(updateInterval.getEntries()[index]);
    }

    private void setAuthorSortSummary() {
        ListPreference authorsSortType = (ListPreference) findPreference(Constants.AUTHOR_SORT_TYPE_KEY);
        int sortType = Integer.parseInt(
                PreferenceManager.getDefaultSharedPreferences(this)
                        .getString(Constants.AUTHOR_SORT_TYPE_KEY, "0"));
        authorsSortType.setSummary(authorsSortType.getEntries()[sortType]);
    }
}