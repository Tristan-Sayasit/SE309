package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.hasProfile;
import static com.se309.soccernexus.util.Const.numTeams;
import static com.se309.soccernexus.util.Const.playerID;
import static com.se309.soccernexus.util.Const.username;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Nathan Turnis
 * Class for displaying and handling logic in the Sign Up Page
 */
public class SignUpActivity extends AppCompatActivity {

    TextInputLayout emailInput;
    TextInputLayout usernameInput;
    TextInputLayout passwordInput;
    TextInputLayout confirmPassInput;
    Button signUpButton;
    ImageButton backButton;
    TextView warningTextView;
    View progressBg;
    ProgressBar progressBar;

    /**
     * Called when the activity is created. Initializes a listener on the required buttons.
     * Checks for form validation upon clicking the sign up button. When sign up is clicked,
     * a request is made to create a new account. Upon a successful response, the user is taken
     * to the Home page.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        emailInput = findViewById(R.id.signup_emailInput);
        usernameInput = findViewById(R.id.signup_usernameInput);
        passwordInput = findViewById(R.id.signup_passwordInput);
        confirmPassInput = findViewById(R.id.signup_confirmPasswordInput);
        signUpButton = findViewById(R.id.signUpButton);
        backButton = findViewById(R.id.signUp_backBtn);
        warningTextView = findViewById(R.id.warningTextSignup);
        progressBg = findViewById(R.id.signup_progressBg);
        progressBar = findViewById(R.id.signup_progressCircle);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if any input is blank, return
                if(emailInput.getEditText().getText().toString().isEmpty()
                    || usernameInput.getEditText().getText().toString().isEmpty()
                    || passwordInput.getEditText().getText().toString().isEmpty()
                    || confirmPassInput.getEditText().getText().toString().isEmpty()) {
                    warningTextView.setText("Some fields are blank!");
                    warningTextView.setAlpha(1);
                    return;
                }

                //if email address is not valid, return
                if(!Patterns.EMAIL_ADDRESS.matcher(emailInput.getEditText().getText().toString()).matches()) {
                    warningTextView.setText("Email is not valid!");
                    warningTextView.setAlpha(1);
                    emailInput.setError("Invalid Email");
                    return;
                }

                //if passwords do not match, return
                if(!confirmPassInput.getEditText().getText().toString().equals(passwordInput.getEditText().getText().toString())) {
                    warningTextView.setText("Passwords must match!");
                    warningTextView.setAlpha(1);
                    confirmPassInput.setError("Password does not match");
                    return;
                }

                //closes keyboard when sign up button is pressed
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(findViewById(R.id.signup_constraintLayout).getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //send sign up information
                RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);

                JSONObject body = new JSONObject();
                try {
                    body.put("username", usernameInput.getEditText().getText().toString());
                    body.put("password", passwordInput.getEditText().getText().toString());
                    body.put("email", emailInput.getEditText().getText().toString());
                    body.put("first_name", "");
                    body.put("last_name", "");
                    body.put("height", "");
                    body.put("weight", "");
                    body.put("birthday", "");
                    body.put("preferred_position", "");
                    body.put("has_profile", false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                progressBar.setVisibility(View.VISIBLE);
                progressBg.setVisibility(View.VISIBLE);

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,  BASE_SERVER_URL + "/players/signUp", body,
                        new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject response) {
                                Log.i("JSON Response", response.toString());
                                try {
                                    playerID = response.getLong("player_id");
                                    Log.i("user id", String.valueOf(playerID));
                                    username = response.getString("username");
                                    numTeams = 0;
                                    hasProfile = false;
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                                Intent intent = new Intent(SignUpActivity.this, ProfileFirstActivity.class);
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

                                //if error code is 400, username already exists
                                if(error.networkResponse.statusCode == 400) {
                                    warningTextView.setText("Username already exists!");
                                    warningTextView.setAlpha(1);
                                    usernameInput.setError("Username already exists");
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                                progressBg.setVisibility(View.INVISIBLE);
                            }
                        }
                );
                queue.add(request);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Called when the activity resumes. Clears loading, warning, and text fields.
     */
    @Override
    protected void onResume() {
        super.onResume();
        progressBg.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        warningTextView.setAlpha(0);
        emailInput.getEditText().getText().clear();
        emailInput.setError(null);
        usernameInput.getEditText().getText().clear();
        usernameInput.setError(null);
        passwordInput.getEditText().getText().clear();
        passwordInput.setError(null);
        confirmPassInput.getEditText().getText().clear();
        confirmPassInput.setError(null);
    }

}
