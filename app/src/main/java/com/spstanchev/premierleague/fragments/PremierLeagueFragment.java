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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.spstanchev.premierleague.common.AsyncJsonResponse;
import com.spstanchev.premierleague.common.DownloadJsonData;
import com.spstanchev.premierleague.R;
import com.spstanchev.premierleague.activities.SettingsActivity;
import com.spstanchev.premierleague.adapters.TeamsCustomAdapter;
import com.spstanchev.premierleague.common.Constants;
import com.spstanchev.premierleague.common.DBHelper;
import com.spstanchev.premierleague.common.Utils;
import com.spstanchev.premierleague.models.League;
import com.spstanchev.premierleague.models.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Stefan on 1/24/2015.
 */
public class PremierLeagueFragment extends Fragment implements AsyncJsonResponse {

    private static final String TAG = PremierLeagueFragment.class.getSimpleName();
    private TeamsCustomAdapter adapter;
    private DBHelper dbHelper;
    private TextView tvCaption, tvTeams, tvNumberOfTeamsLabel, tvNumberOfTeams, tvNumberOfGamesLabel, tvNumberOfGames;
    private ImageView ivLeagueCaption;
    private ArrayList<Team> teams;
    private League league;
    private SharedPreferences sharedPreferences;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        adapter = new TeamsCustomAdapter(activity);
        dbHelper = DBHelper.getInstance(getActivity());
        league = dbHelper.getLeague(1);
        teams = dbHelper.getAllTeams();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    private void downloadJsonLeagueData() {
        DownloadJsonData downloadLeague = new DownloadJsonData(this);
        downloadLeague.execute(Constants.JSON_LEAGUE_URL);
    }

    private void downloadJsonTeamsData() {
        DownloadJsonData downloadTeams = new DownloadJsonData(this);
        downloadTeams.execute(Constants.JSON_TEAMS_URL);
    }

    private boolean isUpdateNeeded() {
        boolean isUpdateNeeded = false;
        if (teams.size() == 0) {
            isUpdateNeeded = true;
        } else if (league != null) {
            Date jsonLastUpdated = Utils.getDateFromString(league.getLastUpdated(), Utils.getSimpleDateFormat());
            String lastUpdatedString = sharedPreferences.getString(SettingsActivity.KEY_PREF_TEAMS_LAST_UPDATED, "");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_section_premier_league, container, false);
        View headerView = inflater.inflate(R.layout.fragment_section_premier_league_header, null);
        tvCaption = (TextView) headerView.findViewById(R.id.tvLeagueCaption);
        ivLeagueCaption = (ImageView) headerView.findViewById(R.id.ivLeagueLogo);
        tvTeams = (TextView) headerView.findViewById(R.id.tvTeams);
        tvNumberOfTeamsLabel = (TextView) headerView.findViewById(R.id.tvNumberOfTeamsLabel);
        tvNumberOfTeams = (TextView) headerView.findViewById(R.id.tvNumberOfTeams);
        tvNumberOfGamesLabel = (TextView) headerView.findViewById(R.id.tvNumberOfGamesLabel);
        tvNumberOfGames = (TextView) headerView.findViewById(R.id.tvNumberOfGames);

        ListView listViewTeams = (ListView) rootView.findViewById(R.id.lvTeams);
        listViewTeams.addHeaderView(headerView, teams, false);
        listViewTeams.setClickable(false);
        listViewTeams.setAdapter(adapter);

        ivLeagueCaption.setImageResource(R.drawable.premierleague_logo);
        tvNumberOfTeamsLabel.setText(getString(R.string.label_number_of_teams));
        tvNumberOfGamesLabel.setText(getString(R.string.label_number_of_games));
        tvTeams.setText(getString(R.string.list_of_teams));

        if (league != null) {
            tvCaption.setText(league.getCaption());
            tvNumberOfTeams.setText(league.getNumberOfTeams().toString());
            tvNumberOfGames.setText(league.getNumberOfGames().toString());
        }

        adapter.updateCollection(teams);

        if (Utils.isNetworkAvailable(getActivity())) {
            downloadJsonLeagueData();
            if (isUpdateNeeded()) {
                downloadJsonTeamsData();
            }
        }

        return rootView;
    }

