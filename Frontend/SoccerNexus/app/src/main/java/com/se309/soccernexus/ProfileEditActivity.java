package com.se309.soccernexus;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.playerID;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;

/**
 * @author Nathan Turnis
 * Class for displaying handling logic in the Profile Edit/Create Page
 */
public class ProfileEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView profileImage;
    TextView pageTitle;
    TextView warningText;
    TextInputLayout firstNameInput, lastNameInput, weightInput;
    Button dateButton;
    Button saveButton, cancelButton;
    Spinner heightFeetSpinner, heightInchesSpinner, posSpinner;
    LocalDate date;
    ImageButton profilePictureBtn;
    Boolean isNewUser;

    String bitmapString;

    View progressBg;
    ProgressBar progressBar;

    //values birthday selector will initialize to
    int yearToPick;
    int monthToPick;
    int dayToPick;

    /**
     * Called when the activity is created. Initializes a listener on the required buttons.
     * Depending on whether or not the user has a profile, the title will be updated to
     * "Edit Profile" or "Create Profile".
     * Sets up all the input and dropdown fields that the user can edit. Creates logic for the
     * date picker. Upon selecting a profile picture from the android device, the picture
     * is converted to a bitmap image.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_activity);

        Bundle extras = getIntent().getExtras();
        isNewUser = extras.getBoolean("newUser");

        profileImage = findViewById(R.id.profile_edit_profilePictureView);
        pageTitle = findViewById(R.id.profile_edit_title);
        warningText = findViewById(R.id.profile_edit_warningText);
        firstNameInput = findViewById(R.id.profile_edit_firstNameInput);
        lastNameInput = findViewById(R.id.profile_edit_lastNameInput);
        heightFeetSpinner = findViewById(R.id.profile_edit_feetSpinner);
        heightInchesSpinner = findViewById(R.id.profile_edit_inchesSpinner);
        weightInput = findViewById(R.id.profile_edit_weightInput);
        dateButton = findViewById(R.id.profile_edit_birthdayButton);
        saveButton = findViewById(R.id.profile_edit_saveBtn);
        cancelButton = findViewById(R.id.profile_edit_cancelBtn);
        posSpinner = findViewById(R.id.profile_edit_positionSpinner);
        profilePictureBtn = findViewById(R.id.profile_edit_selectImgBtn);
        progressBg = findViewById(R.id.profile_edit_progressBg);
        progressBar = findViewById(R.id.profile_edit_progressCircle);

        //if we are a new user, go to creation, otherwise go to edit
        if(isNewUser) {
            date = LocalDate.now();
            yearToPick = Calendar.getInstance().get(Calendar.YEAR);
            monthToPick = Calendar.getInstance().get(Calendar.MONTH);
            dayToPick = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            dateButton.setText(date.getMonthValue() + "/" + date.getDayOfMonth() + "/" + date.getYear());
            saveButton.setText("Create Profile");
            pageTitle.setText("Create Profile");
            cancelButton.setEnabled(false);
            cancelButton.setVisibility(View.INVISIBLE);
        } else {
            progressBg.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            saveButton.setText("Save Changes");
            pageTitle.setText("Edit Profile");
            cancelButton.setEnabled(true);
            cancelButton.setVisibility(View.VISIBLE);
            updateInformation();
        }

        //adapter setup for 3 dropdowns/pickers
        ArrayAdapter<CharSequence> feetAdapter = ArrayAdapter.createFromResource(this, R.array.heightFeet, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> inchesAdapter = ArrayAdapter.createFromResource(this, R.array.heightInches, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> posAdapter = ArrayAdapter.createFromResource(this, R.array.preferredPositions, android.R.layout.simple_spinner_item);
        feetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inchesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        posAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heightFeetSpinner.setAdapter(feetAdapter);
        heightInchesSpinner.setAdapter(inchesAdapter);
        posSpinner.setAdapter(posAdapter);
        heightFeetSpinner.setOnItemSelectedListener(this);
        heightInchesSpinner.setOnItemSelectedListener(this);
        posSpinner.setOnItemSelectedListener(this);


        profilePictureBtn.setOnClickListener(new View.OnClickListener() {
            // Registers a photo picker activity launcher in single-select mode.
            ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                    registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                        // Callback is invoked after the user selects a media item or closes the
                        // photo picker.
                        Bitmap bitmap;
                        if (uri != null) {
                            Log.d("PhotoPicker", "Selected URI: " + uri);

                            try {
                                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getApplicationContext().getContentResolver(), uri));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            profileImage.setImageBitmap(bitmap);
                            bitmapString = bitmapToString(bitmap);
                        } else {
                            Log.d("PhotoPicker", "No media selected");}
                    });
            @Override
            public void onClick(View v) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(yearToPick, monthToPick, dayToPick);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileEditActivity.this, ProfileViewActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Sends request to backend to update/create the player profile. Checks for form validation.
     */
    private void sendRequest() {

        //if fields are blank
        if(firstNameInput.getEditText().getText().toString().isEmpty()
            || lastNameInput.getEditText().getText().toString().isEmpty()
            || weightInput.getEditText().getText().toString().isEmpty()) {
                warningText.setText("Some fields are blank!");
                warningText.setAlpha(1);
                return;
        }

        int weightValue = Integer.parseInt(weightInput.getEditText().getText().toString());
        int playerAge = Calendar.getInstance().get(Calendar.YEAR) - date.getYear();

        //if weight is not in range
        if(weightValue < 10 || weightValue > 1499) {
            warningText.setText("Weight must be between 10 lb and 1500 lb!");
            warningText.setAlpha(1);
            return;
        }

        //if no height is selected
        if(heightFeetSpinner.getSelectedItem().toString().equals("Ft.") || heightInchesSpinner.getSelectedItem().toString().equals("In.")) {
            warningText.setText("Select a height!");
            warningText.setAlpha(1);
            return;
        }

        int feet = Integer.parseInt(heightFeetSpinner.getSelectedItem().toString().replaceAll("[\\D]", ""));
        int inches = Integer.parseInt(heightInchesSpinner.getSelectedItem().toString().replaceAll("[\\D]", ""));
        int totalInches = (feet * 12) + inches;

        //if no preferred position is selected
        if(posSpinner.getSelectedItem().toString().equals("Select a position...")) {
            warningText.setText("Select a preferred position!");
            warningText.setAlpha(1);
            return;
        }

        //check player age
        if(playerAge < 12) {
            warningText.setText("Must be 12 years old or older!");
            warningText.setAlpha(1);
            return;
        } else if(playerAge > 110) {
            warningText.setText("Must be 110 years old or younger!");
            warningText.setAlpha(1);
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(ProfileEditActivity.this);
        JSONObject body = new JSONObject();
        try {
            body.put("player_id", playerID);
            body.put("profile_pic", bitmapString);
            body.put("first_name", firstNameInput.getEditText().getText().toString());
            body.put("last_name", lastNameInput.getEditText().getText().toString());
            body.put("height", totalInches);
            body.put("weight", weightValue);
            body.put("preferred_position", posSpinner.getSelectedItem().toString());
            body.put("birthday", date.toString());
            body.put("has_profile", true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request;
        //create profile if user is new, otherwise update their profile
        /*if(isNewUser) {
            request = new JsonObjectRequest(Request.Method.POST, BASE_SERVER_URL + "/players/createProfile", body,

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("good response", response.toString());
                            Intent intent = new Intent(ProfileEditActivity.this, ProfileViewActivity.class);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("bad response", String.valueOf(error.networkResponse.statusCode));
                        }
                    }
            );
        } else {*/
            progressBg.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            request = new JsonObjectRequest(Request.Method.PUT, BASE_SERVER_URL + "/players/updateProfile", body,

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("good response", response.toString());
                            Intent intent = new Intent(ProfileEditActivity.this, ProfileViewActivity.class);
                            progressBg.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("bad response", error.toString());

                        }
                    }
            );
        //}
        queue.add(request);
    }

    /**
     * Sends a GET request fetching information about user. Calls updateFields() on a
     * successful response.
     */
    private void updateInformation() {
        RequestQueue queue = Volley.newRequestQueue(ProfileEditActivity.this);
        JsonObjectRequest request;
        request = new JsonObjectRequest(Request.Method.GET, BASE_SERVER_URL + "/players/getProfile/" + playerID, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            updateFields(response);
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

    /**
     * Updates the fields based on JSONObject provided by updateInformation()
     * @param object JSONObject given by updateInformation()
     * @throws JSONException
     */
    private void updateFields(JSONObject object) throws JSONException {

        Bitmap imageBitmap = stringToBitMap(object.getString("profile_pic"));

        //if bitmap is null, set a default profile picture
        if(imageBitmap == null) {
            profileImage.setImageResource(R.drawable.blank_profile);
        } else {
            profileImage.setImageBitmap(imageBitmap);
            bitmapString = bitmapToString(imageBitmap);
        }

        firstNameInput.getEditText().setText(object.getString("first_name"));
        lastNameInput.getEditText().setText(object.getString("last_name"));
        heightFeetSpinner.setSelection((object.getInt("height") / 12) + 1);
        heightInchesSpinner.setSelection((object.getInt("height") % 12) + 1);
        weightInput.getEditText().setText(String.valueOf(object.getInt("weight")));
        LocalDate date1 = LocalDate.parse(object.getString("birthday"));
        yearToPick = date1.getYear();
        monthToPick = date1.getMonthValue() - 1;
        dayToPick = date1.getDayOfMonth();
        dateButton.setText(date1.getMonthValue() + "/" + date1.getDayOfMonth() + "/" + date1.getYear());
        date = date1;

        switch (object.getString("preferred_position")) {
            case "Attacker":
                posSpinner.setSelection(1);
                break;
            case "Midfielder":
                posSpinner.setSelection(2);
                break;
            case "Defender":
                posSpinner.setSelection(3);
                break;
            case "Goalkeeper":
                posSpinner.setSelection(4);
                break;
        }

        progressBg.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

    }


    /**
     * Dialog that pops up when user clicks on the birthday button.
     * @param year initial year value
     * @param month initial month value
     * @param day initial day of month value
     */
    public void openDialog(int year, int month, int day) {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                if(month < 10) {
                    if(dayOfMonth > 9) {
                        date = LocalDate.parse(year + "-" + "0" + month + "-" + dayOfMonth);
                    } else {
                        date = LocalDate.parse(year + "-" + "0" + month + "-" + "0" + dayOfMonth);
                    }
                } else {
                    if(dayOfMonth > 9) {
                        date = LocalDate.parse(year + "-" + month + "-" + dayOfMonth);
                    } else {
                        date = LocalDate.parse(year + "-" + month + "-" + "0" + dayOfMonth);
                    }
                }
                dateButton.setText(month + "/" + dayOfMonth + "/" + year);
            }
        }, year, month, day);
        dialog.show();
    }

    /**
     * Called when a dropdown item is selected.
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
     * Called when nothing is selected on a dropdown item.
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Select...", Toast.LENGTH_SHORT).show();
    }

    /**
     * Converts a given bitmap to a String, which can be sent to the backend.
     * @param bitmap
     * @return
     */
    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String s = Base64.encodeToString(b, Base64.DEFAULT);
        return s;
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
