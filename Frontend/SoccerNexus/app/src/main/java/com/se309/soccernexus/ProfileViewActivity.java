package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.*;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;

/**
 * @author Nathan Turnis
 * Class for displaying and handling logic in the Profile View Page
 */
public class ProfileViewActivity extends AppCompatActivity {

    ImageButton homeButton;
    ImageButton teamButton;
    ImageButton settingsButton;
    ImageButton profileEditBtn;

    //main user info
    TextView fullNameText, usernameText, positionText, friendsText;
    ImageView profilePicture;
    //characteristics
    TextView heightText, weightText, birthdayText;
    //stats
    TextView preferredPosText, goalsText, assistsText, gamesPlayedText, goalsPerGameText, assistsPerGameText, yellowCardsText, redCardsText;
    //contact
    TextView emailText;
    JSONObject jsonObject;

    View progressBg;
    ProgressBar progressBar;

    /**
     * Called when the activity is created. Initializes a listener on the required buttons.
     * Calls requestPlayerInfo().
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view_activity);

        Log.i("all good", "all good");

        homeButton = findViewById(R.id.profile_homePageButton);
        teamButton = findViewById(R.id.profile_teamPageButton);
        settingsButton = findViewById(R.id.profile_settingsPageButton);
        profileEditBtn = findViewById(R.id.profile_editProfileBtn);
        fullNameText = findViewById(R.id.profile_fullnameText);
        usernameText = findViewById(R.id.profile_usernameText);
        positionText = findViewById(R.id.profile_mainPosText);
        friendsText = findViewById(R.id.profile_friendsTxt);
        profilePicture = findViewById(R.id.profile_profilePictureView);
        heightText = findViewById(R.id.profile_heightText);
        weightText = findViewById(R.id.profile_weightText);
        birthdayText = findViewById(R.id.profile_birthdayText);
        preferredPosText = findViewById(R.id.profile_positionText);
        goalsText = findViewById(R.id.profile_goalsText);
        assistsText = findViewById(R.id.profile_assistsText);
        gamesPlayedText = findViewById(R.id.profile_gamesPlayedText);
        goalsPerGameText = findViewById(R.id.profile_goalsGameText);
        assistsPerGameText = findViewById(R.id.profile_assistsGameText);
        yellowCardsText = findViewById(R.id.profile_yellowCardsText);
        redCardsText = findViewById(R.id.profile_redCardsText);
        emailText = findViewById(R.id.profile_emailText);
        progressBg = findViewById(R.id.profile_progressBg);
        progressBar = findViewById(R.id.profile_progressCircle);

        progressBar.setVisibility(View.VISIBLE);
        progressBg.setVisibility(View.VISIBLE);

        requestPlayerInfo();

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileViewActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        teamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileViewActivity.this, MultipleViewTeamActivity.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileViewActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        profileEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileViewActivity.this, ProfileEditActivity.class);
                intent.putExtra("newUser", false);
                startActivity(intent);
            }
        });

    }

    /**
     * Sends a GET request fetching all the player profile information.
     */
    private void requestPlayerInfo() {
        RequestQueue queue = Volley.newRequestQueue(ProfileViewActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_SERVER_URL + "/players/getProfile/" + playerID, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Profile JSON Response", response.toString());
                        jsonObject = response;
                        try {
                            updateFields();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },

        new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Profile JSON error", error.toString());
                    }
                }

        );
        queue.add(request);
    }

    /**
     * Updates all of the fields displayed in the player profile.
     * @throws JSONException
     */
    private void updateFields() throws JSONException {

        Bitmap imageBitmap = stringToBitMap(jsonObject.getString("profile_pic"));

        //if bitmap is null, set profile pic to a default image
        if(imageBitmap == null) {
            profilePicture.setImageResource(R.drawable.blank_profile);
        } else {
            profilePicture.setImageBitmap(imageBitmap);
        }

        //update all the fields
        fullNameText.setText(jsonObject.getString("first_name") + " " + jsonObject.getString("last_name"));
        usernameText.setText("@" + jsonObject.getString("username"));
        positionText.setText("Position: " + jsonObject.getString("preferred_position"));
        //friendsText.setText("Friends: " + jsonObject.getJSONArray("friends").length());
        heightText.setText((jsonObject.getInt("height") / 12) + "' " + jsonObject.getInt("height") % 12 + "\"");
        weightText.setText(jsonObject.getInt("weight") + " lb");
        LocalDate date = LocalDate.parse(jsonObject.getString("birthday"));
        birthdayText.setText(date.getMonthValue() + "/" + date.getDayOfMonth() + "/" + date.getYear());
        preferredPosText.setText(jsonObject.getString("preferred_position"));
        goalsText.setText(String.valueOf(jsonObject.getInt("goals")));
        assistsText.setText(String.valueOf(jsonObject.getInt("assists")));
        gamesPlayedText.setText(String.valueOf(jsonObject.getInt("gamesPlayed")));
        goalsPerGameText.setText(String.format("%.2f" , jsonObject.getDouble("goalsPerGame")));
        assistsPerGameText.setText(String.format("%.2f", jsonObject.getDouble("assistPerGame")));
        yellowCardsText.setText(String.valueOf(jsonObject.getInt("yellowCards")));
        redCardsText.setText(String.valueOf(jsonObject.getInt("redCards")));
        emailText.setText(jsonObject.getString("email"));

        progressBar.setVisibility(View.INVISIBLE);
        progressBg.setVisibility(View.INVISIBLE);

    }

    /**
     * Converts a given String to a bitmap which can be used to set ImageView's bitmap.
     * @param encodedString
     * @return
     */
    public Bitmap stringToBitMap(String encodedString){
        try {
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

}
