package com.EmergencyAlertApplication.EAA.DTOs;

public class NewUpperSpeciesDTO {
    private String name;

    public NewUpperSpeciesDTO() {
    }
    public NewUpperSpeciesDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
