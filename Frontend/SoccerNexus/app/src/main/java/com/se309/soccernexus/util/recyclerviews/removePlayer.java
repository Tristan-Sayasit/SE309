package com.se309.soccernexus.util.recyclerviews;

import android.widget.ImageButton;

/**
 * @author Tristan Sayasit
 * Class for storing a Player object
 */
public class removePlayer {
    private String name;
    private String position;
    public long player_id;
    public long team_id;

    /**
     * Creates a new Player.
     * @param name name of the player
     */
    public removePlayer(String name, String position, long player_id, long team_id) {
        this.name = name;
        this.position = position;
        this.player_id = player_id;
        this.team_id = team_id;
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
}
