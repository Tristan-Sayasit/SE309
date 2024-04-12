package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.android.volley.toolbox.Volley;
import com.se309.soccernexus.util.recyclerviews.Match;
import com.se309.soccernexus.util.recyclerviews.MatchRequestsAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

public class PickupMatchRequestsActivity extends AppCompatActivity {

    ImageButton backBtn;
    RecyclerView recyclerView;

    ArrayList<Match> matchList = new ArrayList<>();

    long teamID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_requests_activity);

        backBtn = findViewById(R.id.matchRequest_backBtn);
        recyclerView = findViewById(R.id.matchRequest_recyclerView);

        Bundle extras = getIntent().getExtras();
        teamID = extras.getLong("teamid");

        getMatchRequests();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PickupMatchRequestsActivity.this, PickupMatchChallengeActivity.class);
                intent.putExtra("ourTeam", teamID);
                startActivity(intent);
            }
        });

    }

    public void getMatchRequests() {

        RequestQueue queue = Volley.newRequestQueue(PickupMatchRequestsActivity.this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_SERVER_URL + "/matches/getChallenges/" + teamID, null,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("match requests", response.toString());

                        for(int i = 0; i < response.length(); i++) {
                            try {
                                long matchID = response.getJSONObject(i).getLong("pickup_match_id");
                                String teamName = response.getJSONObject(i).getJSONObject("challenging_team").getString("team_name");
                                String location = response.getJSONObject(i).getString("location");
                                String matchTimeS = response.getJSONObject(i).getString("time_of_match");
                                LocalDateTime matchDate = LocalDateTime.parse(matchTimeS);

                                matchList.add(new Match(teamName, location, matchDate, matchID));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        updateRecyclerView();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Remove button error", error.toString());
                    }
                }

        );

        queue.add(request);
    }

    private void updateRecyclerView() {
        MatchRequestsAdapter adapter = new MatchRequestsAdapter(matchList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

}
