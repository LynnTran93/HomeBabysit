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
                double rate = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BABYSITTER_RATE));

                Babysitter babysitter = new Babysitter(id, name, location, experience, rate);
                babysitterList.add(babysitter);
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter.updateList(babysitterList);
    }

    private void filter() {
        String searchText = searchBar.getText().toString().toLowerCase();
        int experienceFilter = filterExperience.getSelectedItemPosition(); // Adjust logic based on your spinner setup

        List<Babysitter> filteredList = new ArrayList<>();
        for (Babysitter babysitter : babysitterList) {
            boolean matchesSearch = babysitter.getName().toLowerCase().contains(searchText);
            boolean matchesExperience = experienceFilter == 0 || Integer.parseInt(babysitter.getExperience()) >= experienceFilter;

            if (matchesSearch && matchesExperience) {
                filteredList.add(babysitter);
            }
        }

        adapter.updateList(filteredList);
    }
}
