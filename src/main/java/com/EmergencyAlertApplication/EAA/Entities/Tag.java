package com.EmergencyAlertApplication.EAA.Entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String function;

    public Tag() {
    }

    public Tag(UUID id, String function) {
        this.id = id;
        this.function = function;
    }
    public String getFunction() {
        return function;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
