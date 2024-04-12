package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.playerID;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.se309.soccernexus.util.recyclerviews.User;
import com.se309.soccernexus.util.recyclerviews.UserAdapter;
import com.se309.soccernexus.util.recyclerviews.UserChatLobby;
import com.se309.soccernexus.util.recyclerviews.UserChatLobbyAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Nathan Turnis
 * Class for displaying and handling logic in the Chat Lobby Page
 */
public class PrivateMessageLobbyActivity extends AppCompatActivity {

    ImageButton backToHomeBtn, addFriendsBtn, removeFriendsBtn;
    Button cancelBtn, friendRequestsBtn;
    RecyclerView recyclerView, friendsRecyclerView;
    ConstraintLayout addFriendsLayout;

    private ArrayList<UserChatLobby> userChatLobbyList;
    private ArrayList<User> usersList = new ArrayList<>();

    public UserChatLobbyAdapter adapter;

    /**
     * Called when the activity is created. Initializes a listener on the required buttons.
     * Creates a recycler view then fills that recycler view by requesting the lobby list (friends list).
     * If the user clicks the add friend button, it will show a list of users they can add.
     * If the user click the remove friend button, the user can choose to remove each friend
     * individually.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_message_lobby_activity);

        backToHomeBtn = findViewById(R.id.message_lobby_backBtn);
        addFriendsBtn = findViewById(R.id.message_lobby_addFriendsBtn);
        removeFriendsBtn = findViewById(R.id.lobby_removeBtn);
        cancelBtn = findViewById(R.id.add_friend_cancel);
        friendRequestsBtn = findViewById(R.id.lobby_friendRequestsBtn);
        recyclerView = findViewById(R.id.message_lobby_recyclerView);
        addFriendsLayout = findViewById(R.id.add_friend_layout);
        friendsRecyclerView = findViewById(R.id.add_friend_recyclerView);

        userChatLobbyList = new ArrayList<>();

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.recyclerview_divider));

        recyclerView.addItemDecoration(itemDecorator);

        requestLobbyList();

        removeFriendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(removeFriendsBtn.getBackgroundTintList() == ColorStateList.valueOf(0xFF0027ED)) {
                    removeFriendsBtn.setBackgroundTintList(ColorStateList.valueOf(0xFF6200ED));
                } else {
                    removeFriendsBtn.setBackgroundTintList(ColorStateList.valueOf(0xFF0027ED));
                }
                for(UserChatLobby user : userChatLobbyList) {
                    if(user.isCanRemove()){
                        user.setCanRemove(false);
                    } else {
                        user.setCanRemove(true);
                    }
                }
                updateRecyclerView();
            }
        });

        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrivateMessageLobbyActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        addFriendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usersList.clear();

                RequestQueue queue = Volley.newRequestQueue(PrivateMessageLobbyActivity.this);
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_SERVER_URL + "/players/getAllPlayers", null,

                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.i("all users response", response.toString());
                                for(int i = 0; i < response.length(); i++) {
                                    try {
                                        long id = response.getJSONObject(i).getLong("player_id");
                                        if(id == playerID) {
                                            continue;
                                        }
                                        String firstName = response.getJSONObject(i).getString("first_name");
                                        String lastName = response.getJSONObject(i).getString("last_name");
                                        String username = response.getJSONObject(i).getString("username");
                                        usersList.add(new User(firstName, lastName, username, id, 0, 0));
                                    } catch (Exception e) {

                                    }
                                }

                                updateFriendsRecyclerView();
                                //915
                                ObjectAnimator animation = ObjectAnimator.ofFloat(addFriendsLayout, "translationY", -2330);
                                animation.setDuration(190);
                                animation.start();
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("friends error", error.toString());
                                error.printStackTrace();
                            }
                        }
                );

                queue.add(request);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLobbyList();
                ObjectAnimator animation = ObjectAnimator.ofFloat(addFriendsLayout, "translationY", 2330);
                animation.setDuration(190);
                animation.start();
            }
        });

        friendRequestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrivateMessageLobbyActivity.this, FriendRequestsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Updates the friends/chat list recycler view
     */
    public void updateRecyclerView() {
        adapter = new UserChatLobbyAdapter(userChatLobbyList, PrivateMessageLobbyActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void test() {

    }

    /**
     * Updates the recycler view for 'Add Friends'
     */
    public void updateFriendsRecyclerView() {
        ArrayList<User> usersList2 = new ArrayList<>(usersList.stream().filter(x -> !userChatLobbyList.stream().anyMatch(y -> y.getId() == x.getID())).collect(Collectors.toList()));
        usersList.clear();
        usersList.addAll(usersList2);
        UserAdapter adapter2 = new UserAdapter(usersList, PrivateMessageLobbyActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        friendsRecyclerView.setLayoutManager(layoutManager);
        friendsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        friendsRecyclerView.setAdapter(adapter2);
    }

    /**
     * Requests all the friends for the logged-in user.
     */
    public void requestLobbyList() {
        userChatLobbyList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(PrivateMessageLobbyActivity.this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_SERVER_URL + "/friends/getFriendsList/" + playerID, null,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                Log.i("Friends list response", response.toString());
                                String firstName = response.getJSONObject(i).getString("first_name");
                                String lastName = response.getJSONObject(i).getString("last_name");
                                long id = response.getJSONObject(i).getLong("player_id");
                                String username = response.getJSONObject(i).getString("username");
                                userChatLobbyList.add(new UserChatLobby(firstName + " " + lastName, "", id, username));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        updateRecyclerView();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        );
        queue.add(request);

    }

}