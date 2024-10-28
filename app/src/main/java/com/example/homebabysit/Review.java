package com.example.homebabysit;

public class Review {
    private String reviewerName;
    private String reviewTime;
    private int rating;
    private String reviewText;

    public Review(String reviewerName, String reviewTime, int rating, String reviewText) {
        this.reviewerName = reviewerName;
        this.reviewTime = reviewTime;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    // Getter methods
    public String getReviewerName() {
        return reviewerName;
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

    @Override
    public String toString() {
        return "Review{" +
                "reviewerName='" + reviewerName + '\'' +
                ", reviewTime='" + reviewTime + '\'' +
                ", rating=" + rating +
                ", reviewText='" + reviewText + '\'' +
                '}';
    }
}
