package com.example.homebabysit;

public class Babysitter {
    private int id;
    private String name;
    private String email;
    private String location;
    private String qualifications;
    private int experience;
    private double hourlyRate;
    private String availability;
    private double averageRating;

    // Full constructor
    public Babysitter(int id, String name, String location, int experience, double hourlyRate, String availability, double averageRating) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.experience = experience;
        this.hourlyRate = hourlyRate;
        this.availability = availability;
        this.averageRating = averageRating;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
