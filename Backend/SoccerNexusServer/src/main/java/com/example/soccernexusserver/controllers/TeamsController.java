package com.example.soccernexusserver.controllers;

import com.example.soccernexusserver.entities.Invite;
import com.example.soccernexusserver.entities.Notification;
import com.example.soccernexusserver.entities.Player;
import com.example.soccernexusserver.entities.Team;
import com.example.soccernexusserver.jparepos.InviteRepo;
import com.example.soccernexusserver.jparepos.NotificationRepo;
import com.example.soccernexusserver.jparepos.PlayerRepo;
import com.example.soccernexusserver.jparepos.TeamRepo;
import com.example.soccernexusserver.jsonobjs.InvitePlayerJson;
import com.example.soccernexusserver.jsonobjs.TeamMemberJson;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/teams")
public class TeamsController {

    private final TeamRepo _teamRepo;
    private final PlayerRepo _playerRepo;
    private final InviteRepo _inviteRepo;
    private final NotificationRepo _notificationRepo;


    public TeamsController(TeamRepo _teamRepo, PlayerRepo _playerRepo, InviteRepo _inviteRepo, NotificationRepo _notificationRepo) {
        this._teamRepo = _teamRepo;
        this._playerRepo = _playerRepo;
        this._inviteRepo = _inviteRepo;
        this._notificationRepo = _notificationRepo;
    }

