package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.playerID;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.se309.soccernexus.util.recyclerviews.Player;
import com.se309.soccernexus.util.recyclerviews.PlayerListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PickupMatchViewActivity extends AppCompatActivity {

    TextView vsTitleText, locationTxt, timeText, matchFormatTxt, team1Txt, team2Txt, matchScoreTxt, matchCompletedTxt;
    ImageButton backBtn;
    Button formBtn;
    RecyclerView rosterRecycler1, rosterRecycler2;
    private ArrayList<Player> playerList1 = new ArrayList<>();
    private ArrayList<Player> playerList2 = new ArrayList<>();
    long matchID;
    long teamID;
    boolean isFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickup_match_view_activity);

        vsTitleText = findViewById(R.id.pickupview_vsText);
        locationTxt = findViewById(R.id.pickupview_locationTxt);
        timeText = findViewById(R.id.pickupview_timeTxt);
        matchFormatTxt = findViewById(R.id.pickupview_formatTxt);
        team1Txt = findViewById(R.id.pickupview_team1RosterTxt);
        team2Txt = findViewById(R.id.pickupview_team2RosterTxt);
        backBtn = findViewById(R.id.pickupview_backBtn);
        matchScoreTxt = findViewById(R.id.pickupview_matchScoreTxt);
        formBtn = findViewById(R.id.pickupview_formBtn);
        matchCompletedTxt = findViewById(R.id.pickupview_matchCompletedTxt);

        rosterRecycler1 = findViewById(R.id.pickupview_rosterRecycler1);
        rosterRecycler2 = findViewById(R.id.pickupview_rosterRecycler2);

        Bundle extras = getIntent().getExtras();
        Log.i("intent started activity", getIntent().toString());
        matchID = extras.getLong("matchid");
        teamID = extras.getLong("teamid");

        getMatchDetails();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        formBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!isFinished) {
                        completeMatch();
                    } else {
                        submitPostMatchRep();
                    }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
            }
        });

    }

    private void getMatchDetails() {
        RequestQueue queue = Volley.newRequestQueue(PickupMatchViewActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_SERVER_URL + "/matches/getMatchInfo/" + matchID, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("match info response", response.toString());
                        try {

                            int goals1 = response.getInt("challenging_team_goals");
                            int goals2 = response.getInt("accepting_team_goals");
                            matchScoreTxt.setText(goals1 + " - " + goals2);

                            locationTxt.setText(response.getString("location"));
                            matchFormatTxt.setText("Match Format: " + response.getString("format"));
                            LocalDateTime matchDate = LocalDateTime.parse(response.getString("match_time"));

                            int regularHour;
                            String period;
                            int militaryHour = matchDate.getHour();

                            if (militaryHour == 0) {
                                regularHour = 12;
                                period = "AM";
                            } else if (militaryHour == 12) {
                                regularHour = 12;
                                period = "PM";
                            } else if (militaryHour < 12) {
                                regularHour = militaryHour;
                                period = "AM";
                            } else {
                                regularHour = militaryHour - 12;
                                period = "PM";
                            }

                            String min;
                            if(matchDate.getMinute() < 10) {
                                min = "0" + matchDate.getMinute();
                            }  else {
                                min = String.valueOf(matchDate.getMinute());
                            }

                            String matchDayOfWeek = matchDate.getDayOfWeek().toString().toLowerCase();
                            matchDayOfWeek = matchDayOfWeek.substring(0, 1).toUpperCase() + matchDayOfWeek.substring(1);
                            String matchMonth = matchDate.getMonth().toString().toLowerCase();
                            matchMonth =  matchMonth.substring(0, 1).toUpperCase() + matchMonth.substring(1);

                            timeText.setText(matchDayOfWeek + ", " + matchMonth + " " + matchDate.getDayOfMonth()
                            + ", " + regularHour + ":" + min + " " + period);

                            JSONObject challengingTeam = response.getJSONObject("challenging_team");
                            JSONObject acceptingTeam = response.getJSONObject("accepting_team");
                            String challengingTeamName = challengingTeam.getString("team_name");
                            String acceptingTeamName = acceptingTeam.getString("team_name");

                            vsTitleText.setText(challengingTeamName + " vs " + acceptingTeamName);
                            team1Txt.setText(challengingTeamName);
                            team2Txt.setText(acceptingTeamName);

                            if(challengingTeam.getJSONObject("captain").getLong("player_id") != playerID &&
                            acceptingTeam.getJSONObject("captain").getLong("player_id") != playerID) {
                                formBtn.setEnabled(false);
                            }

                            String captainName1 = challengingTeam.getJSONObject("captain").getString("player_name");
                            String captainPos1 = challengingTeam.getJSONObject("captain").getString("player_position");
                            playerList1.add(new Player(captainName1, captainPos1, true));

                            String captainName2 = acceptingTeam.getJSONObject("captain").getString("player_name");
                            String captainPos2 = acceptingTeam.getJSONObject("captain").getString("player_position");
                            playerList2.add(new Player(captainName2, captainPos2, true));

                            for(int i = 1; i < challengingTeam.getJSONArray("players").length(); i++) {
                                String playerName1 = challengingTeam.getJSONArray("players").getJSONObject(i).getString("player_name");
                                String playerPos1 = challengingTeam.getJSONArray("players").getJSONObject(i).getString("player_position");
                                playerList1.add(new Player(playerName1, playerPos1, false));
                            }

                            for(int i = 1; i < acceptingTeam.getJSONArray("players").length(); i++) {
                                String playerName2 = acceptingTeam.getJSONArray("players").getJSONObject(i).getString("player_name");
                                String playerPos2 = acceptingTeam.getJSONArray("players").getJSONObject(i).getString("player_position");
                                playerList2.add(new Player(playerName2, playerPos2, false));
                            }
                            if (response.getBoolean("is_finished")) {
                                formBtn.setText("Post Match Report");
                                matchCompletedTxt.setVisibility(View.VISIBLE);
                            }
                            isFinished = response.getBoolean("is_finished");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        updateRoster1Recycler();
                        updateRoster2Recycler();
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

    private void completeMatch() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("pickup_match_id", matchID);
        RequestQueue queue = Volley.newRequestQueue(PickupMatchViewActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, BASE_SERVER_URL + "/matches/finishMatch", body,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("complete match response", response.toString());
                        formBtn.setText("Post Match Report");
                        matchCompletedTxt.setVisibility(View.VISIBLE);
                        isFinished = true;
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

    private void submitPostMatchRep() {
        Intent intent = new Intent(PickupMatchViewActivity.this, PostMatchActivity.class);
        intent.putExtra("teamid", teamID);
        intent.putExtra("matchid", matchID);
        startActivity(intent);
    }

    private void updateRoster1Recycler() {
        PlayerListAdapter adapter = new PlayerListAdapter(playerList1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rosterRecycler1.setLayoutManager(layoutManager);
        rosterRecycler1.setNestedScrollingEnabled(false);
        rosterRecycler1.setItemAnimator(new DefaultItemAnimator());
        rosterRecycler1.setAdapter(adapter);
    }

    private void updateRoster2Recycler() {
        PlayerListAdapter adapter = new PlayerListAdapter(playerList2);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rosterRecycler2.setLayoutManager(layoutManager);
        rosterRecycler2.setNestedScrollingEnabled(false);
        rosterRecycler2.setItemAnimator(new DefaultItemAnimator());
        rosterRecycler2.setAdapter(adapter);
    }

}
