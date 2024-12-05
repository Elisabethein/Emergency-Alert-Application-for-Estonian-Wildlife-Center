package com.EmergencyAlertApplication.EAA.Entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "usersToSpecies")
public class UserToSpecies {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private boolean isExpert;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "upperSpecies_id")
    private UpperSpecies upperSpecies;

    public UserToSpecies() {
    }

    public UserToSpecies(UUID id, Boolean isExpert, User user, UpperSpecies upperSpecies) {
        this.id = id;
        this.isExpert = isExpert;
        this.user = user;
        this.upperSpecies = upperSpecies;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isExpert() {
        return isExpert;
    }

    public void setExpert(boolean expert) {
        isExpert = expert;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UpperSpecies getUpperSpecies() {
        return upperSpecies;
    }

    public void setUpperSpecies(UpperSpecies upperSpecies) {
        this.upperSpecies = upperSpecies;
    }
}