    @ApiOperation(value = "Gets a team by ID")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Team not found"), @ApiResponse(code = 200, message = "Success")})
    @RequestMapping(value = "/getTeam/{id}", method = RequestMethod.GET)
    public ResponseEntity<Team> GetTeam(@PathVariable long id) {
        Optional<Team> team = _teamRepo.findById(id);
        return team.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Creates a new team")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid request body"), @ApiResponse(code = 201, message = "Team created successfully")})
    @PostMapping("/createTeam")
    public ResponseEntity<Team> CreateTeam(@RequestBody Team newTeam) {
        Optional<Player> profile = _playerRepo.findById(newTeam.captain.player_id);
        if (profile.isPresent()) {
            newTeam.members = new ArrayList<>();
            newTeam.members.add(profile.get());
            Team savedTeam = _teamRepo.save(newTeam);
            return new ResponseEntity<>(savedTeam, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Gets all teams")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    @RequestMapping(value = "/getAllTeams", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Team>> GetTeam() {
        return new ResponseEntity<>(_teamRepo.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Updates a team")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid request body"), @ApiResponse(code = 404, message = "Team not found"), @ApiResponse(code = 200, message = "Team updated successfully")})
    @RequestMapping(value = "/updateTeam", method = RequestMethod.PUT)
    public ResponseEntity<Team> UpdateTeam(@RequestBody Team updatedTeam) {
        Optional<Team> existingTeam = _teamRepo.findById(updatedTeam.team_id);

        if (existingTeam.isPresent()) {
            existingTeam.get().team_name = updatedTeam.team_name;
            existingTeam.get().location = updatedTeam.location;
            _teamRepo.save(existingTeam.get());
            return new ResponseEntity<>(existingTeam.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Deletes a team")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Team not found"), @ApiResponse(code = 200, message = "Team deleted successfully")})
    @RequestMapping(value = "/deleteTeam/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> DeleteTeam(@PathVariable long id) {
        Optional<Team> team = _teamRepo.findById(id);
        if (team.isPresent()) {
            team.get().members.clear();
            _teamRepo.save(team.get());
            _teamRepo.deleteById(id);
            return new ResponseEntity<>("Team successfully Deleted", HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @ApiOperation(value = "Joins a team")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid request body"), @ApiResponse(code = 404, message = "Team or player not found"), @ApiResponse(code = 200, message = "Player joined team successfully")})
    @RequestMapping(value = "/joinTeam", method = RequestMethod.PUT)
    public ResponseEntity<Team> JoinTeam(@RequestBody TeamMemberJson joinedTeam) {
        Optional<Team> existingTeam = _teamRepo.findById(joinedTeam.team_id);
        Optional<Player> joinedPlayer = _playerRepo.findById(joinedTeam.player_id);
        if (existingTeam.isPresent() && joinedPlayer.isPresent()) {
            Team team = existingTeam.get();
            team.members.add(joinedPlayer.get());
            _teamRepo.save(team);
            return new ResponseEntity<>(team, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @ApiOperation(value = "Leaves a team")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid request body"), @ApiResponse(code = 404, message = "Team or player not found"), @ApiResponse(code = 200, message = "Player left team successfully")})
    @RequestMapping(value = "/leaveTeam/{team_id}/{player_id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> LeaveTeam(@PathVariable long team_id, @PathVariable long player_id) {
        Optional<Team> potentialTeam = _teamRepo.findById(team_id);
        Optional<Player> potentialPlayer = _playerRepo.findById(player_id);
        if (potentialPlayer.isPresent() && potentialTeam.isPresent()) {
            Team actualTeam = potentialTeam.get();
            for (Player member : actualTeam.members) {
                if (member.player_id == potentialPlayer.get().player_id) {
                    actualTeam.members.remove(member);
                    _teamRepo.save(actualTeam);
                    return new ResponseEntity<>("Successfully left team.", HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Invites a player to a team")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid request body"), @ApiResponse(code = 404, message = "Team or player not found"), @ApiResponse(code = 200, message = "Invitation sent successfully")})
    @RequestMapping(value = "/invitePlayer", method = RequestMethod.PUT)
    public ResponseEntity<Team> invitePlayerToTeam(@RequestBody InvitePlayerJson inviteRequest) {
        Optional<Team> existingTeam = _teamRepo.findById(inviteRequest.team_id);
        Optional<Player> invitedPlayer = _playerRepo.findById(inviteRequest.player_id);
        Notification inviteNotif = new Notification();
        if (existingTeam.isPresent() && invitedPlayer.isPresent()) {
            Team team = existingTeam.get();
            Invite newInvite = new Invite();
            newInvite.inviting_team = existingTeam.get();
            inviteNotif.notif_sending_team = team;
            inviteNotif.notif_receiving_player = invitedPlayer.get();
            inviteNotif.is_read = false;
            inviteNotif.notification_type = "invite";
            inviteNotif.notification_message = "You've been invited to join team " + team.team_name + "!";
            newInvite.invited_player = invitedPlayer.get();
            _inviteRepo.save(newInvite);
            _notificationRepo.save(inviteNotif);
            return new ResponseEntity<>(team, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Gets a player's invites")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Player not found"), @ApiResponse(code = 200, message = "Success")})
    @RequestMapping(value = "/getPlayerInvites/{player_id}", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Invite>> GetPlayerInvites(@PathVariable long player_id) {
        Optional<Player> existingPlayer = _playerRepo.findById(player_id);
        if (existingPlayer.isPresent()) {
            Iterable<Invite> invites = _inviteRepo.findAllByPlayerId(player_id);
            return new ResponseEntity<>(invites, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/changeTeamName", method = RequestMethod.PUT)
    public ResponseEntity<Team> ChangeTeamName(@RequestBody Team updatedTeam) {
        Optional<Team> existingTeam = _teamRepo.findById(updatedTeam.team_id);

        if (existingTeam.isPresent()) {
            existingTeam.get().team_name = updatedTeam.team_name;
            _teamRepo.save(existingTeam.get());
            return new ResponseEntity<>(existingTeam.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/changeTeamLocation", method = RequestMethod.PUT)
    public ResponseEntity<Team> ChangeTeamLocation(@RequestBody Team updatedTeam) {
        Optional<Team> existingTeam = _teamRepo.findById(updatedTeam.team_id);

        if (existingTeam.isPresent()) {
            existingTeam.get().location = updatedTeam.location;
            _teamRepo.save(existingTeam.get());
            return new ResponseEntity<>(existingTeam.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/removePlayerFromTeam/{team_id}/{player_id}", method = RequestMethod.DELETE)
    public ResponseEntity<Team> RemovePlayerFromTeam(@PathVariable long team_id, @PathVariable long player_id) {
        Optional<Team> existingTeam = _teamRepo.findById(team_id);
        Optional<Player> removedPlayer = _playerRepo.findById(player_id);
        if (existingTeam.isPresent() && removedPlayer.isPresent()) {
            Team team = existingTeam.get();
            for (Player member : team.members) {
                if (member.player_id == removedPlayer.get().player_id) {
                    team.members.remove(member);
                    _teamRepo.save(team);
                    return new ResponseEntity<>(team, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/acceptInvite", method = RequestMethod.PUT)
    public ResponseEntity<Invite> acceptInvite(@RequestBody InvitePlayerJson invite) {
        Optional<Invite> existingInvite = _inviteRepo.findById(invite.invite_id);
        if (existingInvite.isPresent()) {
            Optional<Team> team = _teamRepo.findById(existingInvite.get().inviting_team.team_id);
            Optional<Player> player = _playerRepo.findById(existingInvite.get().invited_player.player_id);
            if (team.isPresent() && player.isPresent()) {
                Team joinedTeam = team.get();
                joinedTeam.members.add(player.get());
                _teamRepo.save(joinedTeam);
                _inviteRepo.deleteById(invite.invite_id);
                return new ResponseEntity<>(existingInvite.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/declineInvite/{invite_id}", method = RequestMethod.DELETE)
    public ResponseEntity<Invite> declineInvite(@PathVariable long invite_id) {
        Optional<Invite> existingInvite = _inviteRepo.findById(invite_id);

        if (existingInvite.isPresent()) {
            _inviteRepo.deleteById(invite_id);
            return new ResponseEntity<>(existingInvite.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
