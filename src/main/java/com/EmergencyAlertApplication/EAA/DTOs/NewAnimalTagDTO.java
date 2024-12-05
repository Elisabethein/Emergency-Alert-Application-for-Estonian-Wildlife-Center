package com.EmergencyAlertApplication.EAA.DTOs;

public class NewAnimalTagDTO {
    private String name;
    public NewAnimalTagDTO(){

    }

    public NewAnimalTagDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
