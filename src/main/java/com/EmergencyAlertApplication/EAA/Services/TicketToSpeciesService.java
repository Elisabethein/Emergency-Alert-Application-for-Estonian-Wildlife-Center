package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.Entities.TicketToSpecies;
import com.EmergencyAlertApplication.EAA.Repositories.TicketToSpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketToSpeciesService {
    private final TicketToSpeciesRepository ticketToSpeciesRepository;

    @Autowired
    public TicketToSpeciesService(TicketToSpeciesRepository ticketToSpeciesRepository) {
        this.ticketToSpeciesRepository = ticketToSpeciesRepository;
    }

    public List<TicketToSpecies> findAll() {
        return ticketToSpeciesRepository.findAll();
    }

    public Optional<TicketToSpecies> findById(UUID id) {
        return ticketToSpeciesRepository.findById(id);
    }

    public TicketToSpecies save(TicketToSpecies ticketToSpecies) {
        return ticketToSpeciesRepository.save(ticketToSpecies);
    }

    public void deleteById(UUID id) {
        ticketToSpeciesRepository.deleteById(id);
    }

    public void deleteByTicketId(UUID id) {
        ticketToSpeciesRepository.deleteByTicketId(id);
    }
}
