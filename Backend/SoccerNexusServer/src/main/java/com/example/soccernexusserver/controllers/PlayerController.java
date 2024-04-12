package com.example.soccernexusserver.controllers;

import com.example.soccernexusserver.entities.Message;
import com.example.soccernexusserver.entities.PickupMatch;
import com.example.soccernexusserver.entities.Player;
import com.example.soccernexusserver.entities.Team;
import com.example.soccernexusserver.jparepos.MessageRepo;
import com.example.soccernexusserver.jparepos.PickupMatchRepo;
import com.example.soccernexusserver.jparepos.PlayerRepo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Api(value = "Player Controller", description = "Handles operations relating to players")
@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerRepo _playerRepo;
    private final MessageRepo _messageRepo;

    private final PickupMatchRepo _pickupMatchRepo;

    public PlayerController(PlayerRepo _playerRepo, MessageRepo _messageRepo, PickupMatchRepo _pickupMatchRepo) {
        this._playerRepo = _playerRepo;
        this._messageRepo = _messageRepo;
        this._pickupMatchRepo = _pickupMatchRepo;
    }

    @ApiOperation(value = "Gets a specific player by ID")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully got player"), @ApiResponse(code = 404, message = "Player not found")})
    @RequestMapping(value = "/getProfile/{id}", method = RequestMethod.GET)
    public ResponseEntity<Player> GetPlayerProfile(@PathVariable long id) {
        Optional<Player> player = _playerRepo.findById(id);
        return player.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Creates a new player with the sign up")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Player ID in use"), @ApiResponse(code = 201, message = "New player created")})
    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity<Player> SignUp(@Valid @RequestBody Player newPlayer) {
        if (newPlayer.player_id == 0) {
            try {
                _playerRepo.save(newPlayer);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(newPlayer, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Logs the player in")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully logged in"), @ApiResponse(code = 400, message = "Login failed")})
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Player> LogIn(@RequestBody Player loginAccount) {
        Optional<Player> potential = _playerRepo.login(loginAccount.username, loginAccount.password);
        potential.ifPresent(player -> player.password = null);
        return potential.map(account -> new ResponseEntity<>(account, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @ApiOperation(value = "Updates personal profile info")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully updated"), @ApiResponse(code = 404, message = "Player not found")})
    @RequestMapping(value = "/updateProfile", method = RequestMethod.PUT)
    public ResponseEntity<Player> EditPlayerProfile(@RequestBody Player updatedProfile) {
        Optional<Player> existingPlayer = _playerRepo.findById(updatedProfile.player_id);
        if (existingPlayer.isPresent()) {
            Player player = existingPlayer.get();
            player.first_name = updatedProfile.first_name;
            player.last_name = updatedProfile.last_name;
            player.birthday = updatedProfile.birthday;
            player.height = updatedProfile.height;
            player.weight = updatedProfile.weight;
            player.preferred_position = updatedProfile.preferred_position;
            player.profile_pic = updatedProfile.profile_pic;
            player.has_profile = updatedProfile.has_profile;
            _playerRepo.save(player);
            return new ResponseEntity<>(player, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @ApiOperation(value = "Deletes a player")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Profile deleted"), @ApiResponse(code = 400, message = "Database issue"), @ApiResponse(code = 404, message = "Player not found")})
    @RequestMapping(value = "/deleteProfile/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> DeletePlayerProfile(@PathVariable long id) {
        Optional<Player> profile = _playerRepo.findById(id);
        if (profile.isPresent()) {
            try {
                _playerRepo.delete(profile.get());
                return new ResponseEntity<>("Player profile deleted", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Sql exception", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Player Not found", HttpStatus.NOT_FOUND);
        }

    }

    @ApiOperation(value = "Gets the teams a player is on")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Player not found"), @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 404, message = "Player not on any trams"),})
    @RequestMapping(value = "/getCurrentPlayersTeams/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Team>> GetCurrPlayersTeam(@PathVariable long id) {
        Optional<Player> player = _playerRepo.findById(id);
        if (player.isPresent()) {
            Player currPlayer = player.get();
            if (!currPlayer.teams.isEmpty()) {
                return new ResponseEntity<>(currPlayer.teams, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Get a list of all players")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully returned a list")})
    @RequestMapping(value = "/getAllPlayers", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Player>> GetAllPlayers() {
        return new ResponseEntity<>(_playerRepo.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/getMessageHistory/{signed_in_username}/{friend_username}", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> GetMessageHistory(@PathVariable String signed_in_username, @PathVariable String friend_username) {
        List<Message> messages = new ArrayList<>();
        Optional<Player> signedInPoten = _playerRepo.getPlayerByUsername(signed_in_username);
        Optional<Player> friendPoten = _playerRepo.getPlayerByUsername(friend_username);
        if (signedInPoten.isPresent() && friendPoten.isPresent()) {
            Player signedIn = signedInPoten.get();
            Player friend = friendPoten.get();
            Iterable<Message> nonListMessageHistory = _messageRepo.getMessageHistory(signedIn.player_id, friend.player_id);
            nonListMessageHistory.forEach(messages::add);
            for (Message message : messages) {
                if (message.receiving_player.player_id == signedIn.player_id) {
                    message.is_read = true;
                    _messageRepo.save(message);
                }
            }
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @ApiOperation(value = "Searches for players by username")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid username"), @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 404, message = "No players found")})
    @RequestMapping(value = "/searchPlayersByUsername/{username}", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Player>> SearchPlayersByUsername(@PathVariable String username) {
        Iterable<Player> players = _playerRepo.findAllByUsernameContains(username);
        players.forEach(x -> {
            x.teams = null;
            x.notifications = null;
            x.received_messages = null;
            x.sent_messages = null;
        });
        return new ResponseEntity<>(players, HttpStatus.OK);
    }
    @ApiOperation(value = "Searches for players by preferred position")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid preferred position"), @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 404, message = "No players found")})
    @RequestMapping(value = "/searchPlayersByPreferredPosition/{preferred_position}", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Player>> SearchPlayersByPreferredPosition(@PathVariable String preferred_position) {
        Iterable<Player> players = _playerRepo.findAllByPreferredPosition(preferred_position);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }
    @ApiOperation(value = "Updates a player's email")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid request body"), @ApiResponse(code = 404, message = "Player not found"), @ApiResponse(code = 200, message = "Success")})
    @RequestMapping(value = "/updateEmail", method = RequestMethod.PUT)
    public ResponseEntity<Player> EditPlayerEmail(@RequestBody Player updatedEmail) {
        Optional<Player> existingPlayer = _playerRepo.findById(updatedEmail.player_id);
        if (existingPlayer.isPresent()) {
            Player player = existingPlayer.get();
            player.email = updatedEmail.email;
            _playerRepo.save(player);
            return new ResponseEntity<>(player, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @ApiOperation(value = "Updates a player's password")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid request body"), @ApiResponse(code = 404, message = "Player not found"), @ApiResponse(code = 200, message = "Success")})
    @RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
    public ResponseEntity<Player> EditPlayerPassword(@RequestBody Player updatedPassword) {
        Optional<Player> existingPlayer = _playerRepo.findById(updatedPassword.player_id);
        if (existingPlayer.isPresent()) {
            Player player = existingPlayer.get();
            player.password = updatedPassword.password;
            _playerRepo.save(player);
            return new ResponseEntity<>(player, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

    }
    @RequestMapping(value = "/getUpcomingMatches/{playerId}", method = RequestMethod.GET)
    public ResponseEntity<List<PickupMatch>> GetPlayersUpcomingMatches(@PathVariable long playerId){
        Optional<Player> existingPlayer = _playerRepo.findById(playerId);
        if(existingPlayer.isPresent()){
            Player player = existingPlayer.get();
            List<PickupMatch> matches = new ArrayList<>();
            for(Team team : player.teams){
                Iterable<PickupMatch> teamsMatches = _pickupMatchRepo.getMatchHistory(team.team_id);
                for(PickupMatch teamMatch: teamsMatches){
                    if (matches.stream().noneMatch(x -> x.pickup_match_id == teamMatch.pickup_match_id)) {
                        matches.add(teamMatch);
                    }
                }
            }
            return new ResponseEntity<>(matches, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
