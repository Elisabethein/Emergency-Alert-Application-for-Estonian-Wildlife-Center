package com.EmergencyAlertApplication.EAA.Services;
import com.EmergencyAlertApplication.EAA.Entities.*;
import com.EmergencyAlertApplication.EAA.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final TicketToAnimalTagRepository ticketToAnimalTagRepository;

    @Autowired
    public StatisticsService(StatisticsRepository statisticsRepository, TicketToAnimalTagRepository ticketToAnimalTagRepository) {
        this.statisticsRepository = statisticsRepository;
        this.ticketToAnimalTagRepository = ticketToAnimalTagRepository;
    }

    public List<Ticket> getFilteredTickets(Timestamp startDate, Timestamp endDate, List<String> upperSpecies, List<String> species, List<String> region, List<String> resolution, List<String> injuries) {
        List<Ticket> allTickets = statisticsRepository.findAll();
        List<Ticket> filteredTickets = new ArrayList<>();
        for (Ticket ticket : allTickets) {
            if (isTicketClosed(ticket) && isTicketWithinDateRange(ticket, startDate, endDate) && doesTicketMatchFilters(ticket, upperSpecies, species, region, resolution, injuries)) {
                filteredTickets.add(ticket);
            }
        }
        return filteredTickets;
    }

    private boolean isTicketClosed(Ticket ticket) {
        return ticket.getStatus().getName().equals("Lõpetatud");
    }
    private boolean isTicketWithinDateRange(Ticket ticket, Timestamp startDate, Timestamp endDate) {
        return !ticket.getOpenDate().before(startDate) && !ticket.getOpenDate().after(endDate);
    }

    private boolean doesTicketMatchFilters(Ticket ticket, List<String> upperSpecies, List<String> species, List<String> region, List<String> resolution, List<String> injuries) {
        UpperSpecies ticketUpperSpecies = ticket.getUpperSpecies();
        Species ticketSpecies = ticket.getSpecies();
        Region ticketRegion = ticket.getRegion();
        Resolution ticketResolution = ticket.getResolution();
        List<TicketToAnimalTag> ticketToAnimalTags = ticketToAnimalTagRepository.findByTicketId(ticket.getId());
        List<AnimalTag> animalTags = new ArrayList<>();

        for (TicketToAnimalTag ticketToAnimalTag : ticketToAnimalTags) {
            animalTags.add(ticketToAnimalTag.getAnimalTag());
        }

        boolean upperSpeciesMatch = (upperSpecies == null || upperSpecies.contains(ticketUpperSpecies.getName()));
        boolean speciesMatch = (species == null || species.contains(ticketSpecies.getName()));
        boolean regionMatch = (region == null || region.contains(ticketRegion.getName()));
        boolean resolutionMatch = (resolution == null || resolution.contains(ticketResolution.getName()));

        boolean injuriesMatch = true;
        for (AnimalTag animalTag : animalTags) {
            if (injuries != null && !injuries.contains(animalTag.getInjury())) {
                injuriesMatch = false;
                break;
            }
        }

        return upperSpeciesMatch && speciesMatch && regionMatch && resolutionMatch && injuriesMatch;
    }
}
