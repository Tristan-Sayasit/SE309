package com.example.soccernexusserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pickup_matches")
public class PickupMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long pickup_match_id;
    public String location;
    public LocalDateTime time_of_match;
    @ManyToOne
    @JoinColumn(name = "challenging_team_id", foreignKey = @ForeignKey(name = "challenging_team_fk"))
    @JsonIgnoreProperties(value = {"members"})
    public Team challenging_team;

    @ManyToOne
    @JoinColumn(name = "accepting_team_id", foreignKey = @ForeignKey(name = "accepting_team_fk"))
    @JsonIgnoreProperties(value = {"members"})
    public Team accepting_team;

    public String match_format;
    public boolean is_finished;
    public int challenging_team_goals;
    public int accepting_team_goals;
    public boolean is_accepted;
    public boolean c_team_report_done;
    public boolean a_team_report_done;
}
