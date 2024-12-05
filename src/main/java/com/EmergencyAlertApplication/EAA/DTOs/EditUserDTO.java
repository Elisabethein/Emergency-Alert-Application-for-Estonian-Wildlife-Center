package com.EmergencyAlertApplication.EAA.DTOs;

import java.sql.Date;
import java.util.List;

public class EditUserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private Date birthDate; // or String if you prefer handling date as String
    private String email;
    private String phoneNr;
    private String county;
    private String city;
    private String streetName;
    private String streetNr;
    private String postalCode;
    private String createdAt;
    private List<String> tags; // Assuming tags are stored as a List of Strings
    private List<String> regions; // Assuming regions are stored as a List of Strings
    private List<String> roles; // Assuming roles are stored as a List of Strings
    private List<SpeciesDTO> species;
    private String password;

    public EditUserDTO() {
    }
    public EditUserDTO(String id, String firstName, String lastName, Date birthDate, String email, String phoneNr, String county, String city, String streetName, String streetNr, String postalCode, String createdAt, List<String> tags, List<String> regions, List<String> roles, List<SpeciesDTO> species, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNr = phoneNr;
        this.county = county;
        this.city = city;
        this.streetName = streetName;
        this.streetNr = streetNr;
        this.postalCode = postalCode;
        this.createdAt = createdAt;
        this.tags = tags;
        this.regions = regions;
        this.roles = roles;
        this.species = species;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNr() {
        return streetNr;
    }

    public void setStreetNr(String streetNr) {
        this.streetNr = streetNr;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getRegions() {
        return regions;
    }

    public void setRegions(List<String> regions) {
        this.regions = regions;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<SpeciesDTO> getSpecies() {
        return species;
    }

    public void setSpecies(List<SpeciesDTO> species) {
        this.species = species;
    }
}
