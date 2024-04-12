package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.playerID;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.se309.soccernexus.util.recyclerviews.Player;
import com.se309.soccernexus.util.recyclerviews.PlayerListAdapter;
import com.se309.soccernexus.util.recyclerviews.User;
import com.se309.soccernexus.util.recyclerviews.UserAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Tristan Sayasit
 * Class for displaying and handling logic for the View Team page
 */
public class ViewTeamActivity extends AppCompatActivity {

    ImageButton profilePageButton;
    ImageButton teamPageButton;
    ImageButton homePageButton;
    ImageButton settingsButton;
    ImageButton challengeTeamBtn, backBtn, editTeamBtn;
    Button invPlayerButton;
    Button cancelButton;
    Button chatButton;
    Button matchHistoryButton;
    TextView teamName;
    TextView teamLocation;
    RecyclerView recyclerView;
    TextView playerCountTxt;
    private ArrayList<Player> playerList;
    private long teamID;
    ConstraintLayout inviteLayout;
    TextInputLayout searchUsername;
    RecyclerView inviteRecyclerView;

    ArrayList<User> userArrayList = new ArrayList<>();

    /**
     * Called when the activity is created. Initializes a listener on the required buttons.
     * Gets the proper team ID from extras. Makes a request to the backend for all the
     * information of the team ID. Updates the team name, player count, and the recycler
     * view holding each player information. When the user clicks the invite players button,
     * a window pops up to search players. Upon clicking search, a request is made to search
     * said player. Updates recycler view with an invite button next to each player.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_team_activity);
        teamName = findViewById(R.id.teamNameView);
        teamLocation = findViewById(R.id.teamLocationView);
        settingsButton = findViewById(R.id.view_team_settingsPageButton);
        recyclerView = findViewById(R.id.playersRecyclerView);
        playerCountTxt = findViewById(R.id.playerCountText);
        invPlayerButton = findViewById(R.id.view_team_invPlayerButton);
        chatButton = findViewById(R.id.view_team_chatButton);
        matchHistoryButton = findViewById(R.id.view_team_matchHistoryButton);
        inviteLayout = findViewById(R.id.invite_layout);
        cancelButton = findViewById(R.id.invite_cancel);
        searchUsername = findViewById(R.id.invite_usernameSearch);
        inviteRecyclerView = findViewById(R.id.invite_recyclerView);
        challengeTeamBtn = findViewById(R.id.view_team_challengeBtn);
        editTeamBtn = findViewById(R.id.view_team_editTeamBtn);
        backBtn = findViewById(R.id.view_team_backBtn);

        playerList = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        teamID = extras.getLong("teamid");
        Log.i("team id extra", String.valueOf(teamID));

        RequestQueue queue = Volley.newRequestQueue(ViewTeamActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_SERVER_URL + "/teams/getTeam/" + teamID, null,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        Log.i("view response", response.toString());
                        try {
                            teamName.setText(response.getString("team_name"));
                            teamLocation.setText(response.getString("location"));

                            long captainID = response.getJSONObject("captain").getLong("player_id");
                            if (captainID != playerID) {
                                invPlayerButton.setEnabled(false);
                                challengeTeamBtn.setEnabled(false);
                                editTeamBtn.setEnabled(false);
                                challengeTeamBtn.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                                editTeamBtn.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                            }
                            String firstName = response.getJSONObject("captain").getString("first_name");
                            String lastName = response.getJSONObject("captain").getString("last_name");
                            String position = response.getJSONObject("captain").getString("preferred_position");
                            playerList.add(new Player(firstName + " " + lastName, position, true));

                            for(int i = 0; i < response.getJSONArray("members").length(); i++) {
                                if(response.getJSONArray("members").getJSONObject(i).getLong("player_id") == captainID) {
                                    continue;
                                }
                                String firstName1 = response.getJSONArray("members").getJSONObject(i).getString("first_name");
                                String lastName1 = response.getJSONArray("members").getJSONObject(i).getString("last_name");
                                String position1 = response.getJSONArray("members").getJSONObject(i).getString("preferred_position");
                                playerList.add(new Player(firstName1 + " " + lastName1, position1, false));
                            }
                            playerCountTxt.setText(playerList.size() + "/12");

                            PlayerListAdapter adapter = new PlayerListAdapter(playerList);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Log.i("JSON Response", response.toString());
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("JSON Error", error.toString());
                    }
                }
        );
        queue.add(request);

        teamName = findViewById(R.id.teamNameView);

        profilePageButton = findViewById(R.id.view_team_profilePageButton);
        teamPageButton = findViewById(R.id.view_team_teamPageButton);
        homePageButton = findViewById(R.id.view_team_homePageButton);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTeamActivity.this, MultipleViewTeamActivity.class);
                startActivity(intent);
            }
        });

        searchUsername.getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {

                    try {
                        InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(findViewById(R.id.linearLayout3).getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    RequestQueue queue = Volley.newRequestQueue(ViewTeamActivity.this);
                    JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_SERVER_URL + "/players/searchPlayersByUsername/" + searchUsername.getEditText().getText().toString(), null,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    userArrayList.clear();
                                    Log.i("search players", response.toString());
                                    for(int i = 0; i < response.length(); i++) {
                                        try {
                                            long id = response.getJSONObject(i).getLong("player_id");
                                            if(id == playerID) {
                                                continue;
                                            }
                                            String firstName = response.getJSONObject(i).getString("first_name");
                                            String lastName = response.getJSONObject(i).getString("last_name");
                                            String username = response.getJSONObject(i).getString("username");
                                            userArrayList.add(new User(firstName, lastName, username, id, 1, 0));
                                        } catch (Exception e) {

                                        }
                                    }
                                    updateInviteRecyclerView();
                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("search error", error.toString());
                                }
                            }
                    );
                    queue.add(request);

                    Log.i("test", "hit enter");
                    return true;

                }
                return false;
            }
        });

        challengeTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTeamActivity.this, PickupMatchChallengeActivity.class);
                intent.putExtra("ourTeam", teamID);
                startActivity(intent);
            }
        });

        editTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTeamActivity.this, EditTeamActivity.class);
                intent.putExtra("teamid", teamID);
                startActivity(intent);
            }
        });

        invPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animation = ObjectAnimator.ofFloat(inviteLayout, "translationY", -2330);
                animation.setDuration(190);
                animation.start();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animation = ObjectAnimator.ofFloat(inviteLayout, "translationY", 2330);
                animation.setDuration(190);
                animation.start();
            }
        });

        profilePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTeamActivity.this, ProfileViewActivity.class);
                startActivity(intent);
            }
        });

        teamPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewTeamActivity.this, MultipleViewTeamActivity.class);
                startActivity(intent);
            }
        });

        homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewTeamActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewTeamActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewTeamActivity.this, TeamChatroomActivity.class);
                intent.putExtra("name", teamName.getText().toString());
                intent.putExtra("team_id", teamID);
                startActivity(intent);
            }
        });

        matchHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewTeamActivity.this, MatchHistoryActivity.class);
                intent.putExtra("teamid", teamID);
                startActivity(intent);
            }
        });
    }

    /**
     * Updates the recycler view for inviting players.
     */
    public void updateInviteRecyclerView() {
        UserAdapter adapter2 = new UserAdapter(userArrayList, ViewTeamActivity.this, teamID);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        inviteRecyclerView.setLayoutManager(layoutManager);
        inviteRecyclerView.setItemAnimator(new DefaultItemAnimator());
        inviteRecyclerView.setAdapter(adapter2);
    }
}
