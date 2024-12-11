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

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_review, parent, false);
        }

        TextView reviewerName = convertView.findViewById(R.id.reviewer_name);
        TextView reviewTime = convertView.findViewById(R.id.review_time);
        TextView rating = convertView.findViewById(R.id.rating);
        TextView reviewText = convertView.findViewById(R.id.review_text);

        reviewerName.setText(review.getReviewerName());
        reviewTime.setText(review.getReviewTime());
        rating.setText(String.valueOf(review.getRating()));
        reviewText.setText(review.getReviewText());

        return convertView;
    }
}
