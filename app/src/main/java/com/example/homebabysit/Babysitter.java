package com.example.homebabysit;

public class Babysitter {
    private int id;
    private String name;
    private String experience;
    private String location;
    private int age;
    private double rating;

    // Constructor with location
    public Babysitter(int id, String name, String experience, String location, int age, double rating) {
        this.id = id;
        this.name = name;
        this.experience = experience;
        this.location = location;
        this.age = age;
        this.rating = rating;
    }

    // Constructor without location (default location is set to "Unknown")
    public Babysitter(int id, String name, String experience, int age, double rating) {
        this.id = id;
        this.name = name;
        this.experience = experience;
        this.age = age;
        this.rating = rating;
        this.location = "Unknown"; // Default value for location
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExperience() {
        return experience;
    }

    public String getLocation() {
        return location;
    }

    public int getAge() {
        return age;
    }

    public double getRating() {
        return rating;
    }

    // Optional: Setters if you need to update any attributes later
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
