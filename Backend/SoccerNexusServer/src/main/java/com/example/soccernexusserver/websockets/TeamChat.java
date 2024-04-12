package com.example.soccernexusserver.websockets;

import com.example.soccernexusserver.context.SpringContext;
import com.example.soccernexusserver.entities.Player;
import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpoint;

import com.example.soccernexusserver.jparepos.PlayerRepo;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;

@ServerEndpoint("/teamChat/{player_id}/{team_id}")
@Component
public class TeamChat {
    private static Map<Session, String> SessionTeamMap = new Hashtable<>();
    private static  Map<String, Session> TeamSessionMap = new Hashtable<>();

    private final PlayerRepo _playerRepo;

    public TeamChat() {
        _playerRepo = (PlayerRepo) SpringContext.getApplicationContext().getBean("playerRepo");
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("player_id")String player_id, @PathParam("team_id")String team_id) throws IOException{
        String key_value = player_id + "-" + team_id;
        if(TeamSessionMap.containsKey(key_value)){
            session.getBasicRemote().sendText("Already In Chat");
            session.close();
        }
        else {
            SessionTeamMap.put(session, key_value);
            TeamSessionMap.put(key_value, session);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException{
        String team_string = SessionTeamMap.get(session);
        String[] split_team = team_string.split("-");
        Optional<Player> potenPlayer = _playerRepo.findById(Long.parseLong(split_team[0]));
        if(potenPlayer.isEmpty()){
            throw new IOException();
        }
        Player sendingPlayer = potenPlayer.get();
        SessionTeamMap.forEach((user_session, team_key) -> {
            String[] split_poten = team_key.split("-");
            if(split_poten[1].compareTo( split_team[1]) == 0){
                try {
                    user_session.getBasicRemote().sendText(sendingPlayer.username + "@*" + message);
                }catch (Exception e){
                    throw new RuntimeException();
                }
            }
        });
    }
    @OnClose
    public void onClose(Session session) {
        String key_value = SessionTeamMap.get(session);
        SessionTeamMap.remove(session);
        TeamSessionMap.remove(key_value);
    }


}
