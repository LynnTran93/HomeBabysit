package com.example.homebabysit;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BabysitterProfileActivity extends AppCompatActivity {

    private EditText name, emailField, qualifications, experience, hourlyRates, locationField, availabilityField;
    private Spinner availabilitySpinner;
    private Button profileUpdateBtn, profileReturnBtn, viewReviewsBtn;
    private DatabaseHelper databaseHelper;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_babysitter_profile);

        // Initialize UI elements
        initializeUI();

        // Get email from intent
        Intent fromLogin = getIntent();
        email = fromLogin.getStringExtra("EMAIL");

        // Load babysitter data
        loadBabysitterData(email);

        // Set up listeners
        setupListeners();
    }

    private void initializeUI() {
        name = findViewById(R.id.name);
        emailField = findViewById(R.id.email);
        qualifications = findViewById(R.id.qualifications);
        experience = findViewById(R.id.experience);
        hourlyRates = findViewById(R.id.hourly_rates);
        locationField = findViewById(R.id.location); // New location field
        availabilityField = findViewById(R.id.availability); // If it's an EditText
        // availabilitySpinner = findViewById(R.id.availability_spinner); // Uncomment if it's a Spinner
        profileUpdateBtn = findViewById(R.id.profile_update_button);
        profileReturnBtn = findViewById(R.id.profile_return_button);
        viewReviewsBtn = findViewById(R.id.view_reviews_btn);
        databaseHelper = new DatabaseHelper(this);
        emailField.setEnabled(false); // Email is read-only
    }


    private void loadBabysitterData(String email) {
        Cursor cursor = databaseHelper.getBabysitterByEmail(email);
        if (cursor != null && cursor.moveToFirst()) {
            // Safely retrieve column values
            name.setText(getColumnValue(cursor, DatabaseHelper.COLUMN_BABYSITTER_NAME, "Unknown"));
            qualifications.setText(getColumnValue(cursor, DatabaseHelper.COLUMN_BABYSITTER_QUALIFICATIONS, "N/A"));
            experience.setText(String.valueOf(getColumnValue(cursor, DatabaseHelper.COLUMN_BABYSITTER_EXPERIENCE, 0)));
            hourlyRates.setText(String.valueOf(getColumnValue(cursor, DatabaseHelper.COLUMN_BABYSITTER_RATE, 0.0)));
            // Load additional fields like location and availability
            locationField.setText(getColumnValue(cursor, DatabaseHelper.COLUMN_BABYSITTER_LOCATION, "Unknown"));
            availabilityField.setText(getColumnValue(cursor, DatabaseHelper.COLUMN_BABYSITTER_AVAILABILITY, "Unknown"));
            cursor.close();
        }
    }

    private void setupListeners() {
        profileUpdateBtn.setOnClickListener(view -> {
            String updatedName = name.getText().toString();
            String location = locationField.getText().toString();  // Get location
            String availability = availabilityField.getText().toString(); // Get availability

            boolean isUpdated = databaseHelper.updateBabysitterProfile(
                    updatedName, email, qualifications.getText().toString(),
                    Integer.parseInt(experience.getText().toString()),
                    Double.parseDouble(hourlyRates.getText().toString()),
                    location, availability); // Include new parameters
            Toast.makeText(this, isUpdated ? "Profile Updated" : "Update Failed", Toast.LENGTH_SHORT).show();
        });

        profileReturnBtn.setOnClickListener(view -> finish());

        viewReviewsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ReviewsListActivity.class);
            intent.putExtra("BABYSITTER_ID", 1); // Replace with actual ID logic
            startActivity(intent);
        });
    }

    private <T> T getColumnValue(Cursor cursor, String columnName, T defaultValue) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex != -1) {
            if (defaultValue instanceof String) {
                return (T) cursor.getString(columnIndex);
            } else if (defaultValue instanceof Integer) {
                return (T) Integer.valueOf(cursor.getInt(columnIndex));
            } else if (defaultValue instanceof Double) {
                return (T) Double.valueOf(cursor.getDouble(columnIndex));
            }
        }
        return defaultValue;
    }
}
