package com.example.homebabysit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText searchBar;
    private Spinner experienceFilterSpinner;
    private RecyclerView recyclerView;
    private BabysitterAdapter adapter;

    private DatabaseHelper databaseHelper;
    private List<Babysitter> babysitterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBar = findViewById(R.id.search_bar);
        experienceFilterSpinner = findViewById(R.id.filter_experience);
        recyclerView = findViewById(R.id.recycler_view);

        databaseHelper = new DatabaseHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BabysitterAdapter(babysitterList, babysitter -> {
            // Handle babysitter selection (optional)
        });
        recyclerView.setAdapter(adapter);

        loadBabysitters();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBabysitters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        experienceFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterBabysitters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed here
            }
        });
    }

    private void loadBabysitters() {
        babysitterList = databaseHelper.getAllBabysitters();
        adapter.updateList(babysitterList);
    }

    private void filterBabysitters() {
        String searchText = searchBar.getText().toString().toLowerCase();
        int experienceFilter = experienceFilterSpinner.getSelectedItemPosition();

        List<Babysitter> filteredList = new ArrayList<>();
        for (Babysitter babysitter : babysitterList) {
            boolean matchesSearch = babysitter.getName().toLowerCase().contains(searchText);
            boolean matchesExperience = (experienceFilter == 0) ||
                    (experienceFilter == 1 && babysitter.getExperience() < 2) ||
                    (experienceFilter == 2 && babysitter.getExperience() >= 2 && babysitter.getExperience() < 5) ||
                    (experienceFilter == 3 && babysitter.getExperience() >= 5);

            if (matchesSearch && matchesExperience) {
                filteredList.add(babysitter);
            }
        }
        adapter.updateList(filteredList);
    }
}

