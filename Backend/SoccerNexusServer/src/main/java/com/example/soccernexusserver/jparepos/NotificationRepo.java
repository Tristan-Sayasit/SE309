package com.example.soccernexusserver.jparepos;

import com.example.soccernexusserver.entities.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepo extends CrudRepository<Notification, Long> {
    @Query("select n from Notification n where n.notif_sending_player.player_id = ?1 and n.notif_receiving_player.player_id = ?2 and n.notification_type = 'friend request' and n.is_read = false")
    Optional<Notification> findFriendReq(long sending_id, long user_id);
}
