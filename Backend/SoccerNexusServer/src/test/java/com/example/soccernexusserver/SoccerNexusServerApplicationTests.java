package com.example.soccernexusserver;

import com.example.soccernexusserver.entities.PickupMatch;
import com.example.soccernexusserver.entities.Team;
import com.example.soccernexusserver.jsonobjs.*;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import com.example.soccernexusserver.entities.Player;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static io.restassured.RestAssured.*;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SoccerNexusServerApplicationTests {

    @LocalServerPort
    int port;

    @Before
    public void setUp() throws Exception{
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }
    @Test
    public void getProfileTest(){
        with().port(port).get("/players/getProfile/3").then().statusCode(200);
    }
    @Test
    public void signupTest(){
        String json = "{" +
                "    \"username\" : \"David232322\"," +
                "    \"password\" : \"Gottem\"," +
                "    \"email\": \"dluiz42@gmail.com\"," +
                "    \"first_name\" : \"David\"," +
                "    \"last_name\" : \"Luiz\"," +
                "    \"height\" : 60," +
                "    \"weight\" : 150.5," +
                "    \"birthday\" : \"2002-05-29\"," +
                "    \"preferred_position\" : \"defender\"," +
                "    \"has_profile\" : false" +
                "}";
        with().port(port).header("Content-Type","application/json").header("Accept","application/json").body(json).post("/players/signUp").then().statusCode(400);
    }

    @Test
    public void loginTest(){
        Player player = new Player();
        player.username = "David2323";
        player.password = "123";
        with().port(port).body(player).header("Content-Type","application/json").header("Accept","application/json").request("POST","/players/login").then().statusCode(400);
    }

    @Test
    public void updateProfileTest(){
        Player player = new Player();
        player.player_id = 3;
        player.first_name = "Mike";
        player.last_name = "Jordan";
        player.birthday = LocalDate.now();
        player.height = 122;
        player.weight = 122;
        player.preferred_position = "Defender";
        player.has_profile = true;
        with().port(port).body(player).header("Content-Type","application/json").header("Accept","application/json").request("PUT","/players/updateProfile").then().statusCode(200);
    }
    @Test
    public void deleteProfileTest(){
        with().port(port).delete("/players/deleteProfile/3").then().statusCode(400);
    }

    @Test
    public void getCurrPlayersTeams(){
        with().port(port).get("/players/getCurrentPlayersTeams/7").then().statusCode(200);
    }

    @Test
    public void getAllPlayersTest(){
        with().port(port).get("/players/getAllPlayers").then().statusCode(200);
    }

    @Test
    public void getMessageHistoryTest(){
        with().port(port).get("/players/getMessageHistory/1/2").then().statusCode(404);
    }
    @Test
    public void searchPlayersByUsernameTest(){
        with().port(port).get("/players/searchPlayersByUsername/David").then().statusCode(200);
    }
    @Test
    public void searchPlayersPositionTest(){
        with().port(port).get("/players/searchPlayersByPreferredPosition/Defender").then().statusCode(200);
    }

    @Test
    public void updateEmailTest(){
        Player player  = new Player();
        player.email = "dluizHAHHAHA@gmail.com";
        player.player_id = 7;
        with().port(port).body(player).header("Content-Type","application/json").header("Accept","application/json").request("PUT", "/players/updateEmail").then().statusCode(200);
    }

    @Test
    public void updatePasswordTest(){

        Player player  = new Player();
        player.password = "asfasf";
        player.player_id = 7;
        with().port(port).body(player).header("Content-Type","application/json").header("Accept","application/json").request("PUT", "/players/updatePassword").then().statusCode(200);
    }

    @Test
    public void upcomingMatchesTest(){
        with().port(port).get("/players/getUpcomingMatches/7").then().statusCode(200);
    }

    @Test
    public void getPlayerNotificationsTest(){
        with().port(port).get("/notifications/getPlayerNotifications/7").then().statusCode(200);
    }

    @Test
    public void readNotificationTest(){
        with().port(port).delete("/notifications/readNotification/6").then().statusCode(200);
    }

    @Test
    public void getTeamTest(){
        with().port(port).get("/teams/getTeam/13").then().statusCode(200);
    }

    @Test
    public void sendFriendRquestTest(){
        FriendRequestJson friendRequestJson = new FriendRequestJson();
        friendRequestJson.signed_in_player_id = 8;
        friendRequestJson.friend_id = 7;
        with().port(port).body(friendRequestJson).header("Content-Type","application/json").header("Accept","application/json").request("POST", "/friends/sendRequest").then().statusCode(200);
    }

    @Test
    public void getFriendsList(){
        with().port(port).get("/friends/getFriendsList/8").then().statusCode(200);
    }

    @Test
    public void removeFriendTest(){
        with().port(port).delete("/friends/removeFriend/8/7").then().statusCode(200);
    }

    @Test
    public void acceptFriendRequest(){
        FriendRequestJson friendRequestJson = new FriendRequestJson();
        friendRequestJson.request_id = 57;
        friendRequestJson.signed_in_player_id = 8;
        with().port(port).body(friendRequestJson).header("Content-Type","application/json").header("Accept","application/json").request("POST", "/friends/acceptRequest").then().statusCode(500);
    }

    @Test
    public void getFriendRequests(){
        with().port(port).get("/friends/getFriendRequests/8").then().statusCode(200);
    }

    @Test
    public void denyFriendRequestTest(){
        with().port(port).delete("/friends/denyRequest/57").then().statusCode(500);
    }

    @Test
    public void challengeTeamTest(){
        ChallengeTeamJson json = new ChallengeTeamJson();
        json.challenging_id = 13;
        json.accepting_id = 70;
        json.location = "WEmb";
        json.time_of_match = LocalDateTime.MAX;
        json.match_format = "11v11";
        with().port(port).body(json).header("Content-Type","application/json").header("Accept","application/json").request("POST", "/matches/challengeTeam").then().statusCode(500);
    }

    @Test
    public void getMatchHistoryTest(){
        with().port(port).get("/matches/getMatchHistory/70").then().statusCode(200);
    }
    @Test
    public void getMatchInfo(){
        with().port(port).get("/matches/getMatchInfo/24").then().statusCode(200);
    }

    @Test
    public void finishMatch(){
        PickupMatch match = new PickupMatch();
        match.pickup_match_id = 24;
        with().port(port).body(match).header("Content-Type","application/json").header("Accept","application/json").request("PUT", "/matches/finishMatch").then().statusCode(200);
    }
    @Test
    public void getChallenges(){
        with().port(port).get("/matches/getChallenges/70").then().statusCode(200);
    }

    @Test
    public void denyChallenge(){
        with().port(port).delete("/matches/denyChallenge/24").then().statusCode(200);
    }

    //JAck
    @Test
    public void createTeamTest(){
        Team team = new Team();
        team.captain = new Player();
        team.captain.player_id = 10;
        with().port(port).body(team).header("Content-Type","application/json").header("Accept","application/json").request("POST", "/teams/createTeam").then().statusCode(201);

    }

    //JAck
    @Test
    public void getAllTeamsTest(){
        with().port(port).get("teams/getAllTeams").then().statusCode(200);
    }

    //JAck
    @Test
    public void updateTeamTest(){
        Team team = new Team();
        team.team_name = "swagtastic";
        team.location = "Baltimore";
        team.team_id = 13;
        with().port(port).body(team).header("Content-Type","application/json").header("Accept","application/json").request("PUT", "/team/updateTeam").then().statusCode(404);
    }
    //JAck
    @Test
    public void deleteTeamTest(){
        with().port(port).delete("/team/deleteTeam/13").then().statusCode(404);
    }

    @Test
    public void acceptChallenge(){
        PickupMatch match = new PickupMatch();
        match.pickup_match_id = 22;
        with().port(port).body(match).header("Content-Type","application/json").header("Accept","application/json").request("PUT", "/matches/acceptChallenge").then().statusCode(200);

    }

    @Test
    public void matchReportTest(){
        MatchStatsJson matchStatsJson = new MatchStatsJson();
        matchStatsJson.goals_scored = new ArrayList<>();
        GoalInfoJson goalInfoJson = new GoalInfoJson();
        matchStatsJson.goals_scored.add(goalInfoJson);
        goalInfoJson.number_of_goals = 10;
        goalInfoJson.player_id = 8;
        goalInfoJson.assists_made = 20;
        matchStatsJson.team_id = 70;
        matchStatsJson.match_id = 22;
        with().port(port).body(matchStatsJson).header("Content-Type","application/json").header("Accept","application/json").request("PUT", "/matches/submitMatchReport").then().statusCode(200);

    }
    @Test
    public void matchReportTest2(){
        MatchStatsJson matchStatsJson = new MatchStatsJson();
        matchStatsJson.goals_scored = new ArrayList<>();
        GoalInfoJson goalInfoJson = new GoalInfoJson();
        matchStatsJson.goals_scored.add(goalInfoJson);
        goalInfoJson.number_of_goals = 10;
        goalInfoJson.player_id = 8;
        goalInfoJson.assists_made = 20;
        matchStatsJson.team_id = 71;
        matchStatsJson.match_id = 22;
        with().port(port).body(matchStatsJson).header("Content-Type","application/json").header("Accept","application/json").request("PUT", "/matches/submitMatchReport").then().statusCode(200);

    }
    @Test
    public void matchReportTest3(){
        MatchStatsJson matchStatsJson = new MatchStatsJson();
        matchStatsJson.goals_scored = new ArrayList<>();
        GoalInfoJson goalInfoJson = new GoalInfoJson();
        matchStatsJson.goals_scored.add(goalInfoJson);
        goalInfoJson.number_of_goals = 10;
        goalInfoJson.player_id = 8;
        goalInfoJson.assists_made = 20;
        matchStatsJson.team_id = 70;
        matchStatsJson.match_id = 100001;
        with().port(port).body(matchStatsJson).header("Content-Type","application/json").header("Accept","application/json").request("PUT", "/matches/submitMatchReport").then().statusCode(404);
    }

    @Test
    public void invitePlayer(){
        InvitePlayerJson invitePlayerJson = new InvitePlayerJson();
        invitePlayerJson.player_id = 32;
        invitePlayerJson.team_id = 37;
        with().port(port).body(invitePlayerJson).header("Content-Type","application/json").header("Accept","application/json").request("PUT", "/teams/invitePlayer").then().statusCode(200);
    }

    @Test
    public void acceptInvite(){
        InvitePlayerJson invitePlayerJson = new InvitePlayerJson();
        invitePlayerJson.invite_id = 62;
        with().port(port).body(invitePlayerJson).header("Content-Type","application/json").header("Accept","application/json").request("PUT", "/teams/acceptInvite").then().statusCode(400);
    }

    @Test
    public void leaveTeamTest(){
        with().port(port).delete("/teams/leaveTeam/71/8").then().statusCode(200);
    }

    @Test
    public void removePlayerTest(){
        with().port(port).delete("/teams/removePlayerFromTeam/70/7").then().statusCode(200);
    }

    @Test
    public void changeLocationTest(){
        Team team = new Team();
        team.location = "fart town";
        team.team_id = 61;
        with().port(port).body(team).header("Content-Type","application/json").header("Accept","application/json").request("PUT", "/teams/changeTeamLocation").then().statusCode(200);
    }

    @Test
    public void changeTeamNameTest(){
        Team team = new Team();
        team.team_name = "farters";
        team.team_id = 61;
        with().port(port).body(team).header("Content-Type","application/json").header("Accept","application/json").request("PUT", "/teams/changeTeamName").then().statusCode(200);
    }

    @Test
    public void getPlayerInvites(){
        with().port(port).get("teams/getPlayerInvites/32").then().statusCode(200);
    }
    @Test
    public void getPlayerInv2tes(){
        with().port(port).get("teams/getPlayerInvites/1321313").then().statusCode(400);
    }

    @Test
    public void removePlayerTest2(){
        with().port(port).delete("/teams/removePlayerFromTeam/73320/73321321").then().statusCode(404);
    }
    @Test
    public void declineInviteTest(){
        with().port(port).delete("/teams/declineInvite/28").then().statusCode(200);
    }
}
