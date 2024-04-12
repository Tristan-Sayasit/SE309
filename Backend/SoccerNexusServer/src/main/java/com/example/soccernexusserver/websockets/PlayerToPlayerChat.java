package com.example.soccernexusserver.websockets;

import com.example.soccernexusserver.context.SpringContext;
import com.example.soccernexusserver.entities.Message;
import com.example.soccernexusserver.entities.Notification;
import com.example.soccernexusserver.entities.Player;
import com.example.soccernexusserver.jparepos.MessageRepo;
import com.example.soccernexusserver.jparepos.NotificationRepo;
import com.example.soccernexusserver.jparepos.PlayerRepo;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;

@ServerEndpoint("/playerToPlayer/{signed_in_user}/{friend_user}")
@Component
public class PlayerToPlayerChat {

    private final MessageRepo _messageRepo;
    private final PlayerRepo _playerRepo;

    private final NotificationRepo _notificationRepo;

    public PlayerToPlayerChat() {
        _messageRepo = (MessageRepo) SpringContext.getApplicationContext().getBean("messageRepo");
        _playerRepo = (PlayerRepo) SpringContext.getApplicationContext().getBean("playerRepo");
        _notificationRepo = (NotificationRepo) SpringContext.getApplicationContext().getBean("notificationRepo");
    }

    private static Map<Session, String> SessionKeyMap = new Hashtable<>();
    private static Map<String, Session> KeySessionMap = new Hashtable<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("signed_in_user") String signed_in_user, @PathParam("friend_user") String friend_user) throws IOException {
        // Handle the case of a duplicate username
        String key_value = signed_in_user + "-" + friend_user;
        if (KeySessionMap.containsKey(key_value)) {
            session.getBasicRemote().sendText("Already In Chat");
            session.close();
        } else {
            SessionKeyMap.put(session, key_value);
            KeySessionMap.put(key_value, session);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        String key_string = SessionKeyMap.get(session);
        String[] split_key = key_string.split("-");
        String reverse_key = split_key[1] + "-" + split_key[0];
        Message newMessage = new Message();
        Notification notification = new Notification();
        newMessage.message_text = message;
        newMessage.time_of_message = LocalDate.now();
        Optional<Player> potential_sending_player = _playerRepo.getPlayerByUsername(split_key[0]);
        Optional<Player> potential_receiving_player = _playerRepo.getPlayerByUsername(split_key[1]);
        if (KeySessionMap.containsKey(reverse_key)) {
            try {
                KeySessionMap.get(reverse_key).getBasicRemote().sendText(message);
                KeySessionMap.get(key_string).getBasicRemote().sendText(message);
                newMessage.is_read = true;
                notification.is_read = true;
            } catch (Exception e) {
                throw new IOException();
            }
        } else {
            try {
                KeySessionMap.get(key_string).getBasicRemote().sendText(message);
                newMessage.is_read = false;
                notification.is_read = false;
            } catch (Exception e) {
                throw new IOException();
            }
        }
        if (potential_receiving_player.isPresent() && potential_sending_player.isPresent()) {
            newMessage.sending_player = potential_sending_player.get();
            newMessage.receiving_player = potential_receiving_player.get();
            notification.notification_type = "message";
            notification.notification_message = potential_sending_player.get().first_name +" sent you a message!";
            notification.notif_receiving_player = potential_receiving_player.get();
            notification.notif_sending_player = potential_sending_player.get();
        } else {
            throw new IOException();
        }
        _messageRepo.save(newMessage);
        _notificationRepo.save(notification);
    }

    @OnClose
    public void onClose(Session session) {
        String key_value = SessionKeyMap.get(session);
        SessionKeyMap.remove(session);
        KeySessionMap.remove(key_value);
    }

}
