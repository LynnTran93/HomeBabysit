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

    private List<String> babysitterList;
    private Map<String, Map<String, List<String>>> babysitterAvailability;
    private String selectedBabysitter;
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
        babysitterList.add("Babysitter A");
        babysitterList.add("Babysitter B");
        babysitterList.add("Babysitter C");

        babysitterAvailability = new HashMap<>();
        Map<String, List<String>> availabilityA = new HashMap<>();
        availabilityA.put("2024-10-12", Arrays.asList("10:00 - 12:00", "14:00 - 16:00"));
        availabilityA.put("2024-10-13", Arrays.asList("09:00 - 11:00"));
        availabilityA.put("2024-10-19", Arrays.asList("10:00 - 12:00", "14:00 - 16:00"));
        availabilityA.put("2024-10-20", Arrays.asList("10:00 - 12:00", "14:00 - 16:00"));
        babysitterAvailability.put("Babysitter A", availabilityA);

        Map<String, List<String>> availabilityB = new HashMap<>();
        availabilityB.put("2024-10-12", Arrays.asList("09:00 - 11:00", "13:00 - 15:00"));
        availabilityB.put("2024-10-19", Arrays.asList("09:00 - 11:00", "13:00 - 15:00"));
        availabilityA.put("2024-10-28", Arrays.asList("10:00 - 12:00", "14:00 - 16:00"));
        availabilityA.put("2024-10-29", Arrays.asList("10:00 - 12:00", "14:00 - 16:00"));
        babysitterAvailability.put("Babysitter B", availabilityB);

        Map<String, List<String>> availabilityC = new HashMap<>();
        availabilityC.put("2024-10-10", Arrays.asList("08:00 - 10:00", "16:00 - 18:00"));
        availabilityC.put("2024-10-11", Arrays.asList("11:00 - 13:00"));
        availabilityC.put("2024-10-12", Arrays.asList("11:00 - 13:00"));
        availabilityC.put("2024-10-13", Arrays.asList("11:00 - 13:00"));
        babysitterAvailability.put("Babysitter C", availabilityC);

        // Set up RecyclerView with an adapter
        recyclerViewBabysitters.setLayoutManager(new LinearLayoutManager(this));
        babysitterAdapter = new BabysitterAdapter(babysitterList, this::onBabysitterSelected);
        recyclerViewBabysitters.setAdapter(babysitterAdapter);

        // Highlight available dates for the selected babysitter
        highlightAvailableDates();

        // Set up SearchView to filter the RecyclerView
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

        // Set up the confirm button click listener
        buttonConfirmBooking.setOnClickListener(v -> {
            String selectedTimeSlot = (String) spinnerTimeSlots.getSelectedItem();
            String observations = editTextObservations.getText().toString();

            if (selectedBabysitter != null && selectedDate != null && selectedTimeSlot != null && !selectedTimeSlot.isEmpty()) {
                saveBooking(selectedBabysitter, selectedDate, selectedTimeSlot, observations);
            } else {
                Toast.makeText(this, "Please select a babysitter, date, and time slot", Toast.LENGTH_SHORT).show();
            }
        });

        // Set a listener for date selection on the calendar
        materialCalendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (selected) {
                selectedDate = date.getYear() + "-" + String.format("%02d", (date.getMonth() + 1)) + "-" + String.format("%02d", date.getDay());
                updateTimeSlots();
            }
        });
    }

    private void filterBabysitters(String query) {
        List<String> filteredList = new ArrayList<>();
        for (String babysitter : babysitterList) {
            if (babysitter.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(babysitter);
            }
        }
        babysitterAdapter.updateList(filteredList);
    }

    private void onBabysitterSelected(String babysitter) {
        selectedBabysitter = babysitter;
        highlightAvailableDates(); // Update highlighted dates when a babysitter is selected
        selectedDate = null; // Reset the selected date
        updateTimeSlots(); // Clear time slots until a new date is selected
    }

    private void highlightAvailableDates() {
        materialCalendarView.removeDecorators(); // Clear any previous decorators
        List<CalendarDay> dates = new ArrayList<>();

        if (selectedBabysitter != null) {
            Map<String, List<String>> availability = babysitterAvailability.get(selectedBabysitter);
            if (availability != null && !availability.isEmpty()) {
                for (String date : availability.keySet()) {
                    String[] dateParts = date.split("-");
                    int year = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]) - 1; // Months are 0-based
                    int day = Integer.parseInt(dateParts[2]);
                    dates.add(CalendarDay.from(year, month, day));
                }

                materialCalendarView.addDecorator(new MyEventDecorator(dates, getResources().getColor(R.color.colorAccent, null)));
            } else {
                Toast.makeText(this, "No availability for the selected babysitter.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select a babysitter to view availability.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTimeSlots() {
        List<String> availableTimeSlots = new ArrayList<>();
        if (selectedBabysitter != null && selectedDate != null) {
            Map<String, List<String>> availability = babysitterAvailability.get(selectedBabysitter);
            if (availability != null) {
                availableTimeSlots = availability.getOrDefault(selectedDate, new ArrayList<>());
            }
        }

        if (availableTimeSlots.isEmpty() && selectedDate != null) {
            Toast.makeText(this, "No available time slots for the selected date and babysitter.", Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableTimeSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeSlots.setAdapter(adapter);
    }

    private void saveBooking(String babysitter, String date, String timeSlot, String observations) {
        Toast.makeText(this, "Booking confirmed for " + babysitter + " on " + date + " at " + timeSlot, Toast.LENGTH_LONG).show();
    }
}
