package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.hasProfile;
import static com.se309.soccernexus.util.Const.numTeams;
import static com.se309.soccernexus.util.Const.playerID;
import static com.se309.soccernexus.util.Const.username;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Nathan Turnis
 * Class for displaying and handling logic in the Logic page
 */
public class LoginActivity  extends AppCompatActivity {

    TextInputLayout usernameInput;
    TextInputLayout passwordInput;
    Button signUpButton;
    Button loginButton;
    TextView warningTextView;

    View progressBg;
    ProgressBar progressBar;

    /**
     * Called when the activity is created. Initializes a listener on the required buttons.
     * Performs form validation on the buttons. When the user clicks login, the method makes
     * a request asking to login. Upon a successful response, the user is taken to the HomeActivity.
     * If the server is down or the request fails, the user is notified.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        signUpButton = findViewById(R.id.signupButton);
        usernameInput = findViewById(R.id.login_usernameInput);
        passwordInput = findViewById(R.id.login_passwordInput);
        loginButton = findViewById(R.id.loginButton);
        warningTextView = findViewById(R.id.warningTextLogin);
        progressBg = findViewById(R.id.login_progressBg);
        progressBar = findViewById(R.id.login_progressCircle);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if inputs are blank, return
                if(usernameInput.getEditText().getText().toString().isEmpty()
                        || passwordInput.getEditText().getText().toString().isEmpty()) {
                    warningTextView.setText("Some fields are blank!");
                    warningTextView.setAlpha(1);
                    return;
                }

                //closes keyboard when login button is pressed
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(findViewById(R.id.login_constraintLayout).getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

                JSONObject body = new JSONObject();
                try {
                    body.put("username", usernameInput.getEditText().getText().toString());
                    body.put("password", passwordInput.getEditText().getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                progressBar.setVisibility(View.VISIBLE);
                progressBg.setVisibility(View.VISIBLE);

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,  BASE_SERVER_URL + "/players/login", body,

                        new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject response) {
                                Log.i("JSON Response", response.toString());
                                try {
                                    playerID = response.getLong("player_id");
                                    Log.i("user id", String.valueOf(playerID));
                                    username = response.getString("username");
                                    numTeams = response.getJSONArray("teams").length();
                                    hasProfile = response.getBoolean("has_profile");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("JSON Error", error.toString());

                                if(error.toString().equals("com.android.volley.TimeoutError")) {
                                    warningTextView.setText("Could not establish connection!");
                                    warningTextView.setAlpha(1);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    progressBg.setVisibility(View.INVISIBLE);
                                    return;
                                }

                                Log.i("JSON Error Code", String.valueOf(error.networkResponse.statusCode));

                                if(error.networkResponse.statusCode == 400 || error.networkResponse.statusCode == 401) {
                                    warningTextView.setText("Incorrect username or password");
                                    warningTextView.setAlpha(1);
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                                progressBg.setVisibility(View.INVISIBLE);
                            }
                        }
                );
                queue.add(request);
            }

        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Called when the activity resumes. Sets the progress, background, and warning text to
     * invisible. Clears the inputs.
     */
    @Override
    protected void onResume() {
        super.onResume();
        progressBg.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        warningTextView.setAlpha(0);
        usernameInput.getEditText().getText().clear();
        passwordInput.getEditText().getText().clear();
    }
}
