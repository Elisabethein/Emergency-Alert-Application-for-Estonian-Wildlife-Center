package com.EmergencyAlertApplication.EAA.Entities;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "species_id")
    private Species species;

    @ManyToOne
    @JoinColumn(name = "upper_species_id")
    private UpperSpecies upperSpecies;

    @ManyToOne
    @JoinColumn(name = "county_id")
    private Region region;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "resolution_id")
    private Resolution resolution;

    private Timestamp openDate;
    private Timestamp closeDate;
    private String description;
    private String describedAnimal;
    private String location;
    private Double latitude;
    private Double longitude;
    private String directions;
    private boolean transportPossibility;
    private boolean reporterCanWait;
    private String reporterName;
    private String reporterPhone;
    private String reporterEmail;
    private String reporterSocialMedia;
    private String notificationSource;
    private Timestamp createdAt;
    private String history;
    private boolean hospital;

    public Ticket() {
    }

    public Ticket(UUID id, Species species, UpperSpecies upperSpecies, Region region, Status status, Resolution resolution, Timestamp openDate, Timestamp closeDate, String description, String describedAnimal, String location, Double latitude, Double longitude, String directions, boolean transportPossibility, boolean reporterCanWait, String reporterName, String reporterPhone, String reporterEmail, String reporterSocialMedia, String notificationSource, Timestamp createdAt, String history, boolean hospital) {
        this.id = id;
        this.species = species;
        this.upperSpecies = upperSpecies;
        this.region = region;
        this.status = status;
        this.resolution = resolution;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.description = description;
        this.describedAnimal = describedAnimal;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.directions = directions;
        this.transportPossibility = transportPossibility;
        this.reporterCanWait = reporterCanWait;
        this.reporterName = reporterName;
        this.reporterPhone = reporterPhone;
        this.reporterEmail = reporterEmail;
        this.reporterSocialMedia = reporterSocialMedia;
        this.notificationSource = notificationSource;
        this.createdAt = createdAt;
        this.history = history;
        this.hospital = hospital;
    }

    public String getReporterSocialMedia() {
        return reporterSocialMedia;
    }

    public void setReporterSocialMedia(String reporterSocialMedia) {
        this.reporterSocialMedia = reporterSocialMedia;
    }

    public String getDescribedAnimal() {
        return describedAnimal;
    }

    public void setDescribedAnimal(String describedAnimal) {
        this.describedAnimal = describedAnimal;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public UpperSpecies getUpperSpecies() {
        return upperSpecies;
    }

    public void setUpperSpecies(UpperSpecies upperSpecies) {
        this.upperSpecies = upperSpecies;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public Timestamp getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Timestamp openDate) {
        this.openDate = openDate;
    }

    public Timestamp getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Timestamp closeDate) {
        this.closeDate = closeDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public boolean isTransportPossibility() {
        return transportPossibility;
    }

    public void setTransportPossibility(boolean transportPossibility) {
        this.transportPossibility = transportPossibility;
    }

    public boolean isReporterCanWait() {
        return reporterCanWait;
    }

    public void setReporterCanWait(boolean reporterCanWait) {
        this.reporterCanWait = reporterCanWait;
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

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    public String getNotificationSource() {
        return notificationSource;
    }

    public void setNotificationSource(String notificationSource) {
        this.notificationSource = notificationSource;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public boolean isHospital() {
        return hospital;
    }

    public void setHospital(boolean hospital) {
        this.hospital = hospital;
    }
}
