package com.EmergencyAlertApplication.EAA.DTOs;

import java.time.LocalDate;
import java.util.List;

public class StatisticsTableDTO {
    private LocalDate date;
    private String upperSpecies;
    private String species;
    private String region;
    private List<String> injuries;
    private String resolution;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUpperSpecies() {
        return upperSpecies;
    }

    public void setUpperSpecies(String upperSpecies) {
        this.upperSpecies = upperSpecies;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<String> getInjuries() {
        return injuries;
    }

    public void setInjuries(List<String> injuries) {
        this.injuries = injuries;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
