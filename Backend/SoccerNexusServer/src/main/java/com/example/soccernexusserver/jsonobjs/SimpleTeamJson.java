package com.example.soccernexusserver.jsonobjs;


import java.util.List;

public class SimpleTeamJson {
    public long team_id;
    public String team_name;
    public SimplePlayerJson captain;
    public List<SimplePlayerJson> players;
}
