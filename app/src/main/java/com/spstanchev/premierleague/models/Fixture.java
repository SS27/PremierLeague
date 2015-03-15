package com.spstanchev.premierleague.models;

/**
 * Created by Stefan on 1/18/2015.
 */
public class Fixture {

    private String date;
    private Integer matchday;
    private String homeTeamName;
    private String awayTeamName;
    private Integer goalsHomeTeam;
    private Integer goalsAwayTeam;

    /**
     *
     * @return
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The matchday
     */
    public Integer getMatchday() {
        return matchday;
    }

    /**
     *
     * @param matchday
     * The matchday
     */
    public void setMatchday(Integer matchday) {
        this.matchday = matchday;
    }

    /**
     *
     * @return
     * The homeTeamName
     */
    public String getHomeTeamName() {
        return homeTeamName;
    }

    /**
     *
     * @param homeTeamName
     * The homeTeamName
     */
    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    /**
     *
     * @return
     * The awayTeamName
     */
    public String getAwayTeamName() {
        return awayTeamName;
    }

    /**
     *
     * @param awayTeamName
     * The awayTeamName
     */
    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    /**
     *
     * @return
     * The goalsHomeTeam
     */
    public Integer getGoalsHomeTeam() {
        return goalsHomeTeam;
    }

    /**
     *
     * @param goalsHomeTeam
     * The goalsHomeTeam
     */
    public void setGoalsHomeTeam(Integer goalsHomeTeam) {
        this.goalsHomeTeam = goalsHomeTeam;
    }

    /**
     *
     * @return
     * The goalsAwayTeam
     */
    public Integer getGoalsAwayTeam() {
        return goalsAwayTeam;
    }

    /**
     *
     * @param goalsAwayTeam
     * The goalsAwayTeam
     */
    public void setGoalsAwayTeam(Integer goalsAwayTeam) {
        this.goalsAwayTeam = goalsAwayTeam;
    }
}