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
import com.spstanchev.premierleague.adapters.ResultsCustomAdapter;
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
public class ResultsFragment extends Fragment implements AsyncJsonResponse {

    private static final String TAG = PremierLeagueFragment.class.getSimpleName();
    private ResultsCustomAdapter adapter;
    private DBHelper dbHelper;
    private ArrayList<Fixture> results;
    private League league;
    private String numberOfDays;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(SettingsActivity.KEY_PREF_TIMEFRAME_RESULTS)){
                String newNumberOfDays = sharedPreferences.getString(SettingsActivity.KEY_PREF_TIMEFRAME_RESULTS, "7");
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
        adapter = new ResultsCustomAdapter(activity);
        dbHelper = DBHelper.getInstance(getActivity());
        league = dbHelper.getLeague(1);
        results = dbHelper.getAllResults();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        numberOfDays = sharedPreferences.getString(SettingsActivity.KEY_PREF_TIMEFRAME_RESULTS, "7");
        if (Utils.isNetworkAvailable(getActivity()) && isUpdateNeeded()) {
            downloadJsonData();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_section_fixtures, container, false);
        View headerView = inflater.inflate(R.layout.fragment_section_fixtures_header, null);
        TextView tvResultsTitle = (TextView) headerView.findViewById(R.id.tvFixtureTitle);
        tvResultsTitle.setText(getString(R.string.label_english_pl_results));

        ListView listViewResults = (ListView) rootView.findViewById(R.id.lvFixtures);
        listViewResults.addHeaderView(headerView, results, false);
        listViewResults.setClickable(false);
        listViewResults.setAdapter(adapter);
        adapter.updateCollection(results);

        return rootView;
    }

    private boolean isUpdateNeeded() {
        boolean isUpdateNeeded = false;
        if (results.size() == 0) {
            isUpdateNeeded = true;
        } else if (league != null) {
            Date jsonLastUpdated = Utils.getDateFromString(league.getLastUpdated(), Utils.getSimpleDateFormat());
            String lastUpdatedString = sharedPreferences.getString(SettingsActivity.KEY_PREF_RESULTS_LAST_UPDATED, "");
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

    private void downloadJsonData() {
        DownloadJsonData downloadResults = new DownloadJsonData(this);
        downloadResults.execute(Constants.JSON_RESULTS_URL + numberOfDays);
    }

    @Override
    public void handleLeagueJsonResponse(String response) {

    }

    @Override
    public void handleTeamsJsonResponse(String response) {

    }

    @Override
    public void handleResultsJsonResponse(String response) {
        Log.d(TAG, "Results response: " + response);
        if (response != null) {
            try {
                JSONObject jsonFixtures = new JSONObject(response);
                JSONArray jsonFixturesArray = jsonFixtures.getJSONArray(Constants.TAG_FIXTURES);
                results = new ArrayList<Fixture>();
                for (int i = 0; i < jsonFixturesArray.length(); i++) {
                    String date = jsonFixturesArray.getJSONObject(i).getString(Constants.TAG_DATE);
                    int matchday = jsonFixturesArray.getJSONObject(i).getInt(Constants.TAG_MATCHDAY);
                    String homeTeamName = jsonFixturesArray.getJSONObject(i).getString(Constants.TAG_HOME_TEAM_NAME);
                    String awayTeamName = jsonFixturesArray.getJSONObject(i).getString(Constants.TAG_AWAY_TEAM_NAME);
                    int goalsHomeTeam = jsonFixturesArray.getJSONObject(i).getJSONObject(Constants.TAG_RESULT)
                            .getInt(Constants.TAG_GOALS_HOME_TEAM);
                    int goalsAwayTeam = jsonFixturesArray.getJSONObject(i).getJSONObject(Constants.TAG_RESULT)
                            .getInt(Constants.TAG_GOALS_AWAY_TEAM);
                    Fixture currentResult = new Fixture();
                    currentResult.setDate(date);
                    currentResult.setMatchday(matchday);
                    currentResult.setHomeTeamName(homeTeamName);
                    currentResult.setAwayTeamName(awayTeamName);
                    currentResult.setGoalsHomeTeam(goalsHomeTeam);
                    currentResult.setGoalsAwayTeam(goalsAwayTeam);
                    results.add(currentResult);
                    Fixture dbFixture = dbHelper.getResult(i + 1);
                    if (dbFixture == null) {
                        if (dbHelper.insertResult(currentResult) == -1)
                            Log.e(TAG, getString(R.string.error_message_unable_to_insert_row_in_db));
                    } else {
                        if (dbHelper.updateResult(i + 1, currentResult) == 0)
                            Log.e(TAG, getString(R.string.error_message_unable_to_update_row_in_db));
                    }

                }
                adapter.updateCollection(results);
                String currentDate = Utils.getSimpleDateFormat().format(new Date());
                Utils.saveStringToSharedPreferences(sharedPreferences, SettingsActivity.KEY_PREF_RESULTS_LAST_UPDATED, currentDate);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e(TAG, getString(R.string.error_message_no_json_response_received));
        }
    }

    @Override
    public void handleFixturesJsonResponse(String response) {

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
