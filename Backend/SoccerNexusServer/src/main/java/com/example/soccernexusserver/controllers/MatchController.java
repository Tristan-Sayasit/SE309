package com.example.soccernexusserver.controllers;

import com.example.soccernexusserver.entities.Notification;
import com.example.soccernexusserver.entities.PickupMatch;
import com.example.soccernexusserver.entities.Player;
import com.example.soccernexusserver.entities.Team;
import com.example.soccernexusserver.jparepos.NotificationRepo;
import com.example.soccernexusserver.jparepos.PickupMatchRepo;
import com.example.soccernexusserver.jparepos.PlayerRepo;
import com.example.soccernexusserver.jparepos.TeamRepo;
import com.example.soccernexusserver.jsonobjs.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/matches")
public class MatchController {

    private final TeamRepo _teamRepo;
    private final PickupMatchRepo _pickupMatchRepo;
    private final NotificationRepo _notificationRepo;
    private final PlayerRepo _playerRepo;

    public MatchController(PickupMatchRepo _pickupMatchRepo, TeamRepo _teamRepo, NotificationRepo _notificationRepo, PlayerRepo _playerRepo) {
        this._pickupMatchRepo = _pickupMatchRepo;
        this._teamRepo = _teamRepo;
        this._notificationRepo = _notificationRepo;
        this._playerRepo = _playerRepo;
    }

