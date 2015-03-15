package com.spstanchev.premierleague.common;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by Stefan on 1/18/2015.
 */
/**
 * Async task class to get json by making HTTP call
 * */
public class DownloadJsonData extends AsyncTask<String, Void, ArrayList<String>> {
    AsyncJsonResponse response;

    public DownloadJsonData(AsyncJsonResponse response){
        this.response = response;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        HttpHandler httpHandler = new HttpHandler();
        String jsonString = httpHandler.makeHttpCall(params[0], HttpHandler.GET, null);
        ArrayList<String> result = new ArrayList<String>();
        result.add(params[0]);
        result.add(jsonString);
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<String> s) {
        super.onPostExecute(s);
        String url = s.get(0);
        if (url.contains(Constants.JSON_LEAGUE_TABLE_URL)){
            response.handleLeagueTableJsonResponse(s.get(1));
        } else if (url.contains(Constants.JSON_FIXTURES_URL)){
            response.handleFixturesJsonResponse(s.get(1));
        } else if (url.contains(Constants.JSON_RESULTS_URL)){
            response.handleResultsJsonResponse(s.get(1));
        } else if (url.contains(Constants.JSON_TEAMS_URL)){
            response.handleTeamsJsonResponse(s.get(1));
        } else if (url.contains(Constants.JSON_LEAGUE_URL)){
            response.handleLeagueJsonResponse(s.get(1));
        }

    }
}