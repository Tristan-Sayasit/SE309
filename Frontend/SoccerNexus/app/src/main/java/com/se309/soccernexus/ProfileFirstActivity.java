package com.se309.soccernexus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Nathan Turnis
 * Class for displaying and handling logic for the Profile First Page
 */
public class ProfileFirstActivity extends AppCompatActivity {

    Button createProfileBtn;
    ImageButton homePageBtn;
    ImageButton teamPageBtn;
    ImageButton settingPageBtn;

    /**
     * Called when the activity is created. Initializes a listener on the required buttons.
     * When the user clicks the create profile button, it sends them to the EditProfileActivity
     * while sending an extra indicating its a new user.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_first_activity);

        createProfileBtn = findViewById(R.id.createProfileBtn);
        homePageBtn = findViewById(R.id.profile_first_homePageButton);
        teamPageBtn = findViewById(R.id.profile_first_teamPageButton);
        settingPageBtn = findViewById(R.id.profile_first_settingsPageButton);

        createProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileFirstActivity.this, ProfileEditActivity.class);
                intent.putExtra("newUser", true);
                startActivity(intent);
            }
        });

        homePageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileFirstActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        teamPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileFirstActivity.this, MultipleViewTeamActivity.class);
                startActivity(intent);
            }
        });

        settingPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileFirstActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

}
