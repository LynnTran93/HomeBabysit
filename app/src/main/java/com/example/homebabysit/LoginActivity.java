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

    private EditText etUsername, etPassword;
    private Button btnLogin, btnSignup, btnCantSignIn;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        btnCantSignIn = findViewById(R.id.btnCantSignIn);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Username and Password are required", Toast.LENGTH_SHORT).show();
            } else {
                // Perform actual login check
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });

        btnSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
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

        btnCantSignIn.setOnClickListener(v -> {
            // Handle password recovery
        });
    }
}
