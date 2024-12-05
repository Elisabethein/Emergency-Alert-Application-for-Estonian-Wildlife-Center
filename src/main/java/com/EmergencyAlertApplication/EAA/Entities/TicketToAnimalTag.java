package com.EmergencyAlertApplication.EAA.Entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ticketsToAnimalTags")
public class TicketToAnimalTag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private AnimalTag animalTag;

    public TicketToAnimalTag(){
    }

    public TicketToAnimalTag(UUID id, Ticket ticket, AnimalTag animalTag) {
        this.id = id;
        this.ticket = ticket;
        this.animalTag = animalTag;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public AnimalTag getAnimalTag() {
        return animalTag;
    }

    public void setAnimalTag(AnimalTag animalTag) {
        this.animalTag = animalTag;
    }
}
