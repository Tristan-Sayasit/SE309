package com.example.soccernexusserver.controllers;

import com.example.soccernexusserver.entities.Notification;
import com.example.soccernexusserver.entities.Player;
import com.example.soccernexusserver.jparepos.NotificationRepo;
import com.example.soccernexusserver.jparepos.PlayerRepo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Api(value = "Notification Controller", description = "Handles notifications")
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final PlayerRepo _playerRepo;
    private final NotificationRepo _notificationRepo;

    public NotificationController(PlayerRepo _playerRepo, NotificationRepo _notificationRepo) {
        this._playerRepo = _playerRepo;
        this._notificationRepo = _notificationRepo;
    }

    @ApiOperation(value = "Gets a specific players notifications")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 404, message = "Player not foun d")})
    @RequestMapping(value = "/getPlayerNotifications/{player_id}", method = RequestMethod.GET)
    public ResponseEntity<List<Notification>> GetPlayerNotifications(@PathVariable long player_id) {
        Optional<Player> potentialPlayer = _playerRepo.findById(player_id);
        if (potentialPlayer.isPresent()) {
            Player playerActual = potentialPlayer.get();
            List<Notification> allNotifs = playerActual.notifications;
            allNotifs.removeIf(x -> x.is_read);
            return new ResponseEntity<>(allNotifs, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Reads a notification preventing it from being returned again")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful"), @ApiResponse(code = 404, message = "Notification Not found")})
    @RequestMapping(value = "/readNotification/{notif_id}", method = RequestMethod.DELETE)
    public ResponseEntity<Notification> ReadNotification(@PathVariable long notif_id) {
        Optional<Notification> potenNotification = _notificationRepo.findById(notif_id);
        if (potenNotification.isPresent()) {
            Notification notificationActual = potenNotification.get();
            notificationActual.is_read = true;
            _notificationRepo.save(notificationActual);
            return new ResponseEntity<>(notificationActual, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
