package com.example.homebabysit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ReviewsListActivity extends AppCompatActivity {

    private ListView reviewsListView;
    private Button backButton;
    private DatabaseHelper databaseHelper;
    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_list);

        reviewsListView = findViewById(R.id.reviews_list_view);
        backButton = findViewById(R.id.back_button);

        databaseHelper = new DatabaseHelper(this);

        int babysitterId = getIntent().getIntExtra("BABYSITTER_ID", -1);
        loadReviews(babysitterId);

        backButton.setOnClickListener(v -> finish());
    }

    private void loadReviews(int babysitterId) {
        List<Review> reviews = databaseHelper.getReviewsByBabysitterId(babysitterId);
        reviewAdapter = new ReviewAdapter(this, reviews);
        reviewsListView.setAdapter(reviewAdapter);
    }
}
