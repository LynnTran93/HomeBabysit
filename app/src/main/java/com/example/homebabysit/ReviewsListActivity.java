package com.example.homebabysit;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ReviewsListActivity extends AppCompatActivity {

    private ListView reviewsListView;
    private DatabaseHelper databaseHelper;
    private ReviewAdapter reviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_list);

        reviewsListView = findViewById(R.id.reviews_list_view);
        databaseHelper = new DatabaseHelper(this);

        int babysitterId = getIntent().getIntExtra("BABYSITTER_ID", -1);

        // Load and display reviews
        loadReviews(babysitterId);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());
    }

    private void loadReviews(int babysitterId) {
        List<Review> reviews = databaseHelper.getReviewsByBabysitterId(babysitterId);
        reviewsAdapter = new ReviewAdapter(this, reviews);
        reviewsListView.setAdapter(reviewsAdapter);
    }
}
