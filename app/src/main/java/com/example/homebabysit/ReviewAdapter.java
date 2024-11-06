package com.example.homebabysit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {
    public ReviewAdapter(Context context, List<Review> reviews) {
        super(context, 0, reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Review review = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_review, parent, false);
        }

        // Lookup view for data population
        TextView reviewerNameTextView = convertView.findViewById(R.id.reviewer_name);
        TextView reviewTimeTextView = convertView.findViewById(R.id.review_time);
        TextView ratingTextView = convertView.findViewById(R.id.rating);
        TextView reviewTextView = convertView.findViewById(R.id.review_text);

        // Populate the data into the template view using the review object
        reviewerNameTextView.setText(review.getReviewerName());
        reviewTimeTextView.setText(review.getReviewTime());
        ratingTextView.setText("Rating: " + review.getRating());
        reviewTextView.setText(review.getReviewText());

        // Return the completed view to render on screen
        return convertView;
    }
}

