package com.example.homebabysit;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BabysitterProfileActivity extends AppCompatActivity {

    private EditText name;
    private String email;
    private EditText emailField;
    private EditText qualifications;
    private EditText experience;
    private EditText hourly_rates;
    private EditText availability;
    private EditText rating;
    private EditText reviewNum;
    private DatabaseHelper databaseHelper;
    private Button profile_update_btn;
    private Button profile_return_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_babysitter_profile);

        Intent fromLogin = getIntent();
        email = fromLogin.getStringExtra("EMAIL");

        // Initialize UI elements and the DatabaseHelper
        profile_update_btn = findViewById(R.id.profile_update_button);
        profile_return_btn = findViewById(R.id.profile_return_button);
        name = findViewById(R.id.name);
        emailField = findViewById(R.id.email);
        qualifications = findViewById(R.id.qualifications);
        experience = findViewById(R.id.experience);
        hourly_rates = findViewById(R.id.hourly_rates);
        availability = findViewById(R.id.availability);
        rating = findViewById(R.id.rating);
        reviewNum = findViewById(R.id.review_num);


        emailField.setText(email);
        emailField.setEnabled(false);

        databaseHelper = new DatabaseHelper(this);

        // Load existing babysitter data
        loadBabysitterData(email);

        // Initially disable the button
        profile_update_btn.setEnabled(false);

        // Add TextWatchers to listen for text changes
        name.addTextChangedListener(inputWatcher);
        qualifications.addTextChangedListener(inputWatcher);
        experience.addTextChangedListener(inputWatcher);
        hourly_rates.addTextChangedListener(inputWatcher);
        availability.addTextChangedListener(inputWatcher);

        profile_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_name = name.getText().toString();
                String input_qualifications = qualifications.getText().toString();
                int input_experience = Integer.parseInt(experience.getText().toString());
                double input_hourly_rates = Double.parseDouble(hourly_rates.getText().toString());
                String input_availability = availability.getText().toString();

                if (databaseHelper.updateBabysitterProfile(input_name, email, input_qualifications, input_experience, input_hourly_rates, input_availability)) {
                    Toast.makeText(BabysitterProfileActivity.this, "Update profile Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BabysitterProfileActivity.this, "Fail to update Profile!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        profile_return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Closes this activity and returns to the previous one
            }
        });
    }

    private void loadBabysitterData(String email) {
        Cursor cursor = databaseHelper.getBabysitterByEmail(email);
        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve data from cursor
            String retrievedName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BABYSITTER_NAME));
            String retrievedQualifications = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BABYSITTER_QUALIFICATIONS));
            int retrievedExperience = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_BABYSITTER_EXPERIENCE));
            double retrievedHourlyRates = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_BABYSITTER_RATE));
            String retrievedAvailability = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BABYSITTER_AVAILABILITY));

            // Get babysitter_id
            int babysitterId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_BABYSITTER_ID));

            // Get rating and reviews number using the databaseHelper
            double rating_avg = databaseHelper.getRatingByBabysitterId(babysitterId);
            int review_num = databaseHelper.getReviewsNumByBabysitterId(babysitterId);

            // Set the retrieved data to the EditTexts
            name.setText(retrievedName);
            qualifications.setText(retrievedQualifications);
            experience.setText(String.valueOf(retrievedExperience));
            hourly_rates.setText(String.valueOf(retrievedHourlyRates));
            availability.setText(retrievedAvailability);
            rating.setText(String.valueOf(rating_avg));
            reviewNum.setText(String.valueOf(review_num));

            // Close cursor
            cursor.close();
        }

    }

    // TextWatcher to monitor input changes
    private final TextWatcher inputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Call a method to check if all inputs are filled
            checkInputs();
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    // Method to check if all inputs are filled
    private void checkInputs() {
        String input_name = name.getText().toString().trim();
        String input_qualifications = qualifications.getText().toString().trim();
        String input_experience = experience.getText().toString().trim();
        String input_hourly_rates = hourly_rates.getText().toString().trim();
        String input_availability = availability.getText().toString().trim();

        // Enable the button if all fields are filled
        profile_update_btn.setEnabled(!input_name.isEmpty() && !input_qualifications.isEmpty()
                && !input_experience.isEmpty() && !input_hourly_rates.isEmpty()
                && !input_availability.isEmpty());
    }
}
