package com.example.soccernexusserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "Notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long notification_id;
    public String notification_type;
    @ManyToOne
    @JoinColumn(name = "notif_sp_id", foreignKey = @ForeignKey(name = "notif_sp_fk"))
    @JsonIgnoreProperties(value = {"sent_messages", "received_messages", "email", "password", "height", "weight", "birthday", "preferred_position", "goals", "assists", "redCards", "yellowCards", "gamesPlayed", "goalsPerGame", "assistPerGame", "has_profile", "profile_pic", "teams", "notifications"})
    public Player notif_sending_player;

    @ManyToOne
    @JoinColumn(name = "notif_st_id", foreignKey = @ForeignKey(name = "notif_st_fk"))
    @JsonIgnoreProperties(value = {"members", "captain", "location"})
    public Team notif_sending_team;

    public String notification_message;

    @ManyToOne
    @JoinColumn(name = "notif_rp_id", foreignKey = @ForeignKey(name = "notif_rp_fk"))
    @JsonIgnoreProperties(value = {"sent_messages", "received_messages", "email", "password", "height", "weight", "birthday", "preferred_position", "goals", "assists", "redCards", "yellowCards", "gamesPlayed", "goalsPerGame", "assistPerGame", "has_profile", "profile_pic", "teams", "notifications"})
    public Player notif_receiving_player;

    public boolean is_read;
}
