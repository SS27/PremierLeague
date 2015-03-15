package com.spstanchev.premierleague.models;

/**
 * Created by Stefan on 1/18/2015.
 */
public class LeagueTable {
    private Integer matchday;
    private Integer position;
    private String teamName;
    private Integer playedGames;
    private Integer points;
    private Integer goals;
    private Integer goalsAgainst;
    private Integer goalDifference;

    public Integer getMatchday() {
        return matchday;
    }

    public void setMatchday(Integer matchday) {
        this.matchday = matchday;
    }
    /**
     *
     * @return
     * The position
     */
    public Integer getPosition() {
        return position;
    }

    /**
     *
     * @param position
     * The position
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     *
     * @return
     * The teamName
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     *
     * @param teamName
     * The teamName
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     *
     * @return
     * The playedGames
     */
    public Integer getPlayedGames() {
        return playedGames;
    }

    /**
     *
     * @param playedGames
     * The playedGames
     */
    public void setPlayedGames(Integer playedGames) {
        this.playedGames = playedGames;
    }

    /**
     *
     * @return
     * The points
     */
    public Integer getPoints() {
        return points;
    }

    /**
     *
     * @param points
     * The points
     */
    public void setPoints(Integer points) {
        this.points = points;
    }

    /**
     *
     * @return
     * The goals
     */
    public Integer getGoals() {
        return goals;
    }

    /**
     *
     * @param goals
     * The goals
     */
    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    /**
     *
     * @return
     * The goalsAgainst
     */
    public Integer getGoalsAgainst() {
        return goalsAgainst;
    }

    /**
     *
     * @param goalsAgainst
     * The goalsAgainst
     */
    public void setGoalsAgainst(Integer goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    /**
     *
     * @return
     * The goalDifference
     */
    public Integer getGoalDifference() {
        return goalDifference;
    }

    /**
     *
     * @param goalDifference
     * The goalDifference
     */
    public void setGoalDifference(Integer goalDifference) {
        this.goalDifference = goalDifference;
    }
}

