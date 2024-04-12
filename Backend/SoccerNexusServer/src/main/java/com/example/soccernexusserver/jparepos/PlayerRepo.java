package com.example.soccernexusserver.jparepos;

import com.example.soccernexusserver.entities.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepo extends CrudRepository<Player, Long> {
    @Query("select p from Player p where p.preferred_position = ?1")
    Iterable<Player> getAllOfPosition(String position);

    @Query("select p from Player p where p.username = ?1 and p.password = ?2")
    Optional<Player> login(String username, String password);

    @Query("SELECT p FROM Player p WHERE p.username LIKE %:username%")
    Iterable<Player> findAllByUsernameContains(@Param("username") String username);

    @Query("SELECT p FROM Player p WHERE p.preferred_position = :preferred_position")
    Iterable<Player> findAllByPreferredPosition(@Param("preferred_position") String preferred_position);

    @Query("select p from Player p where p.username = ?1")
    Optional<Player> getPlayerByUsername(String username);


}
