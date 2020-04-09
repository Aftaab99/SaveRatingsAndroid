package com.example.ratings.storeratings;

public class Rating {
    public long timestamp;
    public int stars, totalStars;

    public Rating(long timestamp, int stars, int totalStars) {
        this.timestamp = timestamp;
        this.stars = stars;
        this.totalStars = totalStars;
    }
}
