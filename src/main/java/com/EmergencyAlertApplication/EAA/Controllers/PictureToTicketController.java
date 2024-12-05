package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.Entities.Picture;
import com.EmergencyAlertApplication.EAA.Entities.PictureToTicket;
import com.EmergencyAlertApplication.EAA.Services.PictureService;
import com.EmergencyAlertApplication.EAA.Services.PictureToTicketService;
import com.EmergencyAlertApplication.EAA.Services.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/picturesToTickets")
public class PictureToTicketController {
    private final TicketService ticketService;
    private final PictureToTicketService pictureToTicketService;
    private final PictureService pictureService;

    public PictureToTicketController(TicketService ticketService, PictureToTicketService pictureToTicketService, PictureService pictureService) {
        this.ticketService = ticketService;
        this.pictureToTicketService = pictureToTicketService;
        this.pictureService = pictureService;
    }
    @GetMapping("/all/{ticketId}")
    public ResponseEntity<List<Picture>> getAllPicturesForTicket(@PathVariable UUID ticketId) {
        try {
            List<PictureToTicket> picturesToTickets = pictureToTicketService.findAllPicturesForTicket(ticketId);
            List<Picture> pictures = new ArrayList<>();
            for (PictureToTicket pictureToTicket : picturesToTickets) {
                pictures.add(pictureToTicket.getPicture());
            }
            return ResponseEntity.ok(pictures);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
