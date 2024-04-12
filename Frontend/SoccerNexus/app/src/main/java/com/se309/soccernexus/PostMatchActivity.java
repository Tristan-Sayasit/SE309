package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.playerID;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.se309.soccernexus.util.recyclerviews.MatchAdapter;
import com.se309.soccernexus.util.recyclerviews.Player;
import com.se309.soccernexus.util.recyclerviews.PostMatchPlayer;
import com.se309.soccernexus.util.recyclerviews.PostMatchPlayerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PostMatchActivity extends AppCompatActivity {

    long teamID;
    long matchID;
    Button submit;
    ImageButton backBtn;
    RecyclerView playerForm;
    private ArrayList<PostMatchPlayer> postMatchPlayer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_match_stats);

        submit = findViewById(R.id.submitFormBtn);
        playerForm = findViewById(R.id.postMatchStatsRecyclerView);
        backBtn = findViewById(R.id.pickupview_backBtn2);
        Bundle extras = getIntent().getExtras();
        teamID = extras.getLong("teamid");
        matchID = extras.getLong("matchid");

        postMatchPlayer = new ArrayList<>();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject body = new JSONObject();
                JSONArray goalsScored = new JSONArray();
                for (int i = 0; i < playerForm.getChildCount(); i++) {
                    PostMatchPlayerAdapter.PostMatchViewHolder item = (PostMatchPlayerAdapter.PostMatchViewHolder) playerForm.findViewHolderForAdapterPosition(i);
                    JSONObject goal = new JSONObject();
                    try {
                        goal.put("player_id", postMatchPlayer.get(i).playerID);
                        goal.put("number_of_goals", Integer.parseInt(item.goals_scored.getEditText().getText().toString()));
                        goal.put("assists_made", Integer.parseInt(item.assists_made.getEditText().getText().toString()));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    goalsScored.put(goal);
                }
                try {
                    body.put("match_id", matchID);
                    body.put("team_id", teamID);
                    body.put("goals_scored", goalsScored);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                RequestQueue queue = Volley.newRequestQueue(PostMatchActivity.this);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, BASE_SERVER_URL + "/matches/submitMatchReport", body,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                finish();
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
        });

        RequestQueue queue = Volley.newRequestQueue(PostMatchActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_SERVER_URL + "/teams/getTeam/" + teamID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("postMatchResponse", response.toString());
                        try {
                            JSONArray members = response.getJSONArray("members");
                            for (int i = 0; i < members.length(); i++) {
                                String name = members.getJSONObject(i).getString("first_name") + " " + members.getJSONObject(i).getString("last_name");
                                long id = members.getJSONObject(i).getLong("player_id");
                                PostMatchPlayer player = new PostMatchPlayer(name, id, 0, 0);
                                postMatchPlayer.add(player);
                            }
                            updatePostStatsRecyclerView();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
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

    public void updatePostStatsRecyclerView() {
        PostMatchPlayerAdapter adapter = new PostMatchPlayerAdapter(postMatchPlayer);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        playerForm.setNestedScrollingEnabled(false);
        playerForm.setLayoutManager(layoutManager);
        playerForm.setItemAnimator(new DefaultItemAnimator());
        playerForm.setAdapter(adapter);
    }
}
