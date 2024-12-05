package com.EmergencyAlertApplication.EAA.DTOs;

public class NewSpeciesDTO {
    private String name;
    private String upperSpeciesName;

    public NewSpeciesDTO() {
    }
    public NewSpeciesDTO(String name, String upperSpeciesName) {
        this.name = name;
        this.upperSpeciesName = upperSpeciesName;
    }

    public String getName() {
        return name;
    }

    public String getUpperSpeciesName() {
        return upperSpeciesName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUpperSpeciesName(String upperSpeciesName) {
        this.upperSpeciesName = upperSpeciesName;
    }
}
