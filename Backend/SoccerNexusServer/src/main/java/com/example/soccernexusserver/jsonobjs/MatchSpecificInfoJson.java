package com.example.soccernexusserver.jsonobjs;

import java.time.LocalDateTime;

public class MatchSpecificInfoJson {
    public long match_id;
    public SimpleTeamJson challenging_team;
    public LocalDateTime match_time;
    public String format;
    public String location;
    public SimpleTeamJson accepting_team;
    public int challenging_team_goals;
    public int accepting_team_goals;
    public boolean is_finished;
}
