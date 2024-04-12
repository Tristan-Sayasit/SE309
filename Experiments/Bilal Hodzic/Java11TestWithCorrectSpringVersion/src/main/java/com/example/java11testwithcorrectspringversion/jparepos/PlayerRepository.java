package com.example.java11testwithcorrectspringversion.jparepos;

import com.example.java11testwithcorrectspringversion.entities.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

}
