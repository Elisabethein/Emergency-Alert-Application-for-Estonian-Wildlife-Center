package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.Services.TicketToAnimalTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ticketToAnimalTags")
public class TicketToAnimalTagController {
    private final TicketToAnimalTagService ticketToAnimalTagService;

    @Autowired
    public TicketToAnimalTagController(TicketToAnimalTagService ticketToAnimalTagService) {
        this.ticketToAnimalTagService = ticketToAnimalTagService;
    }
}
