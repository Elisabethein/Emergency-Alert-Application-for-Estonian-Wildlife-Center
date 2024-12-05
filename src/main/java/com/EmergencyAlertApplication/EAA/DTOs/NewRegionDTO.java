package com.EmergencyAlertApplication.EAA.DTOs;

public class NewRegionDTO {
    private String name;

    public NewRegionDTO() {
    }
    public NewRegionDTO(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
