package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.playerID;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.textfield.TextInputLayout;
import com.se309.soccernexus.util.recyclerviews.Player;
import com.se309.soccernexus.util.recyclerviews.PlayerListAdapter;
import com.se309.soccernexus.util.recyclerviews.RemovePlayerListAdapter;
import com.se309.soccernexus.util.recyclerviews.User;
import com.se309.soccernexus.util.recyclerviews.removePlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditTeamActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageButton backBtn;
    Button saveNameBtn, saveLocationBtn;
    TextInputLayout newTeamName;
    Spinner newTeamLocations;
    TextView playerCount;
    RecyclerView recyclerView;
    private ArrayList<removePlayer> removePlayerList;

    long teamID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_team_activity);

        backBtn = findViewById(R.id.edit_team_backBtn);
        saveNameBtn = findViewById(R.id.saveTeamNameBtn);
        newTeamLocations = findViewById(R.id.newTeamLocations);
        playerCount = findViewById(R.id.updatedPlayerCount);
        saveLocationBtn = findViewById(R.id.saveTeamLocationBtn);
        newTeamName = findViewById(R.id.newTeamName);
        recyclerView = findViewById(R.id.removePlayersRecyclerView);
        removePlayerList = new ArrayList<>();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.teamNames, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newTeamLocations.setAdapter(adapter);
        newTeamLocations.setOnItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();
        teamID = extras.getLong("teamid");
        Log.i("team id extra", String.valueOf(teamID));

        RequestQueue queue = Volley.newRequestQueue(EditTeamActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_SERVER_URL + "/teams/getTeam/" + teamID, null,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {

                        try {
                            long captainID = response.getJSONObject("captain").getLong("player_id");

                            Log.i("view response", response.toString());

                            for(int i = 0; i < response.getJSONArray("members").length(); i++) {
                                if(response.getJSONArray("members").getJSONObject(i).getLong("player_id") == captainID) {
                                    continue;
                                }
                                String firstName1 = response.getJSONArray("members").getJSONObject(i).getString("first_name");
                                String lastName1 = response.getJSONArray("members").getJSONObject(i).getString("last_name");
                                String position1 = response.getJSONArray("members").getJSONObject(i).getString("preferred_position");
                                long player_id = response.getJSONArray("members").getJSONObject(i).getLong("player_id");
                                removePlayerList.add(new removePlayer(firstName1 + " " + lastName1, position1, player_id, teamID));
                            }
                            playerCount.setText(removePlayerList.size() + "/12");

                            RemovePlayerListAdapter adapter = new RemovePlayerListAdapter(removePlayerList);
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

        saveNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNameRequest();
                newTeamName.getEditText().setText("");
            }
        });

        saveLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLocationRequest();
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTeamActivity.this, ViewTeamActivity.class);
                intent.putExtra("teamid", teamID);
                startActivity(intent);
            }
        });
    }

    private void saveNameRequest() {
        RequestQueue queue = Volley.newRequestQueue(EditTeamActivity.this);
        JSONObject body = new JSONObject();
        try {
            body.put("team_id", teamID);
            body.put("team_name", newTeamName.getEditText().getText().toString());
        } catch (Exception E) {
            E.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, BASE_SERVER_URL + "/teams/changeTeamName", body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("good response", response.toString());
                        saveNameBtn.setEnabled(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("bad response", error.toString());
            }
        });
        queue.add(request);
    }

    private void saveLocationRequest() {
        RequestQueue queue = Volley.newRequestQueue(EditTeamActivity.this);
        JSONObject body = new JSONObject();
        try {
            body.put("team_id", teamID);
            body.put("location", newTeamLocations.getSelectedItem().toString());
        } catch (Exception E) {
            E.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, BASE_SERVER_URL + "/teams/changeTeamLocation", body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("good response", response.toString());
                        saveLocationBtn.setEnabled(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("bad response", error.toString());
            }
        });
        queue.add(request);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedLocation = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, selectedLocation, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
