package com.example.soccernexusserver.jparepos;

import com.example.soccernexusserver.entities.PickupMatch;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PickupMatchRepo extends CrudRepository<PickupMatch, Long> {

    @Query("select p from PickupMatch p where (p.accepting_team.team_id = ?1 or p.challenging_team.team_id = ?1) and p.is_accepted = true order by p.time_of_match")
    Iterable<PickupMatch> getMatchHistory(long team_id);

    @Query("select p from PickupMatch p where p.accepting_team.team_id = ?1 and p.is_accepted = false")
    Iterable<PickupMatch> getChallenges(long team_id);
}
