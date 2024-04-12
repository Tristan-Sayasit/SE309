package com.example.soccernexusserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long team_id;
    @OneToOne
    @JsonIgnoreProperties(value = {"password", "teams", "profile_pic", "sent_messages", "received_messages", "notifications"})
    @JoinColumn(referencedColumnName = "player_id", foreignKey = @ForeignKey(name = "captain_fk"))
    public Player captain;

    @ManyToMany
    @JoinTable(name = "team_members", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "player_id"), foreignKey = @ForeignKey(name = "member_team_fk"), inverseForeignKey = @ForeignKey(name = "member_player_fk"))
    @JsonIgnoreProperties(value = {"profile_pic", "teams", "password", "sent_messages", "received_messages", "notifications"})
    public List<Player> members;
    @NonNull
    public String team_name;
    @NonNull
    public String location;

}
