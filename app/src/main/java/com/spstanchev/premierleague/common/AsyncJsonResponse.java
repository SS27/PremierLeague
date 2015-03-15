package com.spstanchev.premierleague.common;

/**
 * Created by Stefan on 1/21/2015.
 */
public interface AsyncJsonResponse {
    public void handleLeagueJsonResponse (String response);
    public void handleTeamsJsonResponse (String response);
    public void handleResultsJsonResponse (String response);
    public void handleFixturesJsonResponse (String response);
    public void handleLeagueTableJsonResponse (String response);
}
