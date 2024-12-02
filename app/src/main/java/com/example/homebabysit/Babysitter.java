package com.example.homebabysit;

public class Babysitter {
    private int id;
    private String name;
    private String location;
    private int experience;
    private double hourlyRate;
    private String availability;
    private double averageRating;

    // Constructor with all attributes
    public Babysitter(int id, String name, String location, int experience, double hourlyRate,
                      String availability, double averageRating) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.experience = experience;
        this.hourlyRate = hourlyRate;
        this.availability = availability;
        this.averageRating = averageRating;
    }

    // Constructor with default values for some attributes
    public Babysitter(int id, String name, int experience) {
        this.id = id;
        this.name = name;
        this.experience = experience;
        this.location = "Unknown"; // Default value
        this.hourlyRate = 0.0; // Default value
        this.availability = "Not specified"; // Default value
        this.averageRating = 0.0; // Default value
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getExperience() {
        return experience;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public String getAvailability() {
        return availability;
    }

    public double getAverageRating() {
        return averageRating;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
