package com.example.homebabysit;

public class Babysitter {
    private String name;
    private String experience;
    private String location;

    public Babysitter(String name, String experience, String location) {
        this.name = name;
        this.experience = experience;
        this.location = location;
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
}
