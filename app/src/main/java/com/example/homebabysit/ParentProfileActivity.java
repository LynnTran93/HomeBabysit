package com.example.homebabysit;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ParentProfileActivity extends AppCompatActivity {

    private EditText name;
    private String email;
    private EditText emailField;
    private EditText location;
    private EditText childrenNum;
    private EditText preferences;
    private DatabaseHelper databaseHelper;
    private Button profile_update_btn;
    private Button profile_return_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile);

        Intent fromLogin = getIntent();
        email = fromLogin.getStringExtra("EMAIL");

        // Initialize UI elements and the DatabaseHelper
        profile_update_btn = findViewById(R.id.profile_update_button);
        profile_return_btn = findViewById(R.id.profile_return_button);
        name = findViewById(R.id.name);
        emailField = findViewById(R.id.email);
        location = findViewById(R.id.location);
        childrenNum = findViewById(R.id.number_of_children);
        preferences = findViewById(R.id.babysitter_preferences);

        emailField.setText(email);
        emailField.setEnabled(false); // Email field is read-only

        databaseHelper = new DatabaseHelper(this);

        // Load existing parent data
        loadParentData(email);

        // Initially disable the button
        profile_update_btn.setEnabled(false);

        // Add TextWatchers to listen for text changes
        name.addTextChangedListener(inputWatcher);
        location.addTextChangedListener(inputWatcher);
        childrenNum.addTextChangedListener(inputWatcher);
        preferences.addTextChangedListener(inputWatcher);

        profile_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_name = name.getText().toString();
                String input_location = location.getText().toString();
                int input_childrenNum = Integer.parseInt(childrenNum.getText().toString());
                String input_preferences = preferences.getText().toString();

                if (databaseHelper.updateParentProfile(input_name, email, input_location, input_childrenNum, input_preferences)) {
                    Toast.makeText(ParentProfileActivity.this, "Update profile Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ParentProfileActivity.this, "Fail to update Profile!", Toast.LENGTH_SHORT).show();
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

    private void loadParentData(String email) {
        Cursor cursor = databaseHelper.getParentByEmail(email);
        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve column indices for each field
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_NAME);
            int locationIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_LOCATION);
            int childrenNumIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_CHILDREN);
            int preferencesIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_PREFERENCES);

            // Only set data if column indices are valid
            if (nameIndex >= 0) name.setText(cursor.getString(nameIndex));
            if (locationIndex >= 0) location.setText(cursor.getString(locationIndex));
            if (childrenNumIndex >= 0) childrenNum.setText(String.valueOf(cursor.getInt(childrenNumIndex)));
            if (preferencesIndex >= 0) preferences.setText(cursor.getString(preferencesIndex));

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
        String input_location = location.getText().toString().trim();
        String input_childrenNum = childrenNum.getText().toString().trim();
        String input_preferences = preferences.getText().toString().trim();

        // Enable the button if all fields are filled
        profile_update_btn.setEnabled(!input_name.isEmpty() && !input_location.isEmpty()
                && !input_childrenNum.isEmpty() && !input_preferences.isEmpty());
    }
}
