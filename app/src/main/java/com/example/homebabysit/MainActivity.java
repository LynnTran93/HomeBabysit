package com.example.homebabysit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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
    }
}