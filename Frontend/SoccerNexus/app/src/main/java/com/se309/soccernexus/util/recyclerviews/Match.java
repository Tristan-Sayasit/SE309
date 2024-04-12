package com.se309.soccernexus.util.recyclerviews;

import java.time.LocalDateTime;

public class Match {

    public long id;
    public long teamID;
    public String teamName;
    public String location;
    public LocalDateTime date;

    public Match(String teamName, String location, LocalDateTime date, long id, long teamID) {
        this.teamName = teamName;
        this.location = location;
        this.date = date;
        this.id = id;
        this.teamID = teamID;
    }
    public Match(String teamName, String location, LocalDateTime date, long id) {
        this.teamName = teamName;
        this.location = location;
        this.date = date;
        this.id = id;
        this.teamID = 0;
    }
}
