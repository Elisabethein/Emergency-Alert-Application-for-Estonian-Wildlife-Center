package com.EmergencyAlertApplication.EAA.Entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "animalTags")
public class AnimalTag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String injury;

    public AnimalTag() {
    }
    public AnimalTag(UUID id, String injury) {
        this.id = id;
        this.injury = injury;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getInjury() {
        return injury;
    }

    public void setInjury(String injury) {
        this.injury = injury;
    }
}
