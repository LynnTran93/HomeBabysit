package com.example.homebabysit;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {

    private SearchView searchViewBabysitters;
    private RecyclerView recyclerViewBabysitters;
    private MaterialCalendarView materialCalendarView;
    private Spinner spinnerTimeSlots;
    private EditText editTextObservations;
    private Button buttonConfirmBooking;

    private List<Babysitter> babysitterList;
    private Map<String, Map<String, List<String>>> babysitterAvailability;
    private Babysitter selectedBabysitter;
    private String selectedDate;
    private BabysitterAdapter babysitterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Initialize UI components
        searchViewBabysitters = findViewById(R.id.searchViewBabysitters);
        recyclerViewBabysitters = findViewById(R.id.recyclerViewBabysitters);
        materialCalendarView = findViewById(R.id.materialCalendarView);
        spinnerTimeSlots = findViewById(R.id.spinnerTimeSlots);
        editTextObservations = findViewById(R.id.editTextObservations);
        buttonConfirmBooking = findViewById(R.id.buttonConfirmBooking);

        // Initialize data
        babysitterList = new ArrayList<>();
        babysitterList.add(new Babysitter(25, "5 years", "New York", 4, 15.50));
        babysitterList.add(new Babysitter(30, "3 years", "Los Angeles", 5, 18.00));
        babysitterList.add(new Babysitter(28, "4 years", "Chicago", 4, 17.25));


        babysitterAvailability = new HashMap<>();
        Map<String, List<String>> availabilityA = new HashMap<>();
        availabilityA.put("2024-10-12", Arrays.asList("10:00 - 12:00", "14:00 - 16:00"));
        babysitterAvailability.put("Babysitter A", availabilityA);
        // Additional availability entries as needed...

        // Set up RecyclerView with an adapter
        recyclerViewBabysitters.setLayoutManager(new LinearLayoutManager(this));
        babysitterAdapter = new BabysitterAdapter(babysitterList, this::onBabysitterSelected);
        recyclerViewBabysitters.setAdapter(babysitterAdapter);

        highlightAvailableDates();

        searchViewBabysitters.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBabysitters(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBabysitters(newText);
                return false;
            }
        });

        buttonConfirmBooking.setOnClickListener(v -> {
            String selectedTimeSlot = (String) spinnerTimeSlots.getSelectedItem();
            String observations = editTextObservations.getText().toString();

            if (selectedBabysitter != null && selectedDate != null && selectedTimeSlot != null && !selectedTimeSlot.isEmpty()) {
                saveBooking(selectedBabysitter, selectedDate, selectedTimeSlot, observations);
            } else {
                Toast.makeText(this, "Please select a babysitter, date, and time slot", Toast.LENGTH_SHORT).show();
            }
        });

        materialCalendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (selected) {
                selectedDate = date.getYear() + "-" + String.format("%02d", (date.getMonth() + 1)) + "-" + String.format("%02d", date.getDay());
                updateTimeSlots();
            }
        });
    }

    private void filterBabysitters(String query) {
        List<Babysitter> filteredList = new ArrayList<>();
        for (Babysitter babysitter : babysitterList) {
            if (babysitter.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(babysitter);
            }
        }
        babysitterAdapter.updateList(filteredList);
    }

    private void onBabysitterSelected(Babysitter babysitter) {
        selectedBabysitter = babysitter;
        highlightAvailableDates();
        selectedDate = null;
        updateTimeSlots();
    }

    private void highlightAvailableDates() {
        materialCalendarView.removeDecorators();
        List<CalendarDay> dates = new ArrayList<>();

        if (selectedBabysitter != null) {
            Map<String, List<String>> availability = babysitterAvailability.get(selectedBabysitter.getName());
            if (availability != null) {
                for (String date : availability.keySet()) {
                    String[] dateParts = date.split("-");
                    int year = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]) - 1;
                    int day = Integer.parseInt(dateParts[2]);
                    dates.add(CalendarDay.from(year, month, day));
                }
                materialCalendarView.addDecorator(new MyEventDecorator(dates, getResources().getColor(R.color.colorAccent, null)));
            }
        }
    }

    private void updateTimeSlots() {
        List<String> availableTimeSlots = new ArrayList<>();
        if (selectedBabysitter != null && selectedDate != null) {
            Map<String, List<String>> availability = babysitterAvailability.get(selectedBabysitter.getName());
            if (availability != null) {
                availableTimeSlots = availability.getOrDefault(selectedDate, new ArrayList<>());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableTimeSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeSlots.setAdapter(adapter);
    }

    private void saveBooking(Babysitter babysitter, String date, String timeSlot, String observations) {
        Toast.makeText(this, "Booking confirmed for " + babysitter.getName() + " on " + date + " at " + timeSlot, Toast.LENGTH_LONG).show();
    }
}