    @Override
    public void handleLeagueJsonResponse(String response) {
        Log.d(TAG, "League response: " + response);
        if (response != null) {
            try {
                JSONObject jsonLeague = new JSONObject(response);
                String caption = jsonLeague.getString(Constants.TAG_CAPTION);
                String leagueShortName = jsonLeague.getString(Constants.TAG_LEAGUE);
                String year = jsonLeague.getString(Constants.TAG_YEAR);
                int numberOfTeams = jsonLeague.getInt(Constants.TAG_NUMBER_OF_TEAMS);
                int numberOfGames = jsonLeague.getInt(Constants.TAG_NUMBER_OF_GAMES);
                String lastUpdated = jsonLeague.getString(Constants.TAG_LAST_UPDATED);
                League premierLeague = new League();
                premierLeague.setCaption(caption);
                premierLeague.setLeague(leagueShortName);
                premierLeague.setYear(year);
                premierLeague.setNumberOfTeams(numberOfTeams);
                premierLeague.setNumberOfGames(numberOfGames);
                premierLeague.setLastUpdated(lastUpdated);
                League dbLeague = dbHelper.getLeague(1);
                if (dbLeague == null) {
                    if (dbHelper.insertLeague(premierLeague) == -1)
                        Log.e(TAG, getString(R.string.error_message_unable_to_insert_row_in_db));
                } else {
                    if (dbHelper.updateLeague(1, premierLeague) == 0)
                        Log.e(TAG, getString(R.string.error_message_unable_to_update_row_in_db));
                }
                tvCaption.setText(caption);
                tvNumberOfTeams.setText(String.valueOf(numberOfTeams));
                tvNumberOfGames.setText(String.valueOf(numberOfGames));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, getString(R.string.error_message_no_json_response_received));
        }
    }

    @Override
    public void handleTeamsJsonResponse(String response) {
        Log.d(TAG, "Teams response: " + response);
        if (response != null) {
            try {
                JSONObject jsonTeam = new JSONObject(response);
                JSONArray jsonTeamsArray = jsonTeam.getJSONArray(Constants.TAG_TEAMS);
                teams = new ArrayList<Team>();
                for (int i = 0; i < jsonTeamsArray.length(); i++) {
                    String name = jsonTeamsArray.getJSONObject(i).getString(Constants.TAG_NAME);
                    String code = jsonTeamsArray.getJSONObject(i).getString(Constants.TAG_CODE);
                    String shortName = jsonTeamsArray.getJSONObject(i).getString(Constants.TAG_SHORT_NAME);
                    String squadMarketValue = jsonTeamsArray.getJSONObject(i).getString(Constants.TAG_SQUAD_MARKET_VALUE);
                    String crestUrl = jsonTeamsArray.getJSONObject(i).getString(Constants.TAG_CREST_URL);
                    Team currentTeam = new Team();
                    currentTeam.setName(name);
                    currentTeam.setCode(code);
                    currentTeam.setShortName(shortName);
                    currentTeam.setSquadMarketValue(squadMarketValue);
                    currentTeam.setCrestUrl(crestUrl);
                    String crestResourceName = shortName.toLowerCase().replaceAll("\\s", "_");
                    currentTeam.setCrestResource(crestResourceName);
                    teams.add(currentTeam);
                    Team dbTeam = dbHelper.getTeam(i + 1);
                    if (dbTeam == null) {
                        if (dbHelper.insertTeam(currentTeam) == -1)
                            Log.e(TAG, getString(R.string.error_message_unable_to_insert_row_in_db));
                    } else {
                        if (dbHelper.updateTeam(i + 1, currentTeam) == 0)
                            Log.e(TAG, getString(R.string.error_message_unable_to_update_row_in_db));
                    }
                }
                adapter.updateCollection(teams);
                String currentDate = Utils.getSimpleDateFormat().format(new Date());
                Utils.saveStringToSharedPreferences(sharedPreferences, SettingsActivity.KEY_PREF_TEAMS_LAST_UPDATED, currentDate);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, getString(R.string.error_message_no_json_response_received));
        }
    }

    @Override
    public void handleResultsJsonResponse(String response) {

    }

    @Override
    public void handleFixturesJsonResponse(String response) {

    }

    @Override
    public void handleLeagueTableJsonResponse(String response) {

    }

}
