package com.example.soccernexusserver.jparepos;

import com.example.soccernexusserver.entities.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepo extends CrudRepository<Message, Long> {
    @Query("select m from Message m where " +
            "(m.sending_player.player_id = ?1 and m.receiving_player.player_id = ?2) or " +
            "(m.sending_player.player_id = ?2 and m.receiving_player.player_id = ?1) order by m.time_of_message asc")
    Iterable<Message> getMessageHistory(long signed_in_id, long friend_id);
}
