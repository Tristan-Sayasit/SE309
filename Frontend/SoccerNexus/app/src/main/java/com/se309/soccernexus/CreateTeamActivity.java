package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.numTeams;
import static com.se309.soccernexus.util.Const.playerID;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Tristan Sayasit
 * Class for displaying and handling logic in the Create Team Page
 */
public class CreateTeamActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    TextInputLayout teamName;
    Button createTeamButton;
    ImageButton create_team_profilePageButton;
    ImageButton create_team_teamPageButton;
    ImageButton create_team_homePageButton;
    ImageButton create_team_settingsPageButton;
    Spinner teamLocations;

    /**
     * Called when the activity is created. Initializes a listener on the required buttons and
     * sets up the adapter for the dropdown spinner.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_team_activity);

        teamName = findViewById(R.id.teamName);
        createTeamButton = findViewById(R.id.createTeamButton);
        teamLocations = findViewById(R.id.teamLocations);

        create_team_profilePageButton = findViewById(R.id.create_team_profilePageButton);
        create_team_teamPageButton = findViewById(R.id.create_team_teamPageButton);
        create_team_homePageButton = findViewById(R.id.create_team_homePageButton);
        create_team_settingsPageButton = findViewById(R.id.create_team_settingsPageButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.teamNames, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamLocations.setAdapter(adapter);
        teamLocations.setOnItemSelectedListener(this);

        create_team_profilePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doesPlayerHaveProfile();
            }
        });

        create_team_teamPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateTeamActivity.this, MultipleViewTeamActivity.class);
                startActivity(intent);
            }
        });

        create_team_homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateTeamActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        create_team_settingsPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateTeamActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        }); {

        }

        createTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(CreateTeamActivity.this);
                JSONObject body = new JSONObject();
                try {
                    body.put("team_name", teamName.getEditText().getText().toString());
                    body.put("location", teamLocations.getSelectedItem().toString());
                    JSONObject body2 = new JSONObject();
                    body2.put("player_id", playerID);
                    body.put("captain", body2);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_SERVER_URL + "/teams/createTeam", body,
                        new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject response) {
                                Log.i("JSON Response", response.toString());
                                Intent intent = new Intent(CreateTeamActivity.this, ViewTeamActivity.class);
                                try {
                                    intent.putExtra("teamid", response.getLong("team_id"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                startActivity(intent);
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("JSON Error", error.toString());
                                if(error.networkResponse.statusCode == 400) {
                                    Intent intent = new Intent(CreateTeamActivity.this, ProfileFirstActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                );
                queue.add(request);

            }

            //teamName.getEditText().getText().clear();
        });
    }

    /**
     * Makes a request to the backend checking whether or not the player has a profile.
     * If user has a profile, it will take them to the profile view page.
     * If user does not have a profile, it will take them to the page indicating they
     * need to make a profile first.
     */
    private void doesPlayerHaveProfile() {
        RequestQueue queue = Volley.newRequestQueue(CreateTeamActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_SERVER_URL + "/players/getProfile/" + playerID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(CreateTeamActivity.this, ProfileViewActivity.class);
                        startActivity(intent);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error response", String.valueOf(error.networkResponse.statusCode));
                        if(error.networkResponse.statusCode == 404) {
                            Intent intent = new Intent(CreateTeamActivity.this, ProfileFirstActivity.class);
                            startActivity(intent);
                        }
                    }
                }

        );
        queue.add(request);
    }

    /**
     * Called when the user selects an item on the dropdown. Sets the selectedLocation variable
     * to the dropdown item.
     * @param adapterView The AdapterView where the selection happened
     * @param view The view within the AdapterView that was clicked
     * @param i The position of the view in the adapter
     * @param l The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedLocation = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, selectedLocation, Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when nothing is selected in the dropdown. Method is required but empty.
     * @param adapterView The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
