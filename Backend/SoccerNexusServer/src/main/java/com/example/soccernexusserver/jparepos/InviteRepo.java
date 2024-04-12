package com.example.soccernexusserver.jparepos;

import com.example.soccernexusserver.entities.Invite;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteRepo extends CrudRepository<Invite,Long> {
    @Query("select i from Invite i where i.invited_player.player_id = ?1")
    Iterable<Invite> findAllByPlayerId(Long playerId);
}
