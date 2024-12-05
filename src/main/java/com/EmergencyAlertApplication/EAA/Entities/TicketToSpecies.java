package com.EmergencyAlertApplication.EAA.Entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ticketsToSpecies")
public class TicketToSpecies {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "species_id")
    private Species species;

    public TicketToSpecies() {
    }

    public TicketToSpecies(UUID id, Ticket ticket, Species species) {
        this.id = id;
        this.ticket = ticket;
        this.species = species;
    }

}
