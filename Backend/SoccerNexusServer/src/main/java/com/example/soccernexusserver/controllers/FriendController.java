package com.example.soccernexusserver.controllers;

import com.example.soccernexusserver.entities.Friend;
import com.example.soccernexusserver.entities.Notification;
import com.example.soccernexusserver.entities.Player;
import com.example.soccernexusserver.jparepos.FriendRepo;
import com.example.soccernexusserver.jparepos.NotificationRepo;
import com.example.soccernexusserver.jparepos.PlayerRepo;
import com.example.soccernexusserver.jsonobjs.FriendRequestJson;
import com.example.soccernexusserver.jsonobjs.FriendRequestReturnJson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/friends")
public class FriendController {

    private final PlayerRepo _playerRepo;
    private final FriendRepo _friendRepo;
    private final NotificationRepo _notificationRepo;

    public FriendController(FriendRepo _friendRepo, PlayerRepo _playerRepo, NotificationRepo _notificationRepo) {
        this._playerRepo = _playerRepo;
        this._friendRepo = _friendRepo;
        this._notificationRepo = _notificationRepo;
    }

    @RequestMapping(value = "/sendRequest", method = RequestMethod.POST)
    public ResponseEntity<Player> AddFriend(@RequestBody FriendRequestJson friendRequest) {
        Optional<Player> potentialSignedIn = _playerRepo.findById(friendRequest.signed_in_player_id);
        Optional<Player> potentialNewFriend = _playerRepo.findById(friendRequest.friend_id);
        if (potentialNewFriend.isPresent() && potentialSignedIn.isPresent()) {
            Optional<Friend> existingAlready = _friendRepo.findSpecificFriend(friendRequest.signed_in_player_id, friendRequest.friend_id);
            if (existingAlready.isPresent()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                Notification newNotif = new Notification();
                Player signed_in = potentialSignedIn.get();
                Player friend = potentialNewFriend.get();
                newNotif.notification_message = signed_in.first_name + " " + signed_in.last_name + " sent you a friend request!";
                newNotif.notification_type = "friend request";
                newNotif.is_read = false;
                newNotif.notif_receiving_player = friend;
                newNotif.notif_sending_player = signed_in;
                _notificationRepo.save(newNotif);
                Friend friendDetail = new Friend();
                friendDetail.player_id_1 = friendRequest.signed_in_player_id;
                friendDetail.player_id_2 = friendRequest.friend_id;
                friendDetail.accepted = false;
                _friendRepo.save(friendDetail);
                return new ResponseEntity<>(potentialNewFriend.get(), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/getFriendsList/{player_id}", method = RequestMethod.GET)
    public ResponseEntity<List<Player>> GetFriendsList(@PathVariable long player_id) {
        Optional<Player> potentialPlayer = _playerRepo.findById(player_id);
        if (potentialPlayer.isPresent()) {
            Player player = potentialPlayer.get();
            List<Friend> friends = _friendRepo.getFriendsList(player.player_id);
            List<Player> friendsList = new ArrayList<>();
            for (Friend friend : friends) {
                Optional<Player> potentialFriend;
                if (friend.player_id_1 == player_id) {
                    potentialFriend = _playerRepo.findById(friend.player_id_2);
                } else {
                    potentialFriend = _playerRepo.findById(friend.player_id_1);
                }
                potentialFriend.ifPresent(friendsList::add);
            }
            friendsList.forEach(x -> {
                x.teams = null;
                x.notifications = null;
                x.received_messages = null;
                x.sent_messages = null;
            });
            return new ResponseEntity<>(friendsList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/removeFriend/{signed_in_id}/{friend_id}", method = RequestMethod.DELETE)
    public ResponseEntity<Friend> RemoveFriend(@PathVariable long signed_in_id, @PathVariable long friend_id) {
        Optional<Friend> potentialFriend = _friendRepo.findSpecificFriend(signed_in_id, friend_id);
        if (potentialFriend.isPresent()) {
            _friendRepo.delete(potentialFriend.get());
            return new ResponseEntity<>(potentialFriend.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/acceptRequest", method = RequestMethod.POST)
    public ResponseEntity<Player> AcceptRequest(@RequestBody FriendRequestJson request) {
        Optional<Friend> potentialFriend = _friendRepo.findById(request.request_id);
        if (potentialFriend.isPresent()) {
            Friend friendObj = potentialFriend.get();
            if(!friendObj.accepted) {
                friendObj.accepted = true;
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            _friendRepo.save(friendObj);
            Optional<Player> friendAdded;
            if (friendObj.player_id_1 == request.signed_in_player_id) {
                friendAdded = _playerRepo.findById(friendObj.player_id_2);
            } else {
                friendAdded = _playerRepo.findById(friendObj.player_id_1);
            }
            Optional<Notification> friendReq = _notificationRepo.findFriendReq(friendObj.player_id_1, friendObj.player_id_2);
            if(friendReq.isPresent()){
                Notification realNotf = friendReq.get();
                realNotf.is_read = true;
                _notificationRepo.save(realNotf);
            }
            return friendAdded.map(player -> new ResponseEntity<>(player, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/getFriendRequests/{player_id}", method = RequestMethod.GET)
    public ResponseEntity<List<FriendRequestReturnJson>> GetFriendRequests(@PathVariable long player_id) {
        Optional<Player> potentialPlayer = _playerRepo.findById(player_id);
        if (potentialPlayer.isPresent()) {
            Player player = potentialPlayer.get();
            List<Friend> friendRequestsRaw = _friendRepo.getReceivedFriendRequests(player.player_id);
            List<FriendRequestReturnJson> friendRequests = new ArrayList<>();
            for (Friend friend : friendRequestsRaw) {
                FriendRequestReturnJson newRequest = new FriendRequestReturnJson();
                newRequest.request_id = friend.friend_id;
                Optional<Player> potenProfile = _playerRepo.findById(friend.player_id_1);
                potenProfile.ifPresent(value -> {
                    newRequest.sending_player = value;
                    friendRequests.add(newRequest);
                });
            }
            friendRequests.forEach(x -> {
                x.sending_player.teams = null;
                x.sending_player.notifications = null;
                x.sending_player.received_messages = null;
                x.sending_player.sent_messages = null;
            });
            return new ResponseEntity<>(friendRequests, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/denyRequest/{request_id}", method = RequestMethod.DELETE)
    public ResponseEntity<Friend> DenyRequest(@PathVariable long request_id) {
        Optional<Friend> potentialFriend = _friendRepo.findById(request_id);
        if (potentialFriend.isPresent()) {
            Friend friend = potentialFriend.get();
            Optional<Notification> friendReq = _notificationRepo.findFriendReq(friend.player_id_1, friend.player_id_2);
            if(friendReq.isPresent()){
                Notification realNotf = friendReq.get();
                realNotf.is_read = true;
                _notificationRepo.save(realNotf);
            }
            _friendRepo.delete(potentialFriend.get());
            return new ResponseEntity<>(potentialFriend.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
