package com.EmergencyAlertApplication.EAA.Repositories;

import com.EmergencyAlertApplication.EAA.Entities.Ticket;
import com.EmergencyAlertApplication.EAA.Entities.TicketToAnimalTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketToAnimalTagRepository extends JpaRepository<TicketToAnimalTag, UUID> {
    List<TicketToAnimalTag> findByTicketId(UUID id);

    void deleteByTicketId(UUID id);

    List<TicketToAnimalTag> findByTicket(Ticket ticket);
}
