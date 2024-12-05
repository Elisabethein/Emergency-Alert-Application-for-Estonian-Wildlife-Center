package com.EmergencyAlertApplication.EAA.DTOs;

public class NewTagDTO {
    private String name;

    public NewTagDTO() {
    }
    public NewTagDTO(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
