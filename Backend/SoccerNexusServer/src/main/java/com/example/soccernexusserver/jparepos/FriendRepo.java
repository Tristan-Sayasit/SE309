package com.example.soccernexusserver.jparepos;

import com.example.soccernexusserver.entities.Friend;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepo extends CrudRepository<Friend, Long> {
    @Query("select f from Friend f where (f.player_id_1 = ?1 or f.player_id_2 = ?1) and f.accepted = true")
    List<Friend> getFriendsList(long player_id);
    @Query("select f from Friend f where (f.player_id_1 = ?1 and f.player_id_2 = ?2) or (f.player_id_1 = ?2 and f.player_id_2 = ?1)")
    Optional<Friend> findSpecificFriend(long id1, long id2);
    @Query("select f from Friend f where f.player_id_2 = ?1 and f.accepted = false")
    List<Friend> getReceivedFriendRequests(long player_id);

    @Query("select f from Friend f where f.player_id_1 = ?1 and f.accepted = false")
    List<Friend> getSentFriendRequests(long player_id);

}
