package com.EmergencyAlertApplication.EAA.DTOs;

import com.EmergencyAlertApplication.EAA.Entities.*;

import java.util.List;

public class EditTicketDTO {
    private String closeDate;
    private String createdAt;
    private String describedAnimal;
    private String description;
    private String directions;
    private String history;
    private String id;
    private double latitude;
    private String location;
    private double longitude;
    private String notificationSource;
    private String openDate;
    private RegionDTO region;
    private Boolean reporterCanWait;
    private String reporterEmail;
    private String reporterName;
    private String reporterPhone;
    private String resolution;
    private String species;
    private String status;
    private List<String> tags;
    private boolean transportPossibility;
    private String upperSpecies;
    private List<User> volunteers;
    private boolean hospital;

    public EditTicketDTO(String closeDate, String createdAt, String describedAnimal, String description, String directions, String history, String id, double latitude, String location, double longitude, String notificationSource, String openDate, RegionDTO region, Boolean reporterCanWait, String reporterEmail, String reporterName, String reporterPhone, String resolution, String species, String status, List<String> tags, boolean transportPossibility, String upperSpecies, List<User> volunteers, boolean hospital) {
        this.closeDate = closeDate;
        this.createdAt = createdAt;
        this.describedAnimal = describedAnimal;
        this.description = description;
        this.directions = directions;
        this.history = history;
        this.id = id;
        this.latitude = latitude;
        this.location = location;
        this.longitude = longitude;
        this.notificationSource = notificationSource;
        this.openDate = openDate;
        this.region = region;
        this.reporterCanWait = reporterCanWait;
        this.reporterEmail = reporterEmail;
        this.reporterName = reporterName;
        this.reporterPhone = reporterPhone;
        this.resolution = resolution;
        this.species = species;
        this.status = status;
        this.tags = tags;
        this.transportPossibility = transportPossibility;
        this.upperSpecies = upperSpecies;
        this.volunteers = volunteers;
        this.hospital = hospital;
    }
    public EditTicketDTO(){

    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescribedAnimal() {
        return describedAnimal;
    }

    public void setDescribedAnimal(String describedAnimal) {
        this.describedAnimal = describedAnimal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNotificationSource() {
        return notificationSource;
    }

    public void setNotificationSource(String notificationSource) {
        this.notificationSource = notificationSource;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public RegionDTO getRegion() {
        return region;
    }

    public void setRegion(RegionDTO region) {
        this.region = region;
    }

    public Boolean getReporterCanWait() {
        return reporterCanWait;
    }

    public void setReporterCanWait(Boolean reporterCanWait) {
        this.reporterCanWait = reporterCanWait;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReporterPhone() {
        return reporterPhone;
    }

    public void setReporterPhone(String reporterPhone) {
        this.reporterPhone = reporterPhone;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isTransportPossibility() {
        return transportPossibility;
    }

    public void setTransportPossibility(boolean transportPossibility) {
        this.transportPossibility = transportPossibility;
    }

    public String getUpperSpecies() {
        return upperSpecies;
    }

    public void setUpperSpecies(String upperSpecies) {
        this.upperSpecies = upperSpecies;
    }

    public List<User> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(List<User> volunteers) {
        this.volunteers = volunteers;
    }

    public boolean isHospital() {
        return hospital;
    }

    public void setHospital(boolean hospital) {
        this.hospital = hospital;
    }
}