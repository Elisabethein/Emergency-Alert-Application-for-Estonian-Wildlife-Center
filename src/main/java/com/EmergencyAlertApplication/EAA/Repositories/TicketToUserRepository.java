package com.EmergencyAlertApplication.EAA.Repositories;

import com.EmergencyAlertApplication.EAA.Entities.Ticket;
import com.EmergencyAlertApplication.EAA.Entities.TicketToUser;
import com.EmergencyAlertApplication.EAA.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketToUserRepository extends JpaRepository<TicketToUser, UUID> {

    List<TicketToUser> findByTicketId(UUID id);

    void deleteByTicketId(UUID id);

    List<TicketToUser> findByTicketAndUser(Ticket ticket, User user);

    List<TicketToUser> findByTicket(Ticket ticket);
    List<TicketToUser> findByUserId(UUID user_id);

    void deleteByUserId(UUID id);
}
