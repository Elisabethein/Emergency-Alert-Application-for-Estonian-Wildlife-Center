package com.EmergencyAlertApplication.EAA.Entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "applicationToTags")
public class ApplicationToTag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "application_id", referencedColumnName = "id")
    private Application application;

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    private Tag tag;

    public ApplicationToTag() {
    }

    public ApplicationToTag(UUID id, Application application, Tag tag) {
        this.id = id;
        this.application = application;
        this.tag = tag;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
