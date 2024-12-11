package com.example.homebabysit;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ParentProfileActivity extends AppCompatActivity {

    private EditText nameField, locationField, childrenNumField, preferencesField;
    private Button updateButton, returnButton;
    private DatabaseHelper databaseHelper;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile);

        initializeUI();

        Intent intent = getIntent();
        email = intent.getStringExtra("EMAIL");

        if (!TextUtils.isEmpty(email)) {
            loadParentData();
        } else {
            Toast.makeText(this, "Email not provided", Toast.LENGTH_SHORT).show();
            finish();
        }

        updateButton.setOnClickListener(v -> updateProfile());
        returnButton.setOnClickListener(v -> finish());
    }

    private void initializeUI() {
        nameField = findViewById(R.id.name);
        locationField = findViewById(R.id.location);
        childrenNumField = findViewById(R.id.number_of_children);
        preferencesField = findViewById(R.id.babysitter_preferences);
        updateButton = findViewById(R.id.profile_update_button);
        returnButton = findViewById(R.id.profile_return_button);
        databaseHelper = new DatabaseHelper(this);
    }

    private void loadParentData() {
        Cursor cursor = databaseHelper.getParentByEmail(email);
        if (cursor != null && cursor.moveToFirst()) {
            nameField.setText(getColumnValue(cursor, DatabaseHelper.COLUMN_PARENT_NAME, "Unknown"));
            locationField.setText(getColumnValue(cursor, DatabaseHelper.COLUMN_PARENT_LOCATION, "Unknown"));
            childrenNumField.setText(String.valueOf(getColumnValue(cursor, DatabaseHelper.COLUMN_PARENT_CHILDREN, 0)));
            preferencesField.setText(getColumnValue(cursor, DatabaseHelper.COLUMN_PARENT_PREFERENCES, "None"));
            cursor.close();
        } else {
            Toast.makeText(this, "Failed to load parent data", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfile() {
        String name = nameField.getText().toString().trim();
        String location = locationField.getText().toString().trim();
        String children = childrenNumField.getText().toString().trim();
        String preferences = preferencesField.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(location) || TextUtils.isEmpty(children) || TextUtils.isEmpty(preferences)) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int numChildren = Integer.parseInt(children);
            boolean isUpdated = databaseHelper.updateParentProfile(name, email, location, numChildren, preferences);

            Toast.makeText(this, isUpdated ? "Profile updated successfully" : "Failed to update profile", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number of children", Toast.LENGTH_SHORT).show();
        }
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
