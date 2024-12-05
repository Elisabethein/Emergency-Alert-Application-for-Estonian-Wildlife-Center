package com.EmergencyAlertApplication.EAA.DTOs;

public class NewResolutionDTO {
    private String name;

    public NewResolutionDTO() {
    }

    public NewResolutionDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
