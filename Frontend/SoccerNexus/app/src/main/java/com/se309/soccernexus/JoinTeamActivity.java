package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.playerID;
import static com.se309.soccernexus.util.Const.teamsPlayerIsOn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Tristan Sayasit
 * Class for displaying and handling logic in the Join Team Page
 */
public class JoinTeamActivity extends AppCompatActivity {

    ImageButton backBtn;
    ImageButton join_team_profilePageButton;
    ImageButton join_team_teamPageButton;
    ImageButton join_team_homePageButton;
    ImageButton join_team_settingsPageButton;
    ArrayList<Team> teamsList = new ArrayList<>();
    RecyclerView recyclerView;

    /**
     * Called when the activity is created. Initializes a listener on the required buttons.
     * Makes a request to get a list of all the teams in the database. Displays these teams
     * via the recycler view.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_team_activity);

        backBtn = findViewById(R.id.join_team_backBtn);
        join_team_profilePageButton = findViewById(R.id.join_team_profilePageButton);
        join_team_teamPageButton = findViewById(R.id.join_team_teamPageButton);
        join_team_homePageButton = findViewById(R.id.join_team_homePageButton);
        join_team_settingsPageButton = findViewById(R.id.join_team_settingsPageButton);
        recyclerView = findViewById(R.id.join_team_recyclerView);

        RequestQueue queue = Volley.newRequestQueue(JoinTeamActivity.this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_SERVER_URL + "/teams/getAllTeams", null,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("join", response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                String name = response.getJSONObject(i).getString("team_name");
                                long id = response.getJSONObject(i).getLong("team_id");
                                String location = response.getJSONObject(i).getString("location");
                                int playerCount = response.getJSONObject(i).getJSONArray("members").length();
                                teamsList.add(new Team(name, id, location, playerCount, 1));
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

        join_team_profilePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinTeamActivity.this, ProfileViewActivity.class);
                startActivity(intent);
            }
        });
        join_team_teamPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JoinTeamActivity.this, MultipleViewTeamActivity.class);
                startActivity(intent);
            }
        });
        join_team_homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JoinTeamActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        join_team_settingsPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinTeamActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinTeamActivity.this, MultipleViewTeamActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Updates the recycler view.
     */
    public void updateRecyclerView() {
        teamsList.removeIf(x -> teamsPlayerIsOn.stream().anyMatch(y -> x.getId() == y));
        TeamAdapter adapter = new TeamAdapter(teamsList, JoinTeamActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
