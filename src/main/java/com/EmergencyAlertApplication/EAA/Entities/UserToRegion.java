package com.EmergencyAlertApplication.EAA.Entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "usersToRegions")
public class UserToRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    private Region region;

    public UserToRegion() {
    }

    public UserToRegion(UUID id, User user, Region region) {
        this.id = id;
        this.user = user;
        this.region = region;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
