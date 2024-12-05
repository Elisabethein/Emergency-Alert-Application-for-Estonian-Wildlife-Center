package com.EmergencyAlertApplication.EAA.Entities;

import jakarta.persistence.*;

import java.util.UUID;


@Entity
@Table(name = "pictures")
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String driveId;

    private String name;

    public Picture(UUID id, String driveId, String name) {
        this.id = id;
        this.driveId = driveId;
        this.name = name;
    }

    public Picture() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriveId() {
        return driveId;
    }

    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }
}
