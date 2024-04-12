package com.se309.soccernexus.util.recyclerviews;

public class PostMatchPlayer {
    public String playerName;
    public long playerID;
    public int goals;
    public int assists;

    public PostMatchPlayer(String playerName, long playerID, int goals, int assists) {
        this.playerName = playerName;
        this.playerID = playerID;
        this.goals = goals;
        this.assists = assists;
    }
}
