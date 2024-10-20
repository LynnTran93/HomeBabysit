package com.example.homebabysit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoToSignup;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToSignup = findViewById(R.id.btnGoToSignup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Email and Password are required", Toast.LENGTH_SHORT).show();
                } else {
                    // Dummy authentication logic
                    if (authenticateUser(email, password)) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        // Redirect to MainActivity after successful login
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();  // Close LoginActivity so the user cannot return using the back button
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnGoToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to SignupActivity when "Sign Up" button is clicked
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        dbHelper = new DatabaseHelper(this);

        Button parent_profile_btn = findViewById(R.id.btnLoginAsParent);
        parent_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "testparent@test.com";
                dbHelper.insertTestData();

                Intent goToParentProfile = new Intent(LoginActivity.this, ParentProfileActivity.class);
                goToParentProfile.putExtra("EMAIL", email);
                startActivity(goToParentProfile);
            }
        });

        Button babysitter_profile_btn = findViewById(R.id.btnLoginAsBabysitter);
        babysitter_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "testbabysitter@test.com";
                dbHelper.insertTestData();

                Intent goToBabysitterProfile = new Intent(LoginActivity.this, BabysitterProfileActivity.class);
                goToBabysitterProfile.putExtra("EMAIL", email);
                startActivity(goToBabysitterProfile);
            }
        });
    }

    private boolean authenticateUser(String email, String password) {
        // Dummy check: In a real application, you would query your backend or database
        return email.equals("user@example.com") && password.equals("password123");
    }
}
