package com.EmergencyAlertApplication.EAA.Entities;
import jakarta.persistence.*;

import java.util.UUID;
@Entity
@Table(name = "pictureToTicket")
public class PictureToTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "picture_id", referencedColumnName = "id", nullable = false)
    private Picture picture;

    @ManyToOne
    @JoinColumn(name = "ticket_id", referencedColumnName = "id", nullable = false)
    private Ticket ticket;

    public PictureToTicket(UUID id, Picture picture, Ticket ticket) {
        this.id = id;
        this.picture = picture;
        this.ticket = ticket;
    }
    public PictureToTicket() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
