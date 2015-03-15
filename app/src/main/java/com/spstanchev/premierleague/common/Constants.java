package com.spstanchev.premierleague.common;

/**
 * Created by Stefan on 1/18/2015.
 */
public interface Constants {
    public static final String JSON_LEAGUE_URL = "http://api.football-data.org/alpha/soccerseasons/354";
    public static final String JSON_TEAMS_URL = JSON_LEAGUE_URL + "/teams";
    public static final String JSON_FIXTURES_URL = JSON_LEAGUE_URL + "/fixtures?timeFrame=n";
    public static final String JSON_RESULTS_URL = JSON_LEAGUE_URL + "/fixtures?timeFrame=p";
    public static final String JSON_LEAGUE_TABLE_URL = JSON_LEAGUE_URL + "/leagueTable";

    //LEAGUE TAGS
    public static final String TAG_CAPTION = "caption";
    public static final String TAG_LEAGUE = "league";
    public static final String TAG_YEAR = "year";
    public static final String TAG_NUMBER_OF_TEAMS = "numberOfTeams";
    public static final String TAG_NUMBER_OF_GAMES = "numberOfGames";
    public static final String TAG_LAST_UPDATED = "lastUpdated";

    //TEAMS TAGS
    public static final String TAG_TEAMS = "teams";
    public static final String TAG_NAME = "name";
    public static final String TAG_CODE = "code";
    public static final String TAG_SHORT_NAME = "shortName";
    public static final String TAG_SQUAD_MARKET_VALUE = "squadMarketValue";
    public static final String TAG_CREST_URL = "crestUrl";
    public static final String TAG_CREST_RESOURCE = "crestResource";

    //FIXTURES TAGS
    public static final String TAG_FIXTURES = "fixtures";
    public static final String TAG_DATE = "date";
    public static final String TAG_MATCHDAY = "matchday";
    public static final String TAG_HOME_TEAM_NAME = "homeTeamName";
    public static final String TAG_AWAY_TEAM_NAME = "awayTeamName";
    public static final String TAG_RESULT = "result";
    public static final String TAG_GOALS_HOME_TEAM = "goalsHomeTeam";
    public static final String TAG_GOALS_AWAY_TEAM = "goalsAwayTeam";

    //LEAGUE TABLE TAGS
    public static final String TAG_STANDING = "standing";
    public static final String TAG_POSITION = "position";
    public static final String TAG_TEAM_NAME = "teamName";
    public static final String TAG_PLAYED_GAMES = "playedGames";
    public static final String TAG_POINTS = "points";
    public static final String TAG_GOALS = "goals";
    public static final String TAG_GOALS_AGAINST = "goalsAgainst";
    public static final String TAG_GOAL_DIFFERENCE = "goalDifference";

    public static final String CREST_IMAGE_DIR = "crests";


    public static final String TAG_RESULTS = "results";
    public static final String TAG_STANDINGS = "standings";
}
