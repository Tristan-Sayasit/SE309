package com.se309.soccernexus.util.recyclerviews;

/**
 * @author Tristan Sayasit
 * Class for storing a Player object
 */
public class Player {
    private String name;
    private String position;
    private boolean isCaptain;

    /**
     * Creates a new Player.
     * @param name name of the player
     * @param position position of the player
     * @param isCaptain whether or not the player is a captain
     */
    public Player(String name, String position, boolean isCaptain) {
        this.name = name;
        this.position = position;
        this.isCaptain = isCaptain;
    }

    /**
     * Gets the name of the player
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the player
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the player's position
     * @return player position
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the player's position
     * @param position
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Returns whether or not the player is a captain
     * @return isCaptain
     */
    public boolean isCaptain() {
        return isCaptain;
    }

    /**
     * Sets whether or not the player is a captain.
     * @param captain
     */
    public void setCaptain(boolean captain) {
        isCaptain = captain;
    }
}
