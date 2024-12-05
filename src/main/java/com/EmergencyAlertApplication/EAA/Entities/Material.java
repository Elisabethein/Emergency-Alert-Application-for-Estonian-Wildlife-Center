package com.EmergencyAlertApplication.EAA.Entities;

import jakarta.persistence.*;

@Entity
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String link;
    private String description;
    private boolean generalMaterial;
    private String selectedSpeciesGroup;

    public Material(Long id, String title, String link, String description, boolean generalMaterial, String selectedSpeciesGroup) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.description = description;
        this.generalMaterial = generalMaterial;
        this.selectedSpeciesGroup = selectedSpeciesGroup;
    }

    public Material() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getGeneralMaterial() {
        return generalMaterial;
    }

    public void setGeneralMaterial(boolean generalMaterial) {
        this.generalMaterial = generalMaterial;
    }

    public String getSelectedSpeciesGroup() {
        return selectedSpeciesGroup;
    }

    public void setSelectedSpeciesGroup(String selectedSpeciesGroup) {
        this.selectedSpeciesGroup = selectedSpeciesGroup;
    }
}
