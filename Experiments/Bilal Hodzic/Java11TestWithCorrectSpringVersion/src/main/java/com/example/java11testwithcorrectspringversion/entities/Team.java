package com.example.java11testwithcorrectspringversion.entities;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long team_id;
    @NonNull
    public String team_name;

    @ManyToMany
    @JoinTable(
            name = "Team_members",
            joinColumns = @JoinColumn(referencedColumnName = "team_id"),
            inverseJoinColumns = @JoinColumn(referencedColumnName = "player_id")
    )
    public Set<Player> members;
}
