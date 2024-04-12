package com.se309.soccernexus;

import android.animation.ObjectAnimator;
import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.playerID;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.se309.soccernexus.util.NotificationType;
import com.se309.soccernexus.util.recyclerviews.Match;
import com.se309.soccernexus.util.recyclerviews.MatchAdapter;
import com.se309.soccernexus.util.recyclerviews.Notification;
import com.se309.soccernexus.util.recyclerviews.NotificationsAdapter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.se309.soccernexus.util.recyclerviews.Post;
import com.se309.soccernexus.util.recyclerviews.PostAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Nathan Turnis
 * Class for displaying and handling logic in the Home Page
 */
public class HomeActivity extends AppCompatActivity {

    ImageButton teamsButton;
    ImageButton profileButton;
    ImageButton settingsButton;
    ImageButton notificationsButton, chatButton;
    ImageButton backBtn;
    View notificationView;
    ConstraintLayout notifLayout;

    TextView noMatchesTxt;

    RecyclerView recyclerView, pickupRecyclerView, postsRecyclerView;
    View fadedBg;

    ImageView redNotif;

    private ArrayList<Notification> notificationList;
    private ArrayList<Match> matchList;
    private ArrayList<Post> postList;

    RequestQueue queue;


    /**
     * Called when the activity is created. Initializes a listener on the required buttons.
     * Makes a request to the backend asking for notifications of the logged-in user.
     * Based on the response, the notifications recycler view will be updated.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        profileButton = findViewById(R.id.profilePageButton);
        teamsButton = findViewById(R.id.teamPageButton);
        settingsButton = findViewById(R.id.settingsPageButton);
        notificationsButton = findViewById(R.id.home_notificationsBtn);
        chatButton = findViewById(R.id.home_chatButton);
        notificationView = findViewById(R.id.home_notificationView);
        backBtn = findViewById(R.id.home_notificationCloseBtn);
        notifLayout = findViewById(R.id.home_notificationLayout);
        recyclerView = findViewById(R.id.home_recycleViewNotif);
        pickupRecyclerView = findViewById(R.id.home_pickupRecycler);
        postsRecyclerView = findViewById(R.id.home_postsRecyclerView);
        fadedBg = findViewById(R.id.home_fadedBg);
        redNotif = findViewById(R.id.home_redNotif);

        noMatchesTxt = findViewById(R.id.home_noMatchesText);

        queue = Volley.newRequestQueue(HomeActivity.this);

        notificationList = new ArrayList<>();
        matchList = new ArrayList<>();
        postList = new ArrayList<>();

        getNotifications();
        getUpcomingMatches();

        postList.add(new Post("Vikings", "Lorem ipsum dolor sit amet, pibus euismod enim aliquam et. Morbi ultricies aliquet", LocalDateTime.now()));
       // postList.add(new Post("Vikings", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras mi nulla, ornare vel augue in, varius molestie nunc. Quisque imperdiet orci sed lectus dignissim, non laoreet erat mattis. Nulla fermentum risus quis magna scelerisque mattis. Pellentesque eu consequat nibh, sit amet volutpat ante. Suspendisse tempus mollis sapien. Donec sit amet ligula iaculis eros consectetur gravida at et nibh. Aenean quis vehicula lectus. Etiam eget dapibus ligula. Quisque sapien purus, imperdiet sit amet pellentesque at, sollicitudin a leo. Mauris semper enim orci, dapibus euismod enim aliquam et. Morbi ultricies aliquet aliquet. Duis in nisl id velit viverra viverra et nec dolor.", null));


        DividerItemDecoration itemDecorator = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.recyclerview_divider));

        recyclerView.addItemDecoration(itemDecorator);
        postsRecyclerView.addItemDecoration(itemDecorator);

        updatePostsRecyclerView();

        notificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatButton.setEnabled(false);
                notificationsButton.setEnabled(false);
                ObjectAnimator animation = ObjectAnimator.ofFloat(notifLayout, "translationX", -785);
                animation.setDuration(130);
                ObjectAnimator animation2 = ObjectAnimator.ofFloat(fadedBg, "alpha", 0f, .75f);
                animation2.setDuration(130);
                animation2.start();
                animation.start();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatButton.setEnabled(true);
                notificationsButton.setEnabled(true);
                ObjectAnimator animation = ObjectAnimator.ofFloat(notifLayout, "translationX", 785);
                animation.setDuration(130);
                ObjectAnimator animation2 = ObjectAnimator.ofFloat(fadedBg, "alpha", .75f, 0f);
                animation2.setDuration(130);
                animation2.start();
                animation.start();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doesPlayerHaveProfile(ProfileViewActivity.class);
            }
        });

        teamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doesPlayerHaveProfile(MultipleViewTeamActivity.class);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PrivateMessageLobbyActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getNotifications() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_SERVER_URL + "/notifications/getPlayerNotifications/" + playerID, null,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("notification", response.toString());
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                String type = response.getJSONObject(i).getString("notification_type");
                                long id = response.getJSONObject(i).getLong("notification_id");
                                if(type.equals("message")) {
                                    String message = response.getJSONObject(i).getString("notification_message");

                                    notificationList.add(new Notification("New Message!", message, id, NotificationType.MESSAGE, -1));
                                } else if(type.equals("invite")) {
                                    String message = response.getJSONObject(i).getString("notification_message");
                                    notificationList.add(new Notification("New Invite!", message, id, NotificationType.INVITE, -1));
                                } else if(type.equals("challenge")) {
                                    String message = response.getJSONObject(i).getString("notification_message");
                                    notificationList.add(new Notification("New challenge!", message, id, NotificationType.CHALLENGE, -1));
                                } else if(type.equals("friend request")) {
                                    String message = response.getJSONObject(i).getString("notification_message");
                                    notificationList.add(new Notification("Friend Request", message, id, NotificationType.FRIEND_REQUEST, -1));
                                } else {
                                    String message = response.getJSONObject(i).getString("notification_message");
                                    notificationList.add(new Notification("New Notification", message, id, NotificationType.UNKNOWN, -1));
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        updateRecyclerView();

                        if(notificationList.size() > 0) {
                            redNotif.setVisibility(View.VISIBLE);
                        } else {
                            redNotif.setVisibility(View.GONE);
                        }

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

    private void getUpcomingMatches() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_SERVER_URL + "/players/getUpcomingMatches/" + playerID, null,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("upcoming matches response", response.toString());
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                long matchID = response.getJSONObject(i).getLong("pickup_match_id");

                                String teamName = response.getJSONObject(i).getJSONObject("accepting_team").getString("team_name");
                                String location = response.getJSONObject(i).getString("location");
                                LocalDateTime date = LocalDateTime.parse(response.getJSONObject(i).getString("time_of_match"));
                                if(date.isAfter(LocalDateTime.now())) {
                                    matchList.add(new Match(teamName, location, date, matchID));
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        updateMatchesRecyclerView();
                        if(matchList.size() == 0) {
                            noMatchesTxt.setVisibility(View.VISIBLE);
                        } else {
                            noMatchesTxt.setVisibility(View.GONE);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(matchList.size() == 0) {
                            noMatchesTxt.setVisibility(View.VISIBLE);
                        } else {
                            noMatchesTxt.setVisibility(View.GONE);
                        }
                    }
                }

        );
        queue.add(request);
    }

    /**
     * Makes a request to see if the user has a profile.
     * If the user has a profile, the method will send them to the activity described in placeWantToGo.
     * If the user does not have a profile, the method will send them to the ProfileFirstActivity.
     * @param placeWantToGo The activity where the caller is intending to go to.
     */
    private void doesPlayerHaveProfile(Class placeWantToGo) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_SERVER_URL + "/players/getProfile/" + playerID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.i("has_profile", String.valueOf(response.getBoolean("has_profile")));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        try {
                            if(response.getBoolean("has_profile")) {
                                Intent intent = new Intent(HomeActivity.this, placeWantToGo);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(HomeActivity.this, ProfileFirstActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error response", String.valueOf(error.networkResponse.statusCode));
                        if(error.networkResponse.statusCode == 404) {
                            Log.i("error 404", "profile not found");
                        }
                    }
                }
        );
        queue.add(request);
    }

    /**
     * Updates the notification recycler view.
     */
    public void updateRecyclerView() {
        NotificationsAdapter adapter = new NotificationsAdapter(notificationList, HomeActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void updateMatchesRecyclerView() {
        MatchAdapter adapter = new MatchAdapter(matchList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        pickupRecyclerView.setNestedScrollingEnabled(false);
        pickupRecyclerView.setLayoutManager(layoutManager);
        pickupRecyclerView.setItemAnimator(new DefaultItemAnimator());
        pickupRecyclerView.setAdapter(adapter);
    }

    public void updatePostsRecyclerView() {
        PostAdapter adapter = new PostAdapter(postList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        postsRecyclerView.setNestedScrollingEnabled(false);
        postsRecyclerView.setLayoutManager(layoutManager);
        postsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        postsRecyclerView.setAdapter(adapter);
    }

}
