package com.example.soccernexusserver.entities;

import javax.persistence.*;

@Entity
@Table(name = "player_friends")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long friend_id;

    public long player_id_1;
    public long player_id_2;
    public boolean accepted;

}
