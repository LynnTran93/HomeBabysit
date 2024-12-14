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
import java.util.List;

public class BookingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MaterialCalendarView calendarView;
    private Spinner timeSlotSpinner;
    private EditText notesField;
    private Button confirmButton;

    private List<Babysitter> babysitters;
    private BabysitterAdapter adapter;

    private Babysitter selectedBabysitter;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        recyclerView = findViewById(R.id.recyclerViewBabysitters);
        calendarView = findViewById(R.id.materialCalendarView);
        timeSlotSpinner = findViewById(R.id.spinnerTimeSlots);
        notesField = findViewById(R.id.editTextObservations);
        confirmButton = findViewById(R.id.buttonConfirmBooking);

        babysitters = new ArrayList<>();
        loadBabysitters(); // Populate babysitters
        setupTimeSlotSpinner(); // Setup the time slot spinner

        adapter = new BabysitterAdapter(babysitters, babysitter -> {
            selectedBabysitter = babysitter;
            highlightAvailableDates();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        confirmButton.setOnClickListener(v -> {
            String notes = notesField.getText().toString();
            String timeSlot = (String) timeSlotSpinner.getSelectedItem();

            if (selectedBabysitter != null && selectedDate != null && timeSlot != null) {
                Toast.makeText(this, "Booking confirmed for " + selectedBabysitter.getName(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Select all booking details", Toast.LENGTH_SHORT).show();
            }
        });

        // Set date change listener for the calendar view
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            selectedDate = date.toString(); // Or format the date to a desired format
        });
    }

    private void loadBabysitters() {
        babysitters.add(new Babysitter(1, "Alice", "New York", 5, 20.0, "Available", 4.5));
        babysitters.add(new Babysitter(2, "Bob", "Los Angeles", 3, 18.0, "Available", 4.0));
    }

    private void setupTimeSlotSpinner() {
        List<String> timeSlots = new ArrayList<>();
        timeSlots.add("Morning");
        timeSlots.add("Afternoon");
        timeSlots.add("Evening");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSlotSpinner.setAdapter(adapter);
    }

    private void highlightAvailableDates() {
        calendarView.addDecorator(new MyEventDecorator(new ArrayList<>(), getResources().getColor(R.color.colorAccent)));
    }
}
