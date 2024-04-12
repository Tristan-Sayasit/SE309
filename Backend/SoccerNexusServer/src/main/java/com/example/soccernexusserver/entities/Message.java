package com.example.soccernexusserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long message_id;
    public String message_text;
    @ManyToOne
    @JoinColumn(name = "sending_player_id", foreignKey = @ForeignKey(name = "sending_player_fk"))
    @JsonIgnoreProperties(value = {"sent_messages", "received_messages", "email", "password", "height",
            "weight", "birthday", "preferred_position", "goals", "assists", "redCards",
            "yellowCards", "gamesPlayed", "goalsPerGame", "assistPerGame", "has_profile",
            "profile_pic", "friends", "teams"})
    public Player sending_player;
    @ManyToOne
    @JoinColumn(name = "receiving_player_id", foreignKey = @ForeignKey(name = "receiving_player_fk"))
    @JsonIgnoreProperties(value = {"sent_messages", "received_messages", "email", "password", "height",
            "weight", "birthday", "preferred_position", "goals", "assists", "redCards",
            "yellowCards", "gamesPlayed", "goalsPerGame", "assistPerGame", "has_profile",
            "profile_pic", "friends", "teams"})
    public Player receiving_player;
    public Boolean is_read;
    public LocalDate time_of_message;
}
