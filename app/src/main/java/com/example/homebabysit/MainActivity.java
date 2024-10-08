package com.example.homebabysit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    private static final int SPLASH_TIME_OUT = 5000; // 5000 milliseconds = 5 seconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        Button parent_profile_btn = findViewById(R.id.parent_profile_button);
        parent_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "testparent@test.com";
                dbHelper.insertTestParentData();

                Intent goToParentProfile = new Intent(MainActivity.this, ParentProfileActivity.class);
                goToParentProfile.putExtra("EMAIL", email);
                startActivity(goToParentProfile);
            }
        });

        Button babysitter_profile_btn = findViewById(R.id.babysitter_profile_button);
        babysitter_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "testbabysitter@test.com";
                dbHelper.insertTestBabysitterData();

                Intent goToBabysitterProfile = new Intent(MainActivity.this, BabysitterProfileActivity.class);
                goToBabysitterProfile.putExtra("EMAIL", email);
                startActivity(goToBabysitterProfile);
            }
        });
         // Splash Screen

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start your main activity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                // Close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
