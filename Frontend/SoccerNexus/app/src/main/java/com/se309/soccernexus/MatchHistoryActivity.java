package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.se309.soccernexus.util.recyclerviews.Match;
import com.se309.soccernexus.util.recyclerviews.MatchAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;

public class MatchHistoryActivity extends AppCompatActivity {

    ImageButton backButton;
    RecyclerView pastMatches;
    private ArrayList<Match> matchList;

    long teamID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_history_activity);

        Bundle extras = getIntent().getExtras();
        teamID = extras.getLong("teamid");

        backButton = findViewById(R.id.match_history_backBtn);
        pastMatches = findViewById(R.id.matchHistoryRecyclerView);

        matchList = new ArrayList<>();

        getMatchHistory();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MatchHistoryActivity.this, ViewTeamActivity.class);
                intent.putExtra("teamid", teamID);
                startActivity(intent);
            }

        });

//        LocalDateTime date1 = LocalDateTime.now();
//
//        date1 = LocalDateTime.of(2023, Month.NOVEMBER, 28, 21, 0);
//        matchList.add(new Match("Vikings", "Minnesota", date1, 0));
//        matchList.add(new Match("Packers", "Wisconsin", date1, 0));
//        matchList.add(new Match("Lions", "Michigan", date1, 0));
    }

    private void getMatchHistory() {
        RequestQueue queue = Volley.newRequestQueue(MatchHistoryActivity.this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_SERVER_URL + "/matches/getMatchHistory/" + teamID, null,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Match History request", response.toString());
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                long matchID = response.getJSONObject(i).getLong("pickup_match_id");
                                String teamName = response.getJSONObject(i).getJSONObject("challenging_team").getString("team_name");
                                String location = response.getJSONObject(i).getString("location");
                                LocalDateTime date = LocalDateTime.parse(response.getJSONObject(i).getString("time_of_match"));
                                matchList.add(new Match(teamName, location, date, matchID, teamID));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Collections.reverse(matchList);
                        updateMatchesRecyclerView();
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

    public void updateMatchesRecyclerView() {
        MatchAdapter adapter = new MatchAdapter(matchList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        pastMatches.setNestedScrollingEnabled(false);
        pastMatches.setLayoutManager(layoutManager);
        pastMatches.setItemAnimator(new DefaultItemAnimator());
        pastMatches.setAdapter(adapter);
    }
}
