package com.example.homebabysit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    private List<Babysitter> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBar = findViewById(R.id.search_bar);
        filterExperience = findViewById(R.id.filter_experience);
        recyclerView = findViewById(R.id.recycler_view);

        // Initialize RecyclerView and Adapter with an empty filteredList initially
        adapter = new BabysitterAdapter(filteredList, babysitter -> {
            // Handle babysitter click event here if needed
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load Babysitters from your database or dummy data
        loadBabysitters();

        // Set listeners for search and filter actions
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(); // Calls filter method whenever text changes
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        filterExperience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter(); // Calls filter method whenever a new experience level is selected
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadBabysitters() {
        babysitterList.add(new Babysitter("Jane Doe", "Intermediate", "Downtown"));
        babysitterList.add(new Babysitter("John Smith", "Expert", "Uptown"));
        babysitterList.add(new Babysitter("Alice Brown", "Beginner", "Suburbs"));

        // Initially, display all babysitters in filteredList
        filteredList.addAll(babysitterList);
        adapter.notifyDataSetChanged();
    }

    private void filter() {
        String searchText = searchBar.getText().toString().toLowerCase();
        String experience = filterExperience.getSelectedItem().toString();

        filteredList.clear();
        for (Babysitter b : babysitterList) {
            boolean matchesExperience = experience.equals("Any") || b.getExperience().equalsIgnoreCase(experience);
            boolean matchesSearchText = b.getName().toLowerCase().contains(searchText) || b.getLocation().toLowerCase().contains(searchText);

            if (matchesExperience && matchesSearchText) {
                filteredList.add(b);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
