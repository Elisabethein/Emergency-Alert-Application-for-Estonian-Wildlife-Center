package com.EmergencyAlertApplication.EAA.Repositories;

import com.EmergencyAlertApplication.EAA.Entities.PictureToTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PictureToTicketRepository extends JpaRepository<PictureToTicket, UUID> {
    List<PictureToTicket> findByTicketId(UUID ticketId);

    void deleteByTicketId(UUID id);
}
