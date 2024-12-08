package com.example.homebabysit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.homebabysit.PushNotificationSender;

public class SignupActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etUsername, etPassword, etEmail;
    private CheckBox cbCaregiver, cbSeekingCare;
    private Button btnRegister, btnReturnToLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        cbCaregiver = findViewById(R.id.cbCaregiver);
        cbSeekingCare = findViewById(R.id.cbSeekingCare);
        btnRegister = findViewById(R.id.btnRegister);
        btnReturnToLogin = findViewById(R.id.btnReturnToLogin);

        btnRegister.setOnClickListener(v -> {
            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String email = etEmail.getText().toString();
            boolean isCaregiver = cbCaregiver.isChecked();
            boolean isSeekingCare = cbSeekingCare.isChecked();

            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
                    TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
                Toast.makeText(SignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = getIntent();
                String deviceToken = intent.getStringExtra("DEVICE_TOKEN");
                // Proceed with user registration
                //Toast.makeText(SignupActivity.this, , Toast.LENGTH_SHORT).show();
                PushNotificationSender.sendNotification(deviceToken, "Success", "Registration Successful");
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnReturnToLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });
    }
}
