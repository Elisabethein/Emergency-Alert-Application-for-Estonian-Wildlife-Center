package com.EmergencyAlertApplication.EAA.Entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "species")
public class Species {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    @ManyToOne
    @JoinColumn(name = "upperSpecies_id", referencedColumnName = "id")
    private UpperSpecies upperSpecies;

    public Species() {
    }

    public Species(UUID id, String name, UpperSpecies upperSpecies) {
        this.id = id;
        this.name = name;
        this.upperSpecies = upperSpecies;
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

    public UpperSpecies getUpperSpecies() {
        return upperSpecies;
    }

    public void setUpperSpecies(UpperSpecies upperSpecies) {
        this.upperSpecies = upperSpecies;
    }
}
