package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.numTeams;
import static com.se309.soccernexus.util.Const.playerID;
import static com.se309.soccernexus.util.Const.teamsPlayerIsOn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.se309.soccernexus.util.recyclerviews.Team;
import com.se309.soccernexus.util.recyclerviews.TeamAdapter;
import com.se309.soccernexus.util.recyclerviews.UserChatLobbyAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author Tristan Sayasit
 * Class for displaying and handling the logic in the Multiple View Team Page
 */
public class MultipleViewTeamActivity extends AppCompatActivity {

    ImageButton homePageButton;
    ImageButton profilePageButton;
    ImageButton settingsPageButtons;
    RecyclerView recyclerView;
    Button joinTeamPageButton;
    Button createTeamPageButton;
    ArrayList<Team> teamList = new ArrayList<>();

    /**
     * Called when the activity is created. Initializes a listener on the required buttons.
     * Makes a request to get all of the teams the logged-in user is on. For each team, the
     * recycler view is updated.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_view_team_activity);
        homePageButton = findViewById(R.id.multiple_view_team_homePageButton2);
        profilePageButton = findViewById(R.id.multiple_view_team_profilePageButton2);
        settingsPageButtons = findViewById(R.id.multiple_view_team_settingsPageButton2);
        createTeamPageButton = findViewById(R.id.multiple_view_team_createTeamBtn);
        joinTeamPageButton = findViewById(R.id.multiple_view_team_joinTeamBtn);
        recyclerView = findViewById(R.id.multipleTeamRecyclerView);

        RequestQueue queue = Volley.newRequestQueue(MultipleViewTeamActivity.this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_SERVER_URL + "/players/getCurrentPlayersTeams/" + playerID, null,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("team", response.toString());
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                long captainID = response.getJSONObject(i).getJSONObject("captain").getLong("player_id");
                                if(captainID == playerID) {
                                    createTeamPageButton.setEnabled(false);
                                }
                                String name = response.getJSONObject(i).getString("team_name");
                                long id = response.getJSONObject(i).getLong("team_id");
                                String location = response.getJSONObject(i).getString("location");
                                int playerCount = response.getJSONObject(i).getJSONArray("members").length();
                                teamList.add(new Team(name, id, location, playerCount, 0));
                                teamsPlayerIsOn.add(id);
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
                        Log.i("Team list error", error.toString());
                    }
                }

        );

        queue.add(request);


        homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MultipleViewTeamActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        profilePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MultipleViewTeamActivity.this, ProfileViewActivity.class);
                startActivity(intent);
            }
        });

        settingsPageButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MultipleViewTeamActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        joinTeamPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MultipleViewTeamActivity.this, JoinTeamActivity.class);
                startActivity(intent);
            }
        });

        createTeamPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MultipleViewTeamActivity.this, CreateTeamActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Updates the recycler view.
     */
    public void updateRecyclerView() {
        TeamAdapter adapter = new TeamAdapter(teamList, MultipleViewTeamActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
