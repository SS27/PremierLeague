package com.spstanchev.premierleague.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.spstanchev.premierleague.common.AsyncJsonResponse;
import com.spstanchev.premierleague.common.DownloadJsonData;
import com.spstanchev.premierleague.R;
import com.spstanchev.premierleague.activities.SettingsActivity;
import com.spstanchev.premierleague.adapters.FixturesCustomAdapter;
import com.spstanchev.premierleague.common.Constants;
import com.spstanchev.premierleague.common.DBHelper;
import com.spstanchev.premierleague.common.Utils;
import com.spstanchev.premierleague.models.Fixture;
import com.spstanchev.premierleague.models.League;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Stefan on 1/25/2015.
 */
public class FixturesFragment extends Fragment implements AsyncJsonResponse {

    private static final String TAG = PremierLeagueFragment.class.getSimpleName();
    private FixturesCustomAdapter adapter;
    private DBHelper dbHelper;
    private ArrayList<Fixture> fixtures;
    private League league;
    private String numberOfDays;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(SettingsActivity.KEY_PREF_TIMEFRAME_FIXTURES)){
                String newNumberOfDays = sharedPreferences.getString(SettingsActivity.KEY_PREF_TIMEFRAME_FIXTURES, "7");
                if (!newNumberOfDays.equals(numberOfDays)){
                    numberOfDays = newNumberOfDays;
                    downloadJsonData();
                }
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        adapter = new FixturesCustomAdapter(activity);
        dbHelper = DBHelper.getInstance(activity);
        league = dbHelper.getLeague(1);
        fixtures = dbHelper.getAllFixtures();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        numberOfDays = sharedPreferences.getString(SettingsActivity.KEY_PREF_TIMEFRAME_FIXTURES, "7");
        //setRetainInstance(true);
        if (Utils.isNetworkAvailable(getActivity()) && isUpdateNeeded()) {
            downloadJsonData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_section_fixtures, container, false);
        View headerView = inflater.inflate(R.layout.fragment_section_fixtures_header, null);
        TextView tvResult = (TextView) headerView.findViewById(R.id.tvResult);
        tvResult.setVisibility(View.INVISIBLE);
        TextView tvFixtureTitle = (TextView) headerView.findViewById(R.id.tvFixtureTitle);
        tvFixtureTitle.setText(getString(R.string.label_english_pl_fixtures));

        ListView listViewFixtures = (ListView) rootView.findViewById(R.id.lvFixtures);
        listViewFixtures.addHeaderView(headerView, fixtures, false);
        listViewFixtures.setClickable(false);
        listViewFixtures.setAdapter(adapter);
        adapter.updateCollection(fixtures);

        return rootView;
    }

    private void downloadJsonData() {
        DownloadJsonData downloadFixtures = new DownloadJsonData(this);
        downloadFixtures.execute(Constants.JSON_FIXTURES_URL + numberOfDays);
    }

    private boolean isUpdateNeeded() {
        boolean isUpdateNeeded = false;
        if (fixtures.size() == 0) {
            isUpdateNeeded = true;
        } else if (league != null) {
            Date jsonLastUpdated = Utils.getDateFromString(league.getLastUpdated(), Utils.getSimpleDateFormat());
            String lastUpdatedString = sharedPreferences.getString(SettingsActivity.KEY_PREF_FIXTURES_LAST_UPDATED, "");
            Date lastUpdated = Utils.getDateFromString(lastUpdatedString, Utils.getSimpleDateFormat());
            if (jsonLastUpdated == null || lastUpdated == null) {
                Log.e(TAG, "Error! Dates shouldn't be null");
            } else if (jsonLastUpdated.after(lastUpdated)) {
                Log.d(TAG, String.format(getString(R.string.log_message_tables_not_up_to_date), jsonLastUpdated, lastUpdated));
                isUpdateNeeded = true;
            }
        }
        return isUpdateNeeded;
    }

    @Override
    public void handleLeagueJsonResponse(String response) {

    }

    @Override
    public void handleTeamsJsonResponse(String response) {

    }

    @Override
    public void handleResultsJsonResponse(String response) {

    }

    @Override
    public void handleFixturesJsonResponse(String response) {
        Log.d(TAG, "Results response: " + response);
        if (response != null) {
            try {
                JSONObject jsonFixtures = new JSONObject(response);
                JSONArray jsonFixturesArray = jsonFixtures.getJSONArray(Constants.TAG_FIXTURES);
                fixtures = new ArrayList<Fixture>();
                for (int i = 0; i < jsonFixturesArray.length(); i++) {
                    String date = jsonFixturesArray.getJSONObject(i).getString(Constants.TAG_DATE);
                    int matchday = jsonFixturesArray.getJSONObject(i).getInt(Constants.TAG_MATCHDAY);
                    String homeTeamName = jsonFixturesArray.getJSONObject(i).getString(Constants.TAG_HOME_TEAM_NAME);
                    String awayTeamName = jsonFixturesArray.getJSONObject(i).getString(Constants.TAG_AWAY_TEAM_NAME);
                    int goalsHomeTeam = jsonFixturesArray.getJSONObject(i).getJSONObject(Constants.TAG_RESULT)
                            .getInt(Constants.TAG_GOALS_HOME_TEAM);
                    int goalsAwayTeam = jsonFixturesArray.getJSONObject(i).getJSONObject(Constants.TAG_RESULT)
                            .getInt(Constants.TAG_GOALS_AWAY_TEAM);
                    Fixture currentFixture = new Fixture();
                    currentFixture.setDate(date);
                    currentFixture.setMatchday(matchday);
                    currentFixture.setHomeTeamName(homeTeamName);
                    currentFixture.setAwayTeamName(awayTeamName);
                    currentFixture.setGoalsHomeTeam(goalsHomeTeam);
                    currentFixture.setGoalsAwayTeam(goalsAwayTeam);
                    fixtures.add(currentFixture);
                    Fixture dbFixture = dbHelper.getFixture(i + 1);
                    if (dbFixture == null) {
                        if (dbHelper.insertFixture(currentFixture) == -1)
                            Log.e(TAG, getString(R.string.error_message_unable_to_insert_row_in_db));
                    } else {
                        if (dbHelper.updateFixture(i + 1, currentFixture) == 0)
                            Log.e(TAG, getString(R.string.error_message_unable_to_update_row_in_db));
                    }

                }
                adapter.updateCollection(fixtures);
                String currentDate = Utils.getSimpleDateFormat().format(new Date());
                Utils.saveStringToSharedPreferences(sharedPreferences, SettingsActivity.KEY_PREF_FIXTURES_LAST_UPDATED, currentDate);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e(TAG, getString(R.string.error_message_no_json_response_received));
        }
    }

    @Override
    public void handleLeagueTableJsonResponse(String response) {

    }

    @Override
    public void onDestroy() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
        super.onDestroy();
    }
}
