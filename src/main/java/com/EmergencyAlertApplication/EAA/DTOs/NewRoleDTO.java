package com.EmergencyAlertApplication.EAA.DTOs;

public class NewRoleDTO {
    private String name;
    public NewRoleDTO() {
    }
    public NewRoleDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
