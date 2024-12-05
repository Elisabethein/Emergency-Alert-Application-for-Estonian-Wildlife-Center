package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.Entities.Picture;
import com.EmergencyAlertApplication.EAA.Entities.PictureToTicket;
import com.EmergencyAlertApplication.EAA.Entities.Ticket;
import com.EmergencyAlertApplication.EAA.Repositories.PictureToTicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PictureToTicketService {
    private final PictureToTicketRepository pictureToTicketRepository;

    public PictureToTicketService(PictureToTicketRepository pictureToTicketRepository) {
        this.pictureToTicketRepository = pictureToTicketRepository;
    }

    public void linkPictureToTicket(Picture picture, Ticket ticket) {
        PictureToTicket pictureToTicket = new PictureToTicket();
        pictureToTicket.setPicture(picture);
        pictureToTicket.setTicket(ticket);
        pictureToTicketRepository.save(pictureToTicket);
    }

    public List<PictureToTicket> findAllPicturesForTicket(UUID ticketId) {
        return pictureToTicketRepository.findByTicketId(ticketId);
    }

    public void deleteByTicketId(UUID id) {
        pictureToTicketRepository.deleteByTicketId(id);
    }
}
