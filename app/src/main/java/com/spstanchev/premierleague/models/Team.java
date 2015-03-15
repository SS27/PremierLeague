package com.spstanchev.premierleague.models;

/**
 * Created by Stefan on 1/18/2015.
 */
public class Team {

    private String name;
    private String code;
    private String shortName;
    private String squadMarketValue;
    private String crestUrl;
    private String crestResource;

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The code
     */
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code
     * The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     *
     * @return
     * The shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     *
     * @param shortName
     * The shortName
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     *
     * @return
     * The squadMarketValue
     */
    public String getSquadMarketValue() {
        return squadMarketValue;
    }

    /**
     *
     * @param squadMarketValue
     * The squadMarketValue
     */
    public void setSquadMarketValue(String squadMarketValue) {
        this.squadMarketValue = squadMarketValue;
    }

    /**
     *
     * @return
     * The crestUrl
     */
    public String getCrestUrl() {
        return crestUrl;
    }

    /**
     *
     * @param crestUrl
     * The crestUrl
     */
    public void setCrestUrl(String crestUrl) {
        this.crestUrl = crestUrl;
    }

    public String getCrestResource() {
        return crestResource;
    }

    public void setCrestResource(String crestResource) {
        this.crestResource = crestResource;
    }

}