package com.example.homebabysit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 5000; // 5000 milliseconds = 5 seconds
    private static final String TAG = "FCM";

    private DatabaseHelper dbHelper; // Declare dbHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize dbHelper
        dbHelper = new DatabaseHelper(this);

        // Handle window insets for splash screen
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        // Parent Profile button setup
        Button parentProfileBtn = findViewById(R.id.parent_profile_button);
        parentProfileBtn.setOnClickListener(view -> {
            String email = "testparent@test.com";
            dbHelper.insertTestData(); // Ensure sample data insertion method is implemented
            Intent goToParentProfile = new Intent(MainActivity.this, ParentProfileActivity.class);
            goToParentProfile.putExtra("EMAIL", email);
            startActivity(goToParentProfile);
        });

        // Babysitter Profile button setup
        Button babysitterProfileBtn = findViewById(R.id.babysitter_profile_button);
        babysitterProfileBtn.setOnClickListener(view -> {
            String email = "testbabysitter@test.com";
            dbHelper.insertTestData();
            Intent goToBabysitterProfile = new Intent(MainActivity.this, BabysitterProfileActivity.class);
            goToBabysitterProfile.putExtra("EMAIL", email);
            startActivity(goToBabysitterProfile);
        });

        // Splash Screen timer
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIME_OUT);

        // Firebase Messaging: Get FCM token
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                return;
            }

            // Retrieve and log the token
            String token = task.getResult();
            Log.d(TAG, "Device Token: " + token);

            // Redirect to SignupActivity with the token as an extra
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            intent.putExtra("DEVICE_TOKEN", token);
            startActivity(intent);

            // Send the token to your backend if needed
        });
    }
}

