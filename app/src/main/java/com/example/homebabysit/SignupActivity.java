package com.example.homebabysit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText firstNameField, lastNameField, usernameField, passwordField, emailField;
    private CheckBox caregiverCheckBox, seekingCareCheckBox;
    private Button registerButton, returnToLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeUI();

        registerButton.setOnClickListener(v -> {
            String firstName = firstNameField.getText().toString();
            String lastName = lastNameField.getText().toString();
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            String email = emailField.getText().toString();
            boolean isCaregiver = caregiverCheckBox.isChecked();
            boolean isSeekingCare = seekingCareCheckBox.isChecked();

            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(username) ||
                    TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Proceed with user registration
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        });

        returnToLoginButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

    private void initializeUI() {
        firstNameField = findViewById(R.id.etFirstName);
        lastNameField = findViewById(R.id.etLastName);
        usernameField = findViewById(R.id.etUsername);
        passwordField = findViewById(R.id.etPassword);
        emailField = findViewById(R.id.etEmail);
        caregiverCheckBox = findViewById(R.id.cbCaregiver);
        seekingCareCheckBox = findViewById(R.id.cbSeekingCare);
        registerButton = findViewById(R.id.btnRegister);
        returnToLoginButton = findViewById(R.id.btnReturnToLogin);
    }
}
