package com.se309.soccernexus.util.recyclerviews;

/**
 * @author Tristan Sayasit
 * Class for storing a Team object
 */
public class Team {
    private String name, location;
    private int playerCount;
    private long id;
    private int btnToShow; //0 for enter, 1 for join, 2 for challenge

    /**
     * Creates a new Team
     * @param name name of the team
     * @param id id of the team
     * @param location the team's location
     * @param playerCount how many players are on the team
     * @param btnToShow decides what button to show in the recycler view. 0 for enter, 1 for join,
     *                  2 for challenge
     */
    public Team(String name, long id, String location, int playerCount, int btnToShow) {
        this.name = name;
        this.id = id;
        this.location = location;
        this.playerCount = playerCount;
        this.btnToShow = btnToShow;
    }

    /**
     * Gets the name of the team
     * @return team name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the team
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the id of the team
     * @return team id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id of the team
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the team's location
     * @return team location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the team's location
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the team's player count
     * @return player count
     */
    public int getPlayerCount() {
        return playerCount;
    }

    /**
     * Sets the team's player count
     * @param playerCount
     */
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    /**
     * Gets the value for what button to show
     * @return btnToShow value
     */
    public int getBtnToShow() {
        return btnToShow;
    }

    /**
     * Sets the value for what button to show
     * @param btnToShow
     */
    public void setBtnToShow(int btnToShow) {
        this.btnToShow = btnToShow;
    }
}
