package com.example.soccernexusserver.jparepos;

import com.example.soccernexusserver.entities.Team;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepo extends CrudRepository<Team, Long> {

}
