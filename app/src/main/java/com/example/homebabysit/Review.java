package com.example.homebabysit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Review {
    private String reviewerName;
    private String reviewTime;
    private int rating;
    private String reviewText;

    // Constructor
    public Review(String reviewerName, String reviewTime, int rating, String reviewText) {
        this.reviewerName = reviewerName;
        this.reviewTime = (reviewTime != null) ? reviewTime : getCurrentTime(); // Default to current time if null
        this.rating = (rating >= 1 && rating <= 5) ? rating : 0; // Ensure rating is between 1 and 5
        this.reviewText = reviewText != null ? reviewText : "No review text provided"; // Default text if null
    }

    // Getters
    public String getReviewerName() {
        return reviewerName != null ? reviewerName : "Anonymous";
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public int getRating() {
        return rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    // Convert to a string for easy printing
    @Override
    public String toString() {
        return "Review{" +
                "reviewerName='" + reviewerName + '\'' +
                ", reviewTime='" + reviewTime + '\'' +
                ", rating=" + rating +
                ", reviewText='" + reviewText + '\'' +
                '}';
    }

    // Helper method to get the current time in a specific format (e.g., ISO 8601)
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