    @ApiOperation(value = "Challenge a team to a match")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created new match for team"), @ApiResponse(code = 404, message = "1 of 2 teams not found")})
    @RequestMapping(value = "/challengeTeam", method = RequestMethod.POST)
    public ResponseEntity<PickupMatch> ChallengeTeam(@RequestBody ChallengeTeamJson challenge) {
        Optional<Team> potentialChallengingTeam = _teamRepo.findById(challenge.challenging_id);
        Optional<Team> potentialAcceptingTeam = _teamRepo.findById(challenge.accepting_id);
        if (potentialChallengingTeam.isPresent() && potentialAcceptingTeam.isPresent()) {
            Team challengingTeam = potentialChallengingTeam.get();
            Team acceptingTeam = potentialAcceptingTeam.get();
            PickupMatch match = new PickupMatch();
            Notification newChallenge = new Notification();
            newChallenge.is_read = false;
            newChallenge.notif_sending_team = challengingTeam;
            newChallenge.notif_receiving_player = acceptingTeam.captain;
            newChallenge.notification_type = "challenge";
            newChallenge.notification_message = "Your team has been challenged by team " + challengingTeam.team_name + "!";
            match.accepting_team = acceptingTeam;
            match.challenging_team = challengingTeam;
            match.location = challenge.location;
            match.time_of_match = challenge.time_of_match;
            match.match_format = challenge.match_format;
            match.is_finished = false;
            match.is_accepted = false;
            _pickupMatchRepo.save(match);
            _notificationRepo.save(newChallenge);
            return new ResponseEntity<>(match, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/getMatchHistory/{team_id}", method = RequestMethod.GET)
    public ResponseEntity<Iterable<PickupMatch>> GetAllMatches(@PathVariable long team_id) {
        Optional<Team> potentialTeam = _teamRepo.findById(team_id);
        if (potentialTeam.isPresent()) {
            Team team = potentialTeam.get();
            Iterable<PickupMatch> matchHistory = _pickupMatchRepo.getMatchHistory(team.team_id);
            return new ResponseEntity<>(matchHistory, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/getMatchInfo/{match_id}", method = RequestMethod.GET)
    public ResponseEntity<MatchSpecificInfoJson> GetSpecificMatchInfo(@PathVariable long match_id) {
        Optional<PickupMatch> potentialPickupMatch = _pickupMatchRepo.findById(match_id);
        if (potentialPickupMatch.isPresent()) {
            PickupMatch actualMatch = potentialPickupMatch.get();
            MatchSpecificInfoJson ret = new MatchSpecificInfoJson();
            ret.match_id = actualMatch.pickup_match_id;
            ret.is_finished = actualMatch.is_finished;
            ret.match_time = actualMatch.time_of_match;
            ret.format = actualMatch.match_format;
            ret.location = actualMatch.location;
            ret.accepting_team_goals = actualMatch.accepting_team_goals;
            ret.challenging_team_goals = actualMatch.challenging_team_goals;
            ret.challenging_team = new SimpleTeamJson();
            ret.accepting_team = new SimpleTeamJson();
            ret.challenging_team.team_id = actualMatch.challenging_team.team_id;
            ret.challenging_team.team_name = actualMatch.challenging_team.team_name;
            SimplePlayerJson captainChallenging = new SimplePlayerJson();
            captainChallenging.player_id = actualMatch.challenging_team.captain.player_id;
            captainChallenging.player_name = actualMatch.challenging_team.captain.first_name + " " + actualMatch.challenging_team.captain.last_name;
            captainChallenging.player_position = actualMatch.challenging_team.captain.preferred_position;
            ret.challenging_team.captain = captainChallenging;
            ret.accepting_team.team_id = actualMatch.accepting_team.team_id;
            ret.accepting_team.team_name = actualMatch.accepting_team.team_name;
            SimplePlayerJson captainAccepting = new SimplePlayerJson();
            captainAccepting.player_id = actualMatch.accepting_team.captain.player_id;
            captainAccepting.player_name = actualMatch.accepting_team.captain.first_name + " " + actualMatch.challenging_team.captain.last_name;
            captainAccepting.player_position = actualMatch.accepting_team.captain.preferred_position;
            ret.accepting_team.captain = captainAccepting;
            ret.accepting_team.players = new ArrayList<>();
            ret.challenging_team.players = new ArrayList<>();
            for (Player player : actualMatch.challenging_team.members) {
                SimplePlayerJson value = new SimplePlayerJson();
                value.player_id = player.player_id;
                value.player_name = player.first_name + " " + player.last_name;
                value.player_position = player.preferred_position;
                ret.challenging_team.players.add(value);
            }
            for (Player player : actualMatch.accepting_team.members) {
                SimplePlayerJson value = new SimplePlayerJson();
                value.player_id = player.player_id;
                value.player_name = player.first_name + " " + player.last_name;
                value.player_position = player.preferred_position;
                ret.accepting_team.players.add(value);
            }
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/submitMatchReport", method = RequestMethod.PUT)
    public ResponseEntity<PickupMatch> SubmitPostMatchStats(@RequestBody MatchStatsJson matchStats) {
        Optional<PickupMatch> potentialMatch = _pickupMatchRepo.findById(matchStats.match_id);
        Optional<Team> potentialTeam = _teamRepo.findById(matchStats.team_id);
        if (potentialMatch.isPresent() && potentialTeam.isPresent()) {
            PickupMatch match = potentialMatch.get();
            Team team = potentialTeam.get();
            if (match.accepting_team.team_id == team.team_id) {
                match.accepting_team_goals += matchStats.goals_scored.size();
                match.a_team_report_done = true;
            } else {
                match.challenging_team_goals += matchStats.goals_scored.size();
                match.c_team_report_done = true;
            }
            for (GoalInfoJson goal : matchStats.goals_scored) {
                Optional<Player> potentialPlayer = _playerRepo.findById(goal.player_id);
                if (potentialPlayer.isPresent()) {
                    Player player = potentialPlayer.get();
                    player.assists += goal.assists_made;
                    player.goals += goal.number_of_goals;
                    player.assistPerGame = (float) player.assists / player.gamesPlayed;
                    player.goalsPerGame = (float) player.goals / player.gamesPlayed;
                    _playerRepo.save(player);
                }
            }
            _pickupMatchRepo.save(match);
            return new ResponseEntity<>(match, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/finishMatch", method = RequestMethod.PUT)
    public ResponseEntity<PickupMatch> FinishMatch(@RequestBody PickupMatch match) {
        Optional<PickupMatch> potentialMatch = _pickupMatchRepo.findById(match.pickup_match_id);
        if (potentialMatch.isPresent()) {
            PickupMatch actualMatch = potentialMatch.get();
            actualMatch.is_finished = true;
            for (Player player : actualMatch.challenging_team.members) {
                AdjustPlayerFinishStats(player);
            }
            for (Player player : actualMatch.accepting_team.members) {
                AdjustPlayerFinishStats(player);
            }
            _pickupMatchRepo.save(actualMatch);
            return new ResponseEntity<>(actualMatch, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private void AdjustPlayerFinishStats(Player player) {
        player.gamesPlayed++;
        player.goalsPerGame = (float) player.goals / player.gamesPlayed;
        player.assistPerGame = (float) player.assists / player.gamesPlayed;
        _playerRepo.save(player);
    }

    @RequestMapping(value = "/getChallenges/{team_id}", method = RequestMethod.GET)
    public ResponseEntity<Iterable<PickupMatch>> GetChallenges(@PathVariable long team_id) {
        Optional<Team> potentialTeam = _teamRepo.findById(team_id);
        if (potentialTeam.isPresent()) {
            Iterable<PickupMatch> challenges = _pickupMatchRepo.getChallenges(team_id);
            return new ResponseEntity<>(challenges, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/acceptChallenge", method = RequestMethod.PUT)
    public ResponseEntity<PickupMatch> AcceptChallenge(@RequestBody PickupMatch match_to_accept) {
        Optional<PickupMatch> potentialMatch = _pickupMatchRepo.findById(match_to_accept.pickup_match_id);
        if (potentialMatch.isPresent()) {
            PickupMatch match = potentialMatch.get();
            match.is_accepted = true;
            _pickupMatchRepo.save(match);
            return new ResponseEntity<>(match, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/denyChallenge/{match_to_decline}", method = RequestMethod.DELETE)
    public ResponseEntity<PickupMatch> DenyChallenge(@PathVariable long match_to_decline) {
        Optional<PickupMatch> potentialMatch = _pickupMatchRepo.findById(match_to_decline);
        if (potentialMatch.isPresent()) {
            PickupMatch match = potentialMatch.get();
            _pickupMatchRepo.delete(match);
            return new ResponseEntity<>(match, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
