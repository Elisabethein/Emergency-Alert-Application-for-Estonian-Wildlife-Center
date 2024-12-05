package com.EmergencyAlertApplication.EAA.DTOs;

public class SpeciesDTO {
    private String name;
    private boolean isExpert;

    public SpeciesDTO() {
    }
    public SpeciesDTO(String name, boolean isExpert) {
        this.name = name;
        this.isExpert = isExpert;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExpert() {
        return isExpert;
    }

    public void setExpert(boolean expert) {
        isExpert = expert;
    }
}

