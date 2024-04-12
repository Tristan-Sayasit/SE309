package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.teamsPlayerIsOn;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
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

import androidx.annotation.NonNull;
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
import com.se309.soccernexus.util.recyclerviews.Team;
import com.se309.soccernexus.util.recyclerviews.TeamAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

/**
 * @author Nathan Turnis
 * Class for displaying and handling logic in the Pickup Match Challenge page
 */
public class PickupMatchChallengeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button modalCancelBtn, modalChallengeBtn, requestsBtn;
    ImageButton backBtn;
    View bg;
    ConstraintLayout modalLayout;
    TextView modalTitleTxt, warningTxt;

    TextInputLayout locationInput, dayInput, monthInput, yearInput, timeInput;

    Spinner formatSpinner;

    RecyclerView recyclerView;

    ArrayList<Team> teamsList = new ArrayList<>();

    LocalDateTime date;

    long challengingID, acceptingID;

    /**
     * Called when the activity is created. Initializes a listener on the required buttons.
     * Gets the teamID of the team which will be challenging another team.
     * Makes a request for a list of all the teams in the backend. Filters out the teams
     * the captain is alreayd on and updates the recycler view.
     *
     * When the user has clicked a challenge button, a modal is opened with details about
     * the pickup match. Upon filling out the correct information and clicking challenge,
     * a request is made to challenge the team.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickup_match_challenge_activity);

        backBtn = findViewById(R.id.pickup_backBtn);
        requestsBtn = findViewById(R.id.pickup_requestsBtn);

        bg = findViewById(R.id.pickup_graybg);
        modalLayout = findViewById(R.id.pickup_modalLayout);
        modalCancelBtn = findViewById(R.id.pickup_modalCancelBtn);
        modalChallengeBtn = findViewById(R.id.pickup_challengeBtn);
        formatSpinner = findViewById(R.id.pickup_formatSpinner);
        recyclerView = findViewById(R.id.pickup_recyclerView);

        modalTitleTxt = findViewById(R.id.pickup_modalTitle);
        warningTxt = findViewById(R.id.pickup_warning);
        locationInput = findViewById(R.id.pickup_locationInput);
        dayInput = findViewById(R.id.pickup_dayInput);
        monthInput = findViewById(R.id.pickup_monthInput);
        yearInput = findViewById(R.id.pickup_yearInput);
        timeInput = findViewById(R.id.pickup_timeHourInput);

        warningTxt.setVisibility(View.INVISIBLE);

        bg.setVisibility(View.INVISIBLE);
        modalLayout.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        challengingID = extras.getLong("ourTeam");

        ArrayAdapter<CharSequence> formatAdapter = ArrayAdapter.createFromResource(this, R.array.pickupMatchFormats, android.R.layout.simple_spinner_item);
        formatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        formatSpinner.setAdapter(formatAdapter);
        formatSpinner.setOnItemSelectedListener(this);

        RequestQueue queue = Volley.newRequestQueue(PickupMatchChallengeActivity.this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_SERVER_URL + "/teams/getAllTeams", null,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("pickup response", response.toString());
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                String name = response.getJSONObject(i).getString("team_name");
                                long id = response.getJSONObject(i).getLong("team_id");
                                String location = response.getJSONObject(i).getString("location");
                                int playerCount = response.getJSONObject(i).getJSONArray("members").length();
                                teamsList.add(new Team(name, id, location, playerCount, 2));
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
                        Log.i("pickup error", error.toString());
                    }
                }

        );

        queue.add(request);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PickupMatchChallengeActivity.this, ViewTeamActivity.class);
                intent.putExtra("teamid", challengingID);
                startActivity(intent);
            }
        });

        modalChallengeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(formatSpinner.getSelectedItem().toString().equals("Select a format...")){
                    warningTxt.setText("Incorrect input!");
                    warningTxt.setVisibility(View.VISIBLE);
                    return;
                }

                try {

                    if(timeInput.getEditText().getText().toString().length() == 3) {
                        timeInput.getEditText().setText("0"+ timeInput.getEditText().getText().toString());
                    }

                    int day = Integer.parseInt(dayInput.getEditText().getText().toString());
                    int year = Integer.parseInt(yearInput.getEditText().getText().toString());
                    Month month = Month.of(Integer.parseInt(monthInput.getEditText().getText().toString()));
                    int hour = Integer.parseInt(timeInput.getEditText().getText().toString().substring(0, 2));
                    int min = Integer.parseInt(timeInput.getEditText().getText().toString().substring(2, 4));

                    date = LocalDateTime.of(year, month, day, hour, min);
                    Log.i("date success", date.toString());
                } catch (Exception e) {
                    warningTxt.setText("Incorrect input!");
                    warningTxt.setVisibility(View.VISIBLE);
                    return;
                }

                JSONObject body = new JSONObject();
                try {
                    body.put("challenging_id", challengingID);
                    body.put("accepting_id", acceptingID);
                    body.put("location", locationInput.getEditText().getText().toString());
                    body.put("match_format", formatSpinner.getSelectedItem().toString());
                    body.put("time_of_match", date.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(PickupMatchChallengeActivity.this);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_SERVER_URL + "/matches/challengeTeam", body,

                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("challenge response", response.toString());
                                closeModal();
                                warningTxt.setText("Warning!");
                                warningTxt.setVisibility(View.INVISIBLE);
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


        modalCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeModal();
            }
        });

        requestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PickupMatchChallengeActivity.this, PickupMatchRequestsActivity.class);
                intent.putExtra("teamid", challengingID);
                startActivity(intent);
            }
        });

    }

    /**
     * Opens the modal which details the information the user will enter to create a pickup match.
     * @param teamName The name of the team being challenged
     * @param teamID The id of the team being challenged
     */
    public void openModal(String teamName, long teamID) {
        acceptingID = teamID;
        modalTitleTxt.setText("Challenge " + teamName);

        backBtn.setEnabled(false);

        bg.setVisibility(View.VISIBLE);
        modalLayout.setVisibility(View.VISIBLE);

        ObjectAnimator fadedBgAnimator = ObjectAnimator.ofFloat(bg, "alpha", 0f, .75f);
        ObjectAnimator modalFadeAnimator = ObjectAnimator.ofFloat(modalLayout, "alpha", 0f, 1f);
        fadedBgAnimator.setDuration(180);
        modalFadeAnimator.setDuration(180);
        fadedBgAnimator.start();
        modalFadeAnimator.start();
    }

    /**
     * Closes the modal and clears the fields.
     */
    public void closeModal() {
        acceptingID = -1;

        backBtn.setEnabled(true);

        ObjectAnimator fadedBgAnimator = ObjectAnimator.ofFloat(bg, "alpha", .75f, .0f);
        ObjectAnimator modalFadeAnimator = ObjectAnimator.ofFloat(modalLayout, "alpha", 1f, 0f);
        fadedBgAnimator.setDuration(180);
        modalFadeAnimator.setDuration(180);
        fadedBgAnimator.start();
        modalFadeAnimator.start();

        modalFadeAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                bg.setVisibility(View.INVISIBLE);
                modalLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });

    }

    /**
     * Updates the recycler view.
     */
    public void updateRecyclerView() {
        teamsList.removeIf(x -> teamsPlayerIsOn.stream().anyMatch(y -> x.getId() == y));
        TeamAdapter adapter = new TeamAdapter(teamsList, PickupMatchChallengeActivity.this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    /**
     * Called when the user selectes an item in the dropdown.
     * @param parent The AdapterView where the selection happened
     * @param view The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, selectedItem, Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when nothing is selected in the dropdown. Will set dropdown to "Select..."
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Select...", Toast.LENGTH_SHORT).show();
    }
}
