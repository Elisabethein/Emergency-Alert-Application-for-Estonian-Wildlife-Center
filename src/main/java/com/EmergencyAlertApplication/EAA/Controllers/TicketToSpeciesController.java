package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.Services.TicketToSpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticketToSpecies")
public class TicketToSpeciesController {

    private final TicketToSpeciesService ticketToSpeciesService;

    @Autowired
    public TicketToSpeciesController(TicketToSpeciesService ticketToSpeciesService) {
        this.ticketToSpeciesService = ticketToSpeciesService;
    }
}
