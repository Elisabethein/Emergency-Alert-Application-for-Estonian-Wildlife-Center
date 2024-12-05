package com.EmergencyAlertApplication.EAA.Entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "resolution")
public class Resolution {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    public Resolution(UUID id, String resolution) {
        this.id = id;
        this.name = resolution;
    }

    public Resolution() {
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

    public void setName(String resolution) {
        this.name = resolution;
    }
}
