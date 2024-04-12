package com.example.java11testwithcorrectspringversion.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long player_id;
    @NonNull
    public String email;
    @NonNull
    public String first_name;
    @NonNull
    public String last_name;
    @NonNull
    public String username;
    @NonNull
    public String password;
    @ManyToMany
    @JoinColumn(name = "player_id")
    @JsonIgnore
    public Set<Player> Friends;

    @ManyToMany(mappedBy = "members")
    public Set<Team> teams;
}
