package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.hasProfile;
import static com.se309.soccernexus.util.Const.numTeams;
import static com.se309.soccernexus.util.Const.playerID;
import static com.se309.soccernexus.util.Const.teamsPlayerIsOn;
import static com.se309.soccernexus.util.Const.username;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Calendar;

/**
 * @author Tristan Sayasit
 * Class for displaying and handling logic in the Settings Page
 */
public class SettingsActivity extends AppCompatActivity {

    ImageButton settings_homePageButton;
    ImageButton settings_teamPageButton;
    ImageButton settings_profilePageButton;
    ImageButton settings_settingsPageButton;
    Button settings_logOutBtn;
    Button saveEmail;
    Button savePassword;
    TextInputLayout emailInput, passwordInput;

    /**
     * Called when the activity is created. Initializes a listener on the required buttons.
     * When the user clicks logout, it sends them back to the LoginActivity and clears all the
     * global variables in Const
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        settings_homePageButton = findViewById(R.id.settings_homePageButton);
        settings_teamPageButton = findViewById(R.id.settings_teamPageButton);
        settings_profilePageButton = findViewById(R.id.settings_profilePageButton);
        settings_settingsPageButton = findViewById(R.id.settings_settingsPageButton);
        settings_logOutBtn = findViewById(R.id.settings_logOutBtn);
        emailInput = findViewById(R.id.settings_emailInput);
        passwordInput = findViewById(R.id.settings_passwordInput);
        saveEmail = findViewById(R.id.settings_saveEmailBtn);
        savePassword = findViewById(R.id.settings_savePasswordBtn);

        settings_homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        settings_teamPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MultipleViewTeamActivity.class);
                startActivity(intent);
            }
        });

        settings_profilePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doesPlayerHaveProfile();
            }
        });

        settings_settingsPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        settings_logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerID = -1;
                username = null;
                numTeams =  -1;
                hasProfile = false;
                teamsPlayerIsOn.clear();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        saveEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestEmail();
                emailInput.getEditText().setText("");
            }
        });

        savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestPassword();
                passwordInput.getEditText().setText("");
            }
        });
    }

    /**
     * Sends a request to update the user's email.
     */
    private void sendRequestEmail() {
        RequestQueue queue = Volley.newRequestQueue(SettingsActivity.this);
        JsonObjectRequest request;

        JSONObject body = new JSONObject();
        try {
            body.put("player_id", playerID);
            body.put("email", emailInput.getEditText().getText().toString());
        } catch (Exception E) {
            E.printStackTrace();
        }
        request = new JsonObjectRequest(Request.Method.PUT, BASE_SERVER_URL + "/players/updateEmail", body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("good response", response.toString());
                        saveEmail.setEnabled(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("bad response", error.toString());
                    }
                }
        );
        queue.add(request);
    }

    /**
     * Sends a request to update the user's password.
     */
    private void sendRequestPassword() {
        RequestQueue queue = Volley.newRequestQueue(SettingsActivity.this);
        JsonObjectRequest request;
        JSONObject body = new JSONObject();

        try {
            body.put("player_id", playerID);
            body.put("password", passwordInput.getEditText().getText().toString());
        } catch (Exception E) {
            E.printStackTrace();
        }
        request = new JsonObjectRequest(Request.Method.PUT, BASE_SERVER_URL + "/players/updatePassword", body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("good response", response.toString());
                        savePassword.setEnabled(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("bad response", error.toString());
                    }
                }
        );
        queue.add(request);
    }

    /**
     * Called when attempting to go to the profile page. If the user has a profile, it will go the
     * ProfileViewActivity, if not, it will go to ProfileFirstActivity.
     */
    private void doesPlayerHaveProfile() {
        RequestQueue queue = Volley.newRequestQueue(SettingsActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_SERVER_URL + "/players/getProfile/" + playerID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.i("has_profile", String.valueOf(response.getBoolean("has_profile")));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        try {
                            if(response.getBoolean("has_profile")) {
                                Intent intent = new Intent(SettingsActivity.this, ProfileViewActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(SettingsActivity.this, ProfileFirstActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error response", String.valueOf(error.networkResponse.statusCode));
                        if(error.networkResponse.statusCode == 404) {
                            Log.i("error 404", "profile not found");
                        }
                    }
                }
        );
        queue.add(request);
    }
}
