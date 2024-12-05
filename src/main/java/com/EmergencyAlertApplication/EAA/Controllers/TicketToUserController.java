package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.Entities.Status;
import com.EmergencyAlertApplication.EAA.Entities.Ticket;
import com.EmergencyAlertApplication.EAA.Entities.TicketToUser;
import com.EmergencyAlertApplication.EAA.Entities.User;
import com.EmergencyAlertApplication.EAA.Services.StatusService;
import com.EmergencyAlertApplication.EAA.Services.TicketService;
import com.EmergencyAlertApplication.EAA.Services.TicketToUserService;
import com.EmergencyAlertApplication.EAA.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ticketToUsers")
public class TicketToUserController {

    private final TicketToUserService ticketToUserService;
    private final UserService userService;
    private final TicketService ticketService;
    private final StatusService statusService;

    @Autowired
    public TicketToUserController(TicketToUserService ticketToUserService, UserService userService, TicketService ticketService, StatusService statusService) {
        this.ticketToUserService = ticketToUserService;
        this.userService = userService;
        this.ticketService = ticketService;
        this.statusService = statusService;
    }

    @GetMapping("/all/{UserId}")
    public ResponseEntity<List<Ticket>> getTicketToUserByUserID(@PathVariable("UserId") UUID id) {
        try {
            List<Ticket> tickets = ticketToUserService.getTicketToUserByUserID(id);
            if (tickets == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @GetMapping("{ticketID}/users")
    public ResponseEntity<List<User>> getTicketToUserByTicketID(@PathVariable("ticketID") UUID id) {
        try {
            List<User> users = ticketToUserService.getTicketToUserByTicketID(id);
            if (users == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/add/{ticketID}/{userID}")
    public ResponseEntity<TicketToUser> addTicketToUser(@PathVariable("ticketID") UUID ticketID, @PathVariable("userID") UUID userID) {
        try {
            User user = userService.getUserById(userID);
            Ticket ticket = ticketService.getTicketById(ticketID);
            if (user == null || ticket == null) return ResponseEntity.notFound().build();

            TicketToUser ticketToUser = new TicketToUser();
            ticketToUser.setTicket(ticket);
            ticketToUser.setUser(user);
            ticketToUserService.save(ticketToUser);

            Status status = statusService.findByName("Hoiukodus");
            ticket.setStatus(status);
            ticketService.save(ticket);

            return ResponseEntity.ok(ticketToUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
