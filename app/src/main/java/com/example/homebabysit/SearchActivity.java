package com.example.homebabysit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText searchBar;
    private Spinner filterExperience;
    private RecyclerView recyclerView;
    private BabysitterAdapter adapter;
    private List<Babysitter> babysitterList = new ArrayList<>();
    private DatabaseHelper dbHelper;

    // Filters for rating and availability
    private int selectedMinRating = 0; // Replace with actual UI input
    private int selectedMaxRating = 5; // Replace with actual UI input
    private List<String> selectedAvailabilityDays = new ArrayList<>(); // Replace with actual UI input

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbHelper = new DatabaseHelper(this);
        searchBar = findViewById(R.id.search_bar);
        filterExperience = findViewById(R.id.filter_experience);
        recyclerView = findViewById(R.id.recycler_view);

        // Initialize RecyclerView and Adapter
        adapter = new BabysitterAdapter(babysitterList, babysitter -> {
            // Handle babysitter click event if needed
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load babysitters from the database
        loadBabysitters();

        // Search bar listener
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Spinner filter listener
        filterExperience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadBabysitters() {
        babysitterList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_BABYSITTERS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BABYSITTER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BABYSITTER_NAME));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BABYSITTER_LOCATION));
                int experience = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BABYSITTER_EXPERIENCE));
                double hourlyRate = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BABYSITTER_RATE));
                String availability = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BABYSITTER_AVAILABILITY));
                double averageRating = dbHelper.getRatingByBabysitterId(id);

                Babysitter babysitter = new Babysitter(id, name, location, experience, hourlyRate, availability, averageRating);
                babysitterList.add(babysitter);
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter.updateList(babysitterList);
    }

    private void filter() {
        String searchText = searchBar.getText().toString().toLowerCase();

        // Get selected experience level (0: any, 1: beginner, 2: intermediate, 3: expert)
        int experienceFilter = filterExperience.getSelectedItemPosition();

        List<Babysitter> filteredList = new ArrayList<>();
        for (Babysitter babysitter : babysitterList) {
            boolean matchesSearch = babysitter.getName().toLowerCase().contains(searchText);

            boolean matchesExperience = (experienceFilter == 0) ||
                    (experienceFilter == 1 && babysitter.getExperience() < 2) ||
                    (experienceFilter == 2 && babysitter.getExperience() >= 2 && babysitter.getExperience() < 5) ||
                    (experienceFilter == 3 && babysitter.getExperience() >= 5);

            boolean matchesRating = (babysitter.getAverageRating() >= selectedMinRating &&
                    babysitter.getAverageRating() <= selectedMaxRating);

            boolean matchesAvailability = matchesSelectedDays(babysitter.getAvailability(), selectedAvailabilityDays);

            if (matchesSearch && matchesExperience && matchesRating && matchesAvailability) {
                filteredList.add(babysitter);
            }
        }

        adapter.updateList(filteredList);
    }

    // Helper method to parse babysitter availability against selected days
    private boolean matchesSelectedDays(String babysitterAvailability, List<String> selectedDays) {
        // Babysitter availability is stored as comma-separated days like "Monday,Tuesday"
        if (selectedDays.isEmpty()) {
            return true; // If no days are selected, all babysitters match
        }
        for (String day : selectedDays) {
            if (babysitterAvailability.toLowerCase().contains(day.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}

