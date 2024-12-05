package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.DTOs.AlertDTO;
import com.EmergencyAlertApplication.EAA.DTOs.EditMyTicketDTO;
import com.EmergencyAlertApplication.EAA.DTOs.EditTicketDTO;
import com.EmergencyAlertApplication.EAA.Entities.*;
import com.EmergencyAlertApplication.EAA.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final TicketToAnimalTagService ticketToAnimalTagService;
    private final TicketToUserService ticketToUserService;
    private final SpeciesService speciesService; // Service to fetch species
    private final ResolutionService resolutionService; // Service to fetch resolutions
    private final StatusService statusService; // Service to fetch statuses
    private final UpperSpeciesService upperSpeciesService; // Service to fetch upper species
    private final RegionService regionService;
    private final AnimalTagService animalTagService;
    private final PictureToTicketService pictureToTicketService;
    private final TicketToSpeciesService ticketToSpeciesService;


    @Autowired
    public TicketController(TicketService ticketService, TicketToAnimalTagService ticketToAnimalTagService, TicketToUserService ticketToUserService, SpeciesService speciesService, ResolutionService resolutionService, StatusService statusService, UpperSpeciesService upperSpeciesService, RegionService regionService, AnimalTagService animalTagService, PictureToTicketService pictureToTicketService, TicketToSpeciesService ticketToSpeciesService) {
        this.ticketService = ticketService;
        this.ticketToAnimalTagService = ticketToAnimalTagService;
        this.ticketToUserService = ticketToUserService;
        this.speciesService = speciesService;
        this.resolutionService = resolutionService;
        this.statusService = statusService;
        this.upperSpeciesService = upperSpeciesService;
        this.regionService = regionService;
        this.animalTagService = animalTagService;
        this.pictureToTicketService = pictureToTicketService;
        this.ticketToSpeciesService = ticketToSpeciesService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        try {
            List<Ticket> tickets = ticketService.getAllTickets();
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Ticket> addTicket(@RequestBody AlertDTO ticket){
        try {
            Ticket newTicket = new Ticket();
            newTicket.setDescription(ticket.getReason());
            newTicket.setDescribedAnimal(ticket.getAnimals());
            newTicket.setLocation(ticket.getLocation());
            newTicket.setDirections(ticket.getDirections());
            newTicket.setReporterName(ticket.getFirstName() + " " + ticket.getLastName());
            newTicket.setReporterPhone(ticket.getPhone());
            newTicket.setReporterEmail(ticket.getEmail());
            newTicket.setReporterCanWait(ticket.isCanWait());
            newTicket.setLatitude(ticket.getLatitude());
            newTicket.setLongitude(ticket.getLongitude());
            newTicket.setStatus(statusService.findByName("Uus"));
            newTicket.setNotificationSource("Forms");
            newTicket.setCreatedAt(Timestamp.valueOf(java.time.LocalDateTime.now()));
            newTicket.setReporterSocialMedia(ticket.getSocialMedia());

            Ticket savedTicket = ticketService.save(newTicket);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTicket);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}/animalTags")
    public ResponseEntity<List<String>> getTicketAnimalTags(@PathVariable UUID id) {
        try {
            List<String> tags = ticketToAnimalTagService.getTicketAnimalTags(id);
            return ResponseEntity.ok(tags);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonList("Error occurred: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<?> getTicketUsers(@PathVariable UUID id) {
        try {
            System.out.println("Fetching users for ticket ID: " + id); // Log the incoming ID
            List<User> users = ticketToUserService.getTicketUsers(id);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage()); // Log the error
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/species")
    public ResponseEntity<List<?>> getTicketSpecies(@PathVariable UUID id) {
        try {
            Ticket ticket = ticketService.getTicketById(id);
            if (ticket == null) {
                return ResponseEntity.notFound().build(); // Return 404 if ticket not found
            }

            // Fetch species associated with the upper species of the ticket
            List<Species> species = speciesService.getAll(ticket.getUpperSpecies().getName());

            if (species == null || species.isEmpty()) {
                return ResponseEntity.notFound().build(); // Return 404 if no species found
            }

            return ResponseEntity.ok(species);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonList("Error occurred: " + e.getMessage())); // Handle server error
        }
    }

    @GetMapping("/{id}/upperSpecies")
    public ResponseEntity<UpperSpecies> getTicketUpperSpecies(@PathVariable UUID id) {
        try {
            Ticket ticket = ticketService.getTicketById(id);
            if (ticket == null) {
                return ResponseEntity.notFound().build(); // Return 404 if ticket not found
            }

            // Fetch the upper species associated with the ticket
            UpperSpecies upperSpecies = upperSpeciesService.findByName(ticket.getUpperSpecies().getName()); // Adjust as needed

            if (upperSpecies == null) {
                return ResponseEntity.notFound().build(); // Return 404 if no upper species found
            }

            return ResponseEntity.ok(upperSpecies);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Handle server error
        }
    }


    @GetMapping("/{id}/resolution")
    public ResponseEntity<Resolution> getTicketResolution(@PathVariable UUID id) {
        try {
            Ticket ticket = ticketService.getTicketById(id);
            if (ticket == null) {
                return ResponseEntity.notFound().build();
            }
            Resolution resolution = ticket.getResolution();
            if (resolution == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(resolution);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Status> getTicketStatus(@PathVariable UUID id) {
        try {
            Ticket ticket = ticketService.getTicketById(id);
            if (ticket == null) {
                return ResponseEntity.notFound().build(); // Return 404 if ticket not found
            }
            Status status = ticket.getStatus();

            if (status == null) {
                return ResponseEntity.notFound().build(); // Return 404 if no status found
            }
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Handle server error
        }
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<Ticket> editTicket(@PathVariable UUID id, @RequestBody EditTicketDTO editTicketDTO) {
        try {
            Ticket ticket = ticketService.getTicketById(id);
            if (ticket == null) {
                return ResponseEntity.notFound().build();
            }

            String startDateString = editTicketDTO.getOpenDate();
            System.out.println("Start date: " + startDateString);
            if (startDateString != null && !Objects.equals(startDateString, "")) {
                Timestamp startDate = Timestamp.valueOf(startDateString + " 00:00:00");
                ticket.setOpenDate(startDate);
            }
            String endDateString = editTicketDTO.getCloseDate();
            System.out.println("End date: " + endDateString);
            if (endDateString != null && !Objects.equals(endDateString, "")) {
                Timestamp endDate = Timestamp.valueOf(endDateString + " 23:59:59");
                ticket.setCloseDate(endDate);
            }

            Status status = statusService.findByName(editTicketDTO.getStatus());
            ticket.setStatus(status);

            if (ticket.getCloseDate() == null && Objects.equals(status.getName(), "Lõpetatud")) {
                ticket.setCloseDate(Timestamp.valueOf(java.time.LocalDateTime.now()));
                if (ticket.getOpenDate() == null) {
                    ticket.setOpenDate(Timestamp.valueOf(java.time.LocalDateTime.now()));
                }
            }
            if (ticket.getOpenDate() == null && Objects.equals(status.getName(), "Avatud") || Objects.equals(status.getName(), "Hoiukodus")){
                ticket.setOpenDate(Timestamp.valueOf(java.time.LocalDateTime.now()));
            }

            ticket.setDescribedAnimal(editTicketDTO.getDescribedAnimal());
            ticket.setDescription(editTicketDTO.getDescription());
            ticket.setDirections(editTicketDTO.getDirections());
            ticket.setHistory(editTicketDTO.getHistory());
            ticket.setLatitude(editTicketDTO.getLatitude());
            ticket.setLongitude(editTicketDTO.getLongitude());
            ticket.setLocation(editTicketDTO.getLocation());
            ticket.setNotificationSource(editTicketDTO.getNotificationSource());
            ticket.setHospital(editTicketDTO.isHospital());


            if (!Objects.equals(editTicketDTO.getRegion().getName(), "")) {
                Region region = regionService.getRegionByName(editTicketDTO.getRegion().getName());
                ticket.setRegion(region);
            }

            ticket.setReporterCanWait(editTicketDTO.getReporterCanWait());
            ticket.setReporterEmail(editTicketDTO.getReporterEmail());
            ticket.setReporterName(editTicketDTO.getReporterName());
            ticket.setReporterPhone(editTicketDTO.getReporterPhone());

            if (editTicketDTO.getResolution() != null && !Objects.equals(editTicketDTO.getResolution(), "")) {
                Resolution resolution = resolutionService.getResolutionByName(editTicketDTO.getResolution());
                ticket.setResolution(resolution);
            }

            if (editTicketDTO.getSpecies() != null && !Objects.equals(editTicketDTO.getSpecies(), "")) {
                Species species = speciesService.getSpeciesByName(editTicketDTO.getSpecies());
                ticket.setSpecies(species);
            }

            List<TicketToAnimalTag> existingTags = ticketToAnimalTagService.findByTicket(ticket);
            for (TicketToAnimalTag existingTag : existingTags) {
                ticketToAnimalTagService.delete(existingTag);
            }
            List<String> tags = editTicketDTO.getTags();
            for (String tag : tags) {
                AnimalTag animalTag = animalTagService.getByAnimalTagInjury(tag);
                TicketToAnimalTag ticketToAnimalTag = new TicketToAnimalTag();
                ticketToAnimalTag.setTicket(ticket);
                ticketToAnimalTag.setAnimalTag(animalTag);
                ticketToAnimalTagService.save(ticketToAnimalTag);
            }

            ticket.setTransportPossibility(editTicketDTO.isTransportPossibility());

            UpperSpecies upperSpecies = upperSpeciesService.findByName(editTicketDTO.getUpperSpecies());
            ticket.setUpperSpecies(upperSpecies);


            List<TicketToUser> existingUsers = ticketToUserService.findByTicket(ticket);
            for (TicketToUser existingUser : existingUsers) {
                ticketToUserService.delete(existingUser);
            }
            List<User> users = editTicketDTO.getVolunteers();
            for (User user : users) {
                TicketToUser ticketToUser = new TicketToUser();
                ticketToUser.setTicket(ticket);
                ticketToUser.setUser(user);
                ticketToUser.setActive(true);
                ticketToUser.setAssignmentDate(Timestamp.valueOf(java.time.LocalDateTime.now()));
                ticketToUserService.save(ticketToUser);
            }

            Ticket updatedTicket = ticketService.save(ticket);
            return ResponseEntity.ok(updatedTicket);
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
    @PutMapping("/edit/{ticketId}/{userId}")
    public ResponseEntity<Ticket> addTicketToUser(@PathVariable UUID ticketId, @PathVariable UUID userId, @RequestBody EditMyTicketDTO editMyTicketDTO) {
        try {
            Ticket ticket = ticketService.getTicketById(ticketId);
            if (ticket == null) {
                return ResponseEntity.notFound().build();
            }

            Status status = statusService.findByName(editMyTicketDTO.getStatus());
            ticket.setStatus(status);

            if (ticket.getCloseDate() == null && Objects.equals(status.getName(), "Lõpetatud")) {
                ticket.setCloseDate(Timestamp.valueOf(java.time.LocalDateTime.now()));
                if (ticket.getOpenDate() == null) {
                    ticket.setOpenDate(Timestamp.valueOf(java.time.LocalDateTime.now()));
                }
            }
            if (ticket.getOpenDate() == null && Objects.equals(status.getName(), "Avatud") || Objects.equals(status.getName(), "Hoiukodus")){
                ticket.setOpenDate(Timestamp.valueOf(java.time.LocalDateTime.now()));
            }

            ticket.setHistory(editMyTicketDTO.getHistory());

            if (editMyTicketDTO.getSpecies() != null && !Objects.equals(editMyTicketDTO.getSpecies(), "")) {
                Species species = speciesService.getSpeciesByName(editMyTicketDTO.getSpecies());
                ticket.setSpecies(species);
            }

            List<TicketToAnimalTag> existingTags = ticketToAnimalTagService.findByTicket(ticket);
            for (TicketToAnimalTag existingTag : existingTags) {
                ticketToAnimalTagService.delete(existingTag);
            }
            List<String> tags = editMyTicketDTO.getTags();
            for (String tag : tags) {
                AnimalTag animalTag = animalTagService.getByAnimalTagInjury(tag);
                TicketToAnimalTag ticketToAnimalTag = new TicketToAnimalTag();
                ticketToAnimalTag.setTicket(ticket);
                ticketToAnimalTag.setAnimalTag(animalTag);
                ticketToAnimalTagService.save(ticketToAnimalTag);
            }

            UpperSpecies upperSpecies = upperSpeciesService.findByName(editMyTicketDTO.getUpperSpecies());
            ticket.setUpperSpecies(upperSpecies);

            if (editMyTicketDTO.getResolution() != null && !Objects.equals(editMyTicketDTO.getResolution(), "")) {
                Resolution resolution = resolutionService.getResolutionByName(editMyTicketDTO.getResolution());
                ticket.setResolution(resolution);
            }

            Ticket updatedTicket = ticketService.save(ticket);
            return ResponseEntity.ok(updatedTicket);
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<String> deleteTicket(@PathVariable UUID id) {
        try {
            System.out.println("Deleting ticket with ID: " + id);
            ticketToUserService.deleteByTicketId(id);
            System.out.println("Deleted ticketToUser");
            ticketToAnimalTagService.deleteByTicketId(id);
            System.out.println("Deleted ticketToAnimalTag");
            ticketToSpeciesService.deleteByTicketId(id);
            System.out.println("Deleted ticketToSpecies");
            pictureToTicketService.deleteByTicketId(id);
            System.out.println("Deleted pictureToTicket");
            ticketService.deleteById(id);
            System.out.println("Deleted ticket");
            return ResponseEntity.ok("Ticket deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }
}

