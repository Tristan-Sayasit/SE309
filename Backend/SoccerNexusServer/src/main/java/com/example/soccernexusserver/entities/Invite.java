package com.example.soccernexusserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "Invites")
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long invite_id;

    @ManyToOne
    @JoinColumn(name = "inviting_team_id", foreignKey = @ForeignKey(name = "inviting_team_fk"))
    @JsonIgnoreProperties(value = {"members", "captain", "location"})
    public Team inviting_team;

    @ManyToOne
    @JoinColumn(name = "invited_player_id", foreignKey = @ForeignKey(name = "invited_player_fk"))

    @JsonIgnoreProperties(value = {"sent_messages", "received_messages", "email", "password", "height", "weight", "birthday", "preferred_position", "goals", "assists", "redCards", "yellowCards", "gamesPlayed", "goalsPerGame", "assistPerGame", "has_profile", "profile_pic", "friends", "teams", "notifications"})
    public Player invited_player;
}
