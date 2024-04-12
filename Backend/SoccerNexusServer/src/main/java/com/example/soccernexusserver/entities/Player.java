package com.example.soccernexusserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.lang.NonNull;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long player_id;
    @NonNull
    @NotBlank
    public String email;
    @NonNull
    @NotBlank
    public String password;
    @NonNull
    @NotBlank
    @Column(unique=true, length = 25)
    public String username;
    public String first_name;
    public String last_name;
    public int height;
    public float weight;
    public LocalDate birthday;
    public String preferred_position;
    public int goals;
    public int assists;
    public int redCards;
    public int yellowCards;
    public int gamesPlayed;
    public float goalsPerGame;
    public float assistPerGame;
    public boolean has_profile;
    @Lob
    public String profile_pic;

    @ManyToMany(mappedBy = "members")
    public List<Team> teams;

    @OneToMany(mappedBy = "sending_player")
    @JsonIgnore
    public List<Message> sent_messages;

    @OneToMany(mappedBy = "receiving_player")
    @JsonIgnore
    public List<Message> received_messages;

    @OneToMany(mappedBy = "notif_receiving_player")
    public List<Notification> notifications;
}
