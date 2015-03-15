package com.spstanchev.premierleague.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.spstanchev.premierleague.models.Fixture;
import com.spstanchev.premierleague.models.League;
import com.spstanchev.premierleague.models.LeagueTable;
import com.spstanchev.premierleague.models.Team;

import java.util.ArrayList;

/**
 * Created by Stefan on 1/21/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "PremierLeague.db";

    // Table Names
    private static final String TABLE_LEAGUE = "league";
    private static final String TABLE_TEAMS = "teams";
    private static final String TABLE_RESULTS = "results";
    private static final String TABLE_FIXTURES = "fixtures";
    private static final String TABLE_LEAGUE_TABLE = "leagueTable";

    // Common column names
    private static final String KEY_ID = "id";

    // Table Create Statements
    // League table create statement
    private static final String CREATE_TABLE_LEAGUE = "CREATE TABLE " + TABLE_LEAGUE +
            "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Constants.TAG_CAPTION + " TEXT NOT NULL, " +
            Constants.TAG_LEAGUE + " TEXT NOT NULL, " +
            Constants.TAG_YEAR + " INTEGER NOT NULL, " +
            Constants.TAG_NUMBER_OF_TEAMS + " INTEGER NOT NULL, " +
            Constants.TAG_NUMBER_OF_GAMES + " INTEGER NOT NULL, " +
            Constants.TAG_LAST_UPDATED + " TEXT" + ")";

    // Teams table create statement
    private static final String CREATE_TABLE_TEAMS = "CREATE TABLE " + TABLE_TEAMS +
            "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Constants.TAG_NAME + " TEXT NOT NULL, " +
            Constants.TAG_CODE + " TEXT, " +
            Constants.TAG_SHORT_NAME + " TEXT NOT NULL, " +
            Constants.TAG_SQUAD_MARKET_VALUE + " TEXT, " +
            Constants.TAG_CREST_URL + " TEXT, " +
            Constants.TAG_CREST_RESOURCE + " TEXT" + ")";

    // Fixtures table create statement
    private static final String CREATE_TABLE_RESULTS = "CREATE TABLE " + TABLE_RESULTS +
            "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Constants.TAG_DATE + " TEXT NOT NULL, " +
            Constants.TAG_MATCHDAY + " INTEGER NOT NULL, " +
            Constants.TAG_HOME_TEAM_NAME + " TEXT NOT NULL, " +
            Constants.TAG_AWAY_TEAM_NAME + " TEXT NOT NULL, " +
            Constants.TAG_GOALS_HOME_TEAM + " INTEGER NOT NULL, " +
            Constants.TAG_GOALS_AWAY_TEAM + " INTEGER NOT NULL" + ")";

    // Fixtures table create statement
    private static final String CREATE_TABLE_FIXTURES = "CREATE TABLE " + TABLE_FIXTURES +
            "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Constants.TAG_DATE + " TEXT NOT NULL, " +
            Constants.TAG_MATCHDAY + " INTEGER NOT NULL, " +
            Constants.TAG_HOME_TEAM_NAME + " TEXT NOT NULL, " +
            Constants.TAG_AWAY_TEAM_NAME + " TEXT NOT NULL, " +
            Constants.TAG_GOALS_HOME_TEAM + " INTEGER NOT NULL, " +
            Constants.TAG_GOALS_AWAY_TEAM + " INTEGER NOT NULL" + ")";

    // League table (standings) table create statement
    private static final String CREATE_TABLE_LEAGUE_TABLE = "CREATE TABLE " + TABLE_LEAGUE_TABLE +
            "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Constants.TAG_MATCHDAY + " TEXT NOT NULL, " +
            Constants.TAG_POSITION + " INTEGER NOT NULL, " +
            Constants.TAG_TEAM_NAME + " TEXT NOT NULL, " +
            Constants.TAG_PLAYED_GAMES + " INTEGER NOT NULL, " +
            Constants.TAG_POINTS + " INTEGER NOT NULL, " +
            Constants.TAG_GOALS + " INTEGER NOT NULL, " +
            Constants.TAG_GOALS_AGAINST + " INTEGER NOT NULL, " +
            Constants.TAG_GOAL_DIFFERENCE + " INTEGER NOT NULL" + ")";

    private static DBHelper sInstance;

    public static DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_LEAGUE);
        db.execSQL(CREATE_TABLE_TEAMS);
        db.execSQL(CREATE_TABLE_RESULTS);
        db.execSQL(CREATE_TABLE_FIXTURES);
        db.execSQL(CREATE_TABLE_LEAGUE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(TAG, "Upgrading database from version " + oldVersion +
                " to " + newVersion + ". Old data will be destroyed!");
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAGUE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIXTURES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAGUE_TABLE);

        //crete new tables
        onCreate(db);
    }

    // ------------------------ "league" table methods ----------------//
    /**
     * Inserting new row in table league
     **/
    public long insertLeague (League league) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.TAG_CAPTION, league.getCaption());
        values.put(Constants.TAG_LEAGUE, league.getLeague());
        values.put(Constants.TAG_YEAR, league.getYear());
        values.put(Constants.TAG_NUMBER_OF_TEAMS, league.getNumberOfTeams());
        values.put(Constants.TAG_NUMBER_OF_GAMES, league.getNumberOfGames());
        values.put(Constants.TAG_LAST_UPDATED, league.getLastUpdated());

        //insert row
        long row_id = db.insert(TABLE_LEAGUE, null, values);
        db.close();
        return row_id;
    }

    /**
     * Updating a leagues
     **/
    public int updateLeague (int id, League league) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.TAG_CAPTION, league.getCaption());
        values.put(Constants.TAG_LEAGUE, league.getLeague());
        values.put(Constants.TAG_YEAR, league.getYear());
        values.put(Constants.TAG_NUMBER_OF_TEAMS, league.getNumberOfTeams());
        values.put(Constants.TAG_NUMBER_OF_GAMES, league.getNumberOfGames());
        values.put(Constants.TAG_LAST_UPDATED, league.getLastUpdated());

        //updating a row
        int rows_num = db.update(TABLE_LEAGUE, values, KEY_ID + " = ?",
                new String[] {String.valueOf(id)});
        db.close();
        return rows_num;
    }

    /**
     * Getting single league
     **/
    public League getLeague (int id){
        String selectQuery = "SELECT * FROM " + TABLE_LEAGUE +
                " WHERE " + KEY_ID + " = " + id ;

        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        League league = null;
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            league = new League();
            league.setCaption(c.getString(c.getColumnIndex(Constants.TAG_CAPTION)));
            league.setLeague(c.getString(c.getColumnIndex(Constants.TAG_LEAGUE)));
            league.setYear(c.getString(c.getColumnIndex(Constants.TAG_YEAR)));
            league.setNumberOfTeams(c.getInt(c.getColumnIndex(Constants.TAG_NUMBER_OF_TEAMS)));
            league.setNumberOfGames(c.getInt(c.getColumnIndex(Constants.TAG_NUMBER_OF_GAMES)));
            league.setLastUpdated(c.getString(c.getColumnIndex(Constants.TAG_LAST_UPDATED)));
        }
        c.close();
        db.close();
        return league;

    }

    /**
     * Getting all leagues
     **/
    public ArrayList<League> getAllLeagues (){
        ArrayList<League> leagues = new ArrayList<League>();
        String selectQuery = "SELECT * FROM " + TABLE_LEAGUE;

        Log.d(TAG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                League league = new League();
                league.setCaption(c.getString(c.getColumnIndex(Constants.TAG_CAPTION)));
                league.setLeague(c.getString(c.getColumnIndex(Constants.TAG_LEAGUE)));
                league.setYear(c.getString(c.getColumnIndex(Constants.TAG_YEAR)));
                league.setNumberOfTeams(c.getInt(c.getColumnIndex(Constants.TAG_NUMBER_OF_TEAMS)));
                league.setNumberOfGames(c.getInt(c.getColumnIndex(Constants.TAG_NUMBER_OF_GAMES)));
                league.setLastUpdated(c.getString(c.getColumnIndex(Constants.TAG_LAST_UPDATED)));

                //adding the league to list
                leagues.add(league);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return leagues;
    }

    /**
     * Deleting a league by id
     **/
    public int deleteLeague (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows_num = db.delete(TABLE_LEAGUE, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rows_num;
    }

    // ------------------------ "team" table methods ----------------//
    /**
     * Inserting new row in table team
     **/
    public long insertTeam (Team team) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.TAG_NAME, team.getName());
        values.put(Constants.TAG_CODE, team.getCode());
        values.put(Constants.TAG_SHORT_NAME, team.getShortName());
        values.put(Constants.TAG_SQUAD_MARKET_VALUE, team.getSquadMarketValue());
        values.put(Constants.TAG_CREST_URL, team.getCrestUrl());
        values.put(Constants.TAG_CREST_RESOURCE, team.getCrestResource());

        //insert row
        long row_id = db.insert(TABLE_TEAMS, null, values);
        db.close();
        return row_id;
    }

    /**
     * Updating a team
     **/
    public int updateTeam (int id, Team team) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.TAG_NAME, team.getName());
        values.put(Constants.TAG_CODE, team.getCode());
        values.put(Constants.TAG_SHORT_NAME, team.getShortName());
        values.put(Constants.TAG_SQUAD_MARKET_VALUE, team.getSquadMarketValue());
        values.put(Constants.TAG_CREST_URL, team.getCrestUrl());
        values.put(Constants.TAG_CREST_RESOURCE, team.getCrestResource());

        //updating a row
        int rows_num = db.update(TABLE_TEAMS, values, KEY_ID + " = ?",
                new String[] {String.valueOf(id)});
        db.close();
        return rows_num;
    }

    /**
     * Getting single team
     **/
    public Team getTeam (int id){
        String selectQuery = "SELECT * FROM " + TABLE_TEAMS +
                " WHERE " + KEY_ID + " = " + id;

        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Team team = null;
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            team = new Team();
            team.setName(c.getString(c.getColumnIndex(Constants.TAG_NAME)));
            team.setCode(c.getString(c.getColumnIndex(Constants.TAG_CODE)));
            team.setShortName(c.getString(c.getColumnIndex(Constants.TAG_SHORT_NAME)));
            team.setSquadMarketValue(c.getString(c.getColumnIndex(Constants.TAG_SQUAD_MARKET_VALUE)));
            team.setCrestUrl(c.getString(c.getColumnIndex(Constants.TAG_CREST_URL)));
            team.setCrestResource(c.getString(c.getColumnIndex(Constants.TAG_CREST_RESOURCE)));
        }
        c.close();
        db.close();
        return team;
    }

    /**
     * Getting all teams
     **/
    public ArrayList<Team> getAllTeams (){
        ArrayList<Team> teams = new ArrayList<Team>();
        String selectQuery = "SELECT * FROM " + TABLE_TEAMS;

        Log.d(TAG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Team team = new Team();
                team.setName(c.getString(c.getColumnIndex(Constants.TAG_NAME)));
                team.setCode(c.getString(c.getColumnIndex(Constants.TAG_CODE)));
                team.setShortName(c.getString(c.getColumnIndex(Constants.TAG_SHORT_NAME)));
                team.setSquadMarketValue(c.getString(c.getColumnIndex(Constants.TAG_SQUAD_MARKET_VALUE)));
                team.setCrestUrl(c.getString(c.getColumnIndex(Constants.TAG_CREST_URL)));
                team.setCrestResource(c.getString(c.getColumnIndex(Constants.TAG_CREST_RESOURCE)));

                //adding the team to list
                teams.add(team);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return teams;
    }

    /**
     * Deleting a team
     **/
    public int deleteTeam (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows_num = db.delete(TABLE_TEAMS, KEY_ID + " = ?",
                new String[]{String.valueOf(id)} );
        db.close();
        return rows_num;
    }

    // ------------------------ "results" table methods ----------------//
    /**
     * Inserting new row in table results
     **/
    public long insertResult (Fixture fixture) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.TAG_DATE, fixture.getDate());
        values.put(Constants.TAG_MATCHDAY, fixture.getMatchday());
        values.put(Constants.TAG_HOME_TEAM_NAME, fixture.getHomeTeamName());
        values.put(Constants.TAG_AWAY_TEAM_NAME, fixture.getAwayTeamName());
        values.put(Constants.TAG_GOALS_HOME_TEAM, fixture.getGoalsHomeTeam());
        values.put(Constants.TAG_GOALS_AWAY_TEAM, fixture.getGoalsAwayTeam());

        //insert row
        long row_id = db.insert(TABLE_RESULTS, null, values);
        db.close();
        return row_id;
    }

    /**
     * Getting single result
     **/
    public Fixture getResult (int id){
        String selectQuery = "SELECT * FROM " + TABLE_RESULTS +
                " WHERE " + KEY_ID + " = " + id;

        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Fixture fixture = null;
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            fixture = new Fixture();
            fixture.setDate(c.getString(c.getColumnIndex(Constants.TAG_DATE)));
            fixture.setMatchday(c.getInt(c.getColumnIndex(Constants.TAG_MATCHDAY)));
            fixture.setHomeTeamName(c.getString(c.getColumnIndex(Constants.TAG_HOME_TEAM_NAME)));
            fixture.setAwayTeamName(c.getString(c.getColumnIndex(Constants.TAG_AWAY_TEAM_NAME)));
            fixture.setGoalsHomeTeam(c.getInt(c.getColumnIndex(Constants.TAG_GOALS_HOME_TEAM)));
            fixture.setGoalsAwayTeam(c.getInt(c.getColumnIndex(Constants.TAG_GOALS_AWAY_TEAM)));

        }
        c.close();
        db.close();
        return fixture;
    }

    /**
     * Updating a result
     **/
    public int updateResult (int id, Fixture fixture) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.TAG_DATE, fixture.getDate());
        values.put(Constants.TAG_MATCHDAY, fixture.getMatchday());
        values.put(Constants.TAG_HOME_TEAM_NAME, fixture.getHomeTeamName());
        values.put(Constants.TAG_AWAY_TEAM_NAME, fixture.getAwayTeamName());
        values.put(Constants.TAG_GOALS_HOME_TEAM, fixture.getGoalsHomeTeam());
        values.put(Constants.TAG_GOALS_AWAY_TEAM, fixture.getGoalsAwayTeam());

        //updating a row
        int rows_num = db.update(TABLE_RESULTS, values, KEY_ID + " = ?",
                new String[] {String.valueOf(id)} );
        db.close();
        return rows_num;
    }


    /**
     * Getting all results
     **/
    public ArrayList<Fixture> getAllResults (){
        ArrayList<Fixture> fixtures = new ArrayList<Fixture>();
        String selectQuery = "SELECT * FROM " + TABLE_RESULTS;

        Log.d(TAG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Fixture fixture = new Fixture();
                fixture.setDate(c.getString(c.getColumnIndex(Constants.TAG_DATE)));
                fixture.setMatchday(c.getInt(c.getColumnIndex(Constants.TAG_MATCHDAY)));
                fixture.setHomeTeamName(c.getString(c.getColumnIndex(Constants.TAG_HOME_TEAM_NAME)));
                fixture.setAwayTeamName(c.getString(c.getColumnIndex(Constants.TAG_AWAY_TEAM_NAME)));
                fixture.setGoalsHomeTeam(c.getInt(c.getColumnIndex(Constants.TAG_GOALS_HOME_TEAM)));
                fixture.setGoalsAwayTeam(c.getInt(c.getColumnIndex(Constants.TAG_GOALS_AWAY_TEAM)));

                //adding the fixture to list
                fixtures.add(fixture);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return fixtures;
    }

    /**
     * Deleting a result
     **/
    public int deleteResult (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows_num = db.delete(TABLE_RESULTS, KEY_ID + " = ?",
                new String[]{String.valueOf(id)} );
        db.close();
        return rows_num;
    }

    // ------------------------ "fixtures" table methods ----------------//
    /**
     * Inserting new row in table fixtures
     **/
    public long insertFixture (Fixture fixture) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.TAG_DATE, fixture.getDate());
        values.put(Constants.TAG_MATCHDAY, fixture.getMatchday());
        values.put(Constants.TAG_HOME_TEAM_NAME, fixture.getHomeTeamName());
        values.put(Constants.TAG_AWAY_TEAM_NAME, fixture.getAwayTeamName());
        values.put(Constants.TAG_GOALS_HOME_TEAM, fixture.getGoalsHomeTeam());
        values.put(Constants.TAG_GOALS_AWAY_TEAM, fixture.getGoalsAwayTeam());

        //insert row
        long row_id = db.insert(TABLE_FIXTURES, null, values);
        db.close();
        return row_id;
    }

    /**
     * Getting single fixture
     **/
    public Fixture getFixture (int id){
        String selectQuery = "SELECT * FROM " + TABLE_FIXTURES +
                " WHERE " + KEY_ID + " = " + id;

        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Fixture fixture = null;
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            fixture = new Fixture();
            fixture.setDate(c.getString(c.getColumnIndex(Constants.TAG_DATE)));
            fixture.setMatchday(c.getInt(c.getColumnIndex(Constants.TAG_MATCHDAY)));
            fixture.setHomeTeamName(c.getString(c.getColumnIndex(Constants.TAG_HOME_TEAM_NAME)));
            fixture.setAwayTeamName(c.getString(c.getColumnIndex(Constants.TAG_AWAY_TEAM_NAME)));
            fixture.setGoalsHomeTeam(c.getInt(c.getColumnIndex(Constants.TAG_GOALS_HOME_TEAM)));
            fixture.setGoalsAwayTeam(c.getInt(c.getColumnIndex(Constants.TAG_GOALS_AWAY_TEAM)));
        }
        c.close();
        db.close();
        return fixture;
    }

    /**
     * Updating a fixture
     **/
    public int updateFixture (int id, Fixture fixture) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.TAG_DATE, fixture.getDate());
        values.put(Constants.TAG_MATCHDAY, fixture.getMatchday());
        values.put(Constants.TAG_HOME_TEAM_NAME, fixture.getHomeTeamName());
        values.put(Constants.TAG_AWAY_TEAM_NAME, fixture.getAwayTeamName());
        values.put(Constants.TAG_GOALS_HOME_TEAM, fixture.getGoalsHomeTeam());
        values.put(Constants.TAG_GOALS_AWAY_TEAM, fixture.getGoalsAwayTeam());

        //updating a row
        int rows_num = db.update(TABLE_FIXTURES, values, KEY_ID + " = ?",
                new String[] {String.valueOf(id)} );
        db.close();
        return rows_num;
    }


    /**
     * Getting all fixtures
     **/
    public ArrayList<Fixture> getAllFixtures (){
        ArrayList<Fixture> fixtures = new ArrayList<Fixture>();
        String selectQuery = "SELECT * FROM " + TABLE_FIXTURES;

        Log.d(TAG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Fixture fixture = new Fixture();
                fixture.setDate(c.getString(c.getColumnIndex(Constants.TAG_DATE)));
                fixture.setMatchday(c.getInt(c.getColumnIndex(Constants.TAG_MATCHDAY)));
                fixture.setHomeTeamName(c.getString(c.getColumnIndex(Constants.TAG_HOME_TEAM_NAME)));
                fixture.setAwayTeamName(c.getString(c.getColumnIndex(Constants.TAG_AWAY_TEAM_NAME)));
                fixture.setGoalsHomeTeam(c.getInt(c.getColumnIndex(Constants.TAG_GOALS_HOME_TEAM)));
                fixture.setGoalsAwayTeam(c.getInt(c.getColumnIndex(Constants.TAG_GOALS_AWAY_TEAM)));

                //adding the fixture to list
                fixtures.add(fixture);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return fixtures;
    }

    /**
     * Deleting a fixture
     **/
    public int deleteFixture (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows_num = db.delete(TABLE_FIXTURES, KEY_ID + " = ?",
                new String[]{String.valueOf(id)} );
        db.close();
        return rows_num;
    }

    // ------------------------ "league" table methods ----------------//
    /**
     * Inserting new row in table leagueTable
     **/
    public long insertLeagueTable (LeagueTable leagueTable) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.TAG_MATCHDAY, leagueTable.getMatchday());
        values.put(Constants.TAG_POSITION, leagueTable.getPosition());
        values.put(Constants.TAG_TEAM_NAME, leagueTable.getTeamName());
        values.put(Constants.TAG_PLAYED_GAMES, leagueTable.getPlayedGames());
        values.put(Constants.TAG_POINTS, leagueTable.getPoints());
        values.put(Constants.TAG_GOALS, leagueTable.getGoals());
        values.put(Constants.TAG_GOALS_AGAINST, leagueTable.getGoalsAgainst());
        values.put(Constants.TAG_GOAL_DIFFERENCE, leagueTable.getGoalDifference());

        //insert row
        long row_id = db.insert(TABLE_LEAGUE_TABLE, null, values);
        db.close();
        return row_id;
    }

    /**
     * Updating a record in the leagueTable Table
     **/
    public int updateLeagueTable (int id, LeagueTable leagueTable) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.TAG_MATCHDAY, leagueTable.getMatchday());
        values.put(Constants.TAG_POSITION, leagueTable.getPosition());
        values.put(Constants.TAG_TEAM_NAME, leagueTable.getTeamName());
        values.put(Constants.TAG_PLAYED_GAMES, leagueTable.getPlayedGames());
        values.put(Constants.TAG_POINTS, leagueTable.getPoints());
        values.put(Constants.TAG_GOALS, leagueTable.getGoals());
        values.put(Constants.TAG_GOALS_AGAINST, leagueTable.getGoalsAgainst());
        values.put(Constants.TAG_GOAL_DIFFERENCE, leagueTable.getGoalDifference());

        //updating a row
        int rows_num = db.update(TABLE_LEAGUE_TABLE, values, KEY_ID + " = ?",
                new String[] {String.valueOf(id)});

        db.close();
        return rows_num;
    }

    /**
     * Getting single record from LeagueTable
     **/
    public LeagueTable getTeamFromLeagueTable(int id){
        String selectQuery = "SELECT * FROM " + TABLE_LEAGUE_TABLE +
                " WHERE " + KEY_ID + " = " + id;

        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        LeagueTable leagueTable = null;
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            leagueTable = new LeagueTable();
            leagueTable.setMatchday(c.getInt(c.getColumnIndex(Constants.TAG_MATCHDAY)));
            leagueTable.setPosition(c.getInt(c.getColumnIndex(Constants.TAG_POSITION)));
            leagueTable.setTeamName(c.getString(c.getColumnIndex(Constants.TAG_TEAM_NAME)));
            leagueTable.setPlayedGames(c.getInt(c.getColumnIndex(Constants.TAG_PLAYED_GAMES)));
            leagueTable.setPoints(c.getInt(c.getColumnIndex(Constants.TAG_POINTS)));
            leagueTable.setGoals(c.getInt(c.getColumnIndex(Constants.TAG_GOALS)));
            leagueTable.setGoalsAgainst(c.getInt(c.getColumnIndex(Constants.TAG_GOALS_AGAINST)));
            leagueTable.setGoalDifference(c.getInt(c.getColumnIndex(Constants.TAG_GOAL_DIFFERENCE)));

        }
        c.close();
        db.close();
        return leagueTable;
    }

    /**
     * Getting the full league table
     **/
    public ArrayList<LeagueTable> getLeagueTable (){
        ArrayList<LeagueTable> leagueTableArrayList = new ArrayList<LeagueTable>();
        String selectQuery = "SELECT * FROM " + TABLE_LEAGUE_TABLE;

        Log.d(TAG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                LeagueTable leagueTable = new LeagueTable();
                leagueTable.setMatchday(c.getInt(c.getColumnIndex(Constants.TAG_MATCHDAY)));
                leagueTable.setPosition(c.getInt(c.getColumnIndex(Constants.TAG_POSITION)));
                leagueTable.setTeamName(c.getString(c.getColumnIndex(Constants.TAG_TEAM_NAME)));
                leagueTable.setPlayedGames(c.getInt(c.getColumnIndex(Constants.TAG_PLAYED_GAMES)));
                leagueTable.setPoints(c.getInt(c.getColumnIndex(Constants.TAG_POINTS)));
                leagueTable.setGoals(c.getInt(c.getColumnIndex(Constants.TAG_GOALS)));
                leagueTable.setGoalsAgainst(c.getInt(c.getColumnIndex(Constants.TAG_GOALS_AGAINST)));
                leagueTable.setGoalDifference(c.getInt(c.getColumnIndex(Constants.TAG_GOAL_DIFFERENCE)));
                //adding the league to list
                leagueTableArrayList.add(leagueTable);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return leagueTableArrayList;
    }

    /**
     * Deleting a record from the leagueTable
     **/
    public int deleteLeagueTable (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows_num = db.delete(TABLE_LEAGUE_TABLE, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rows_num;
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
