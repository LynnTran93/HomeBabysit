package com.example.homebabysit;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.login_button);
        Button signupButton = findViewById(R.id.signup_button);

        loginButton.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        signupButton.setOnClickListener(v -> startActivity(new Intent(this, SignupActivity.class)));
    }
}
