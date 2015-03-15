package com.spstanchev.premierleague.models;

/**
 * Created by Stefan on 1/18/2015.
 */
public class League {

    private String caption;
    private String league;
    private String year;
    private Integer numberOfTeams;
    private Integer numberOfGames;
    private String lastUpdated;

    /**
     *
     * @return
     * The caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     *
     * @param caption
     * The caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     *
     * @return
     * The league
     */
    public String getLeague() {
        return league;
    }

    /**
     *
     * @param league
     * The league
     */
    public void setLeague(String league) {
        this.league = league;
    }

    /**
     *
     * @return
     * The year
     */
    public String getYear() {
        return year;
    }

    /**
     *
     * @param year
     * The year
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     *
     * @return
     * The numberOfTeams
     */
    public Integer getNumberOfTeams() {
        return numberOfTeams;
    }

    /**
     *
     * @param numberOfTeams
     * The numberOfTeams
     */
    public void setNumberOfTeams(Integer numberOfTeams) {
        this.numberOfTeams = numberOfTeams;
    }

    /**
     *
     * @return
     * The numberOfGames
     */
    public Integer getNumberOfGames() {
        return numberOfGames;
    }

    /**
     *
     * @param numberOfGames
     * The numberOfGames
     */
    public void setNumberOfGames(Integer numberOfGames) {
        this.numberOfGames = numberOfGames;
    }


    /**
     *
     * @return
     * The lastUpdated
     */
    public String getLastUpdated() {
        return lastUpdated;
    }

    /**
     *
     * @param lastUpdated
     * The lastUpdated
     */
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}