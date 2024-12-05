package com.EmergencyAlertApplication.EAA.DTOs;

import java.util.List;

public class FiltersDTO {
    private String ajaperioodiAlgus; // Start date
    private String ajaperioodiLopp; // End date
    private List<String> liigigrupp; // Upper species
    private List<String> loomaLiik; // Species
    private List<String> piirkond; // Region
    private List<String> vigastus; // Injury
    private List<String> lahendus;

    public FiltersDTO(String ajaperioodiAlgus, String ajaperioodiLopp, List<String> liigigrupp, List<String> loomaLiik, List<String> piirkond, List<String> vigastus, List<String> lahendus) {
        this.ajaperioodiAlgus = ajaperioodiAlgus;
        this.ajaperioodiLopp = ajaperioodiLopp;
        this.liigigrupp = liigigrupp;
        this.loomaLiik = loomaLiik;
        this.piirkond = piirkond;
        this.vigastus = vigastus;
        this.lahendus = lahendus;
    }

    public String getAjaperioodiAlgus() {
        return ajaperioodiAlgus;
    }

    public void setAjaperioodiAlgus(String ajaperioodiAlgus) {
        this.ajaperioodiAlgus = ajaperioodiAlgus;
    }

    public String getAjaperioodiLopp() {
        return ajaperioodiLopp;
    }

    public void setAjaperioodiLopp(String ajaperioodiLopp) {
        this.ajaperioodiLopp = ajaperioodiLopp;
    }

    public List<String> getLiigigrupp() {
        return liigigrupp;
    }

    public void setLiigigrupp(List<String> liigigrupp) {
        this.liigigrupp = liigigrupp;
    }

    public List<String> getLoomaLiik() {
        return loomaLiik;
    }

    public void setLoomaLiik(List<String> loomaLiik) {
        this.loomaLiik = loomaLiik;
    }

    public List<String> getPiirkond() {
        return piirkond;
    }

    public void setPiirkond(List<String> piirkond) {
        this.piirkond = piirkond;
    }

    public List<String> getVigastus() {
        return vigastus;
    }

    public void setVigastus(List<String> vigastus) {
        this.vigastus = vigastus;
    }

    public List<String> getLahendus() {
        return lahendus;
    }

    public void setLahendus(List<String> lahendus) {
        this.lahendus = lahendus;
    }
}
