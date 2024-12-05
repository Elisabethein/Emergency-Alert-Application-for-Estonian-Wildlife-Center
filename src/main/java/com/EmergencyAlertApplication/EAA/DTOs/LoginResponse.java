package com.EmergencyAlertApplication.EAA.DTOs;

import com.EmergencyAlertApplication.EAA.Entities.User;

import java.util.List;

public class LoginResponse {
    private User user;
    private List<String> roles;
    private List<String> regions;
    private String token;
    public LoginResponse() {
    }
    public LoginResponse(User user, List<String> roles, List<String> regions, String token) {
        this.user = user;
        this.roles = roles;
        this.regions = regions;
        this.token = token;
    }

    // Getters and Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getRegions() {
        return regions;
    }

    public void setRegions(List<String> regions) {
        this.regions = regions;
    }
}
