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

    // Full constructor including email and qualifications
    public Babysitter(int id, String name, String email, String location, String qualifications, int experience, double hourlyRate, String availability, double averageRating) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.location = location;
        this.qualifications = qualifications;
        this.experience = experience >= 0 ? experience : 0; // Ensure experience is non-negative
        this.hourlyRate = hourlyRate;
        this.availability = availability != null ? availability : "Not Available"; // Default value for availability
        this.averageRating = averageRating >= 0 ? averageRating : 0.0; // Ensure valid average rating
    }

    // Getters and setters with basic validation
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name != null ? name : "No Name Provided";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email != null ? email : "No Email Provided";
    }

    public void setEmail(String email) {
        if (isValidEmail(email)) {
            this.email = email;
        } else {
            this.email = "Invalid Email"; // Default to invalid if the email is incorrect
        }
    }

    // Basic email validation (basic regex check)
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    public String getLocation() {
        return location != null ? location : "No Location Provided";
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getQualifications() {
        return qualifications != null ? qualifications : "No Qualifications Provided";
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        if (experience >= 0) {
            this.experience = experience;
        } else {
            this.experience = 0; // Ensure non-negative experience
        }
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
        this.availability = availability != null ? availability : "Not Available";
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        if (averageRating >= 0) {
            this.averageRating = averageRating;
        } else {
            this.averageRating = 0.0; // Default to 0 if average rating is negative
        }
    }

    @Override
    public String toString() {
        return "Babysitter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", qualifications='" + qualifications + '\'' +
                ", experience=" + experience +
                ", hourlyRate=" + hourlyRate +
                ", availability='" + availability + '\'' +
                ", averageRating=" + averageRating +
                '}';
    }

    public Babysitter(int id, String name, String location, int experience, double hourlyRate, String availability, double averageRating) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.experience = experience;
        this.hourlyRate = hourlyRate;
        this.availability = availability != null ? availability : "Not Available";
        this.averageRating = averageRating;
        this.email = "Not Provided"; // Default value
        this.qualifications = "Not Provided"; // Default value
    }

}
