package com.example.java11testwithcorrectspringversion.controllers;

import com.example.java11testwithcorrectspringversion.entities.Player;
import com.example.java11testwithcorrectspringversion.entities.Team;
import com.example.java11testwithcorrectspringversion.jparepos.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private PlayerRepository _playerRepos;
    @RequestMapping(
            value = "/welcome",
            method = RequestMethod.GET
    )
    public ResponseEntity<String> Welcome(){
        return new ResponseEntity<>("Worked Good", HttpStatus.OK);
    }
    @RequestMapping(
            value = "/addPlayer",
            method = {RequestMethod.POST}
    )
    public ResponseEntity<Player> AddPlayer(@RequestBody Player newPlayer){
        _playerRepos.save(newPlayer);
        return new ResponseEntity<>(newPlayer, HttpStatus.OK);
    }
}
