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

import com.spstanchev.premierleague.common.AsyncJsonResponse;
import com.spstanchev.premierleague.common.DownloadJsonData;
import com.spstanchev.premierleague.R;
import com.spstanchev.premierleague.activities.SettingsActivity;
import com.spstanchev.premierleague.adapters.TableCustomAdapter;
import com.spstanchev.premierleague.common.Constants;
import com.spstanchev.premierleague.common.DBHelper;
import com.spstanchev.premierleague.common.Utils;
import com.spstanchev.premierleague.models.League;
import com.spstanchev.premierleague.models.LeagueTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Stefan on 1/26/2015.
 */
public class LeagueTableFragment extends Fragment implements AsyncJsonResponse {

    private static final String TAG = PremierLeagueFragment.class.getSimpleName();
    private TableCustomAdapter adapter;
    private DBHelper dbHelper;
    private League league;
    private ArrayList<LeagueTable> leagueTable;
    private SharedPreferences sharedPreferences;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        adapter = new TableCustomAdapter(activity);
        dbHelper = DBHelper.getInstance(getActivity());
        league = dbHelper.getLeague(1);
        leagueTable = dbHelper.getLeagueTable();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        if (Utils.isNetworkAvailable(getActivity()) && isUpdateNeeded()) {
            downloadJsonData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_section_league_table, container, false);
        View headerView = inflater.inflate(R.layout.fragment_section_league_table_header, null);

        ListView listViewTable = (ListView) rootView.findViewById(R.id.lvTable);
        listViewTable.addHeaderView(headerView, leagueTable, false);
        listViewTable.setClickable(false);
        listViewTable.setAdapter(adapter);
        adapter.updateCollection(leagueTable);

        return rootView;
    }

    private boolean isUpdateNeeded() {
        boolean isUpdateNeeded = false;
        if (leagueTable.size() == 0) {
            isUpdateNeeded = true;
        } else if (league != null) {
            Date jsonLastUpdated = Utils.getDateFromString(league.getLastUpdated(), Utils.getSimpleDateFormat());
            String lastUpdatedString = sharedPreferences.getString(SettingsActivity.KEY_PREF_TABLE_LAST_UPDATED, "");
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
        DownloadJsonData downloadLeagueTable = new DownloadJsonData(this);
        downloadLeagueTable.execute(Constants.JSON_LEAGUE_TABLE_URL);
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

    }

    @Override
    public void handleLeagueTableJsonResponse(String response) {
        Log.d(TAG, "League table response: " + response);
        if (response != null) {
            try {
                JSONObject jsonStanding = new JSONObject(response);
                JSONArray jsonStandingArray = jsonStanding.getJSONArray(Constants.TAG_STANDING);
                int matchday = jsonStanding.getInt(Constants.TAG_MATCHDAY);
                leagueTable = new ArrayList<LeagueTable>();
                for (int i = 0; i < jsonStandingArray.length(); i++) {
                    int position = jsonStandingArray.getJSONObject(i).getInt(Constants.TAG_POSITION);
                    String teamName = jsonStandingArray.getJSONObject(i).getString(Constants.TAG_TEAM_NAME);
                    int playedGames = jsonStandingArray.getJSONObject(i).getInt(Constants.TAG_PLAYED_GAMES);
                    int points = jsonStandingArray.getJSONObject(i).getInt(Constants.TAG_POINTS);
                    int goals = jsonStandingArray.getJSONObject(i).getInt(Constants.TAG_GOALS);
                    int goalsAgainst = jsonStandingArray.getJSONObject(i).getInt(Constants.TAG_GOALS_AGAINST);
                    int goalDifference = jsonStandingArray.getJSONObject(i).getInt(Constants.TAG_GOAL_DIFFERENCE);
                    LeagueTable currentTableEntry = new LeagueTable();
                    currentTableEntry.setMatchday(matchday);
                    currentTableEntry.setPosition(position);
                    currentTableEntry.setTeamName(teamName);
                    currentTableEntry.setPlayedGames(playedGames);
                    currentTableEntry.setPoints(points);
                    currentTableEntry.setGoals(goals);
                    currentTableEntry.setGoalsAgainst(goalsAgainst);
                    currentTableEntry.setGoalDifference(goalDifference);
                    leagueTable.add(currentTableEntry);
                    LeagueTable dbLeagueTable = dbHelper.getTeamFromLeagueTable(i + 1);
                    if (dbLeagueTable == null) {
                        if (dbHelper.insertLeagueTable(currentTableEntry) == -1)
                            Log.e(TAG, getString(R.string.error_message_unable_to_insert_row_in_db));
                    } else {
                        if (dbHelper.updateLeagueTable(i + 1, currentTableEntry) == 0)
                            Log.e(TAG, getString(R.string.error_message_unable_to_update_row_in_db));
                    }
                    adapter.updateCollection(leagueTable);
                    String currentDate = Utils.getSimpleDateFormat().format(new Date());
                    Utils.saveStringToSharedPreferences(sharedPreferences, SettingsActivity.KEY_PREF_TABLE_LAST_UPDATED, currentDate);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e(TAG, getString(R.string.error_message_no_json_response_received));
        }
    }
}
