package com.EmergencyAlertApplication.EAA.DTOs;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class AlertDTO {
    private String reason;            // What happened
    private String animals;           // Which animal
    private String location;          // Where it happened
    private String directions;        // Directions
    private String firstName;         // First name
    private String lastName;          // Last name
    private String phone;             // Phone number
    private String email;             // Email address
    private boolean canWait;          // Can wait at location
    private List<MultipartFile> pictures; // List of image files
    private Double latitude;           // Latitude
    private Double longitude;          // Longitude

    private String socialMedia;       // Social media

    public AlertDTO() {
    }

    public AlertDTO(String reason, String animals, String location, String directions, String firstName,
                    String lastName, String phone, String email, boolean canWait, List<MultipartFile> pictures,
                    Double latitude, Double longitude, String socialMedia) {
        this.reason = reason;
        this.animals = animals;
        this.location = location;
        this.directions = directions;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.canWait = canWait;
        this.pictures = pictures;
        this.latitude = latitude;
        this.longitude = longitude;
        this.socialMedia = socialMedia;
    }

    // Getters and setters

    public String getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(String socialMedia) {
        this.socialMedia = socialMedia;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAnimals() {
        return animals;
    }

    public void setAnimals(String animals) {
        this.animals = animals;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isCanWait() {
        return canWait;
    }

    public void setCanWait(boolean canWait) {
        this.canWait = canWait;
    }

    public List<MultipartFile> getPictures() {
        return pictures;
    }

    public void setPictures(List<MultipartFile> pictures) {
        this.pictures = pictures;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
