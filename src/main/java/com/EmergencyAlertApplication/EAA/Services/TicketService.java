package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.Entities.*;
import com.EmergencyAlertApplication.EAA.Repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketToAnimalTagRepository ticketToAnimalTagRepository;
    private final TicketToUserRepository ticketToUserRepository;
    private final ResolutionRepository resolutionRepository;
    private final SpeciesRepository speciesRepository;
    private final UpperSpeciesRepository upperSpeciesRepository;
    private final RegionRepository regionRepository;
    private final StatusRepository statusRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, TicketToAnimalTagRepository ticketToAnimalTagRepository, TicketToUserRepository ticketToUserRepository, ResolutionRepository resolutionRepository, SpeciesRepository speciesRepository, UpperSpeciesRepository upperSpeciesRepository, RegionRepository regionRepository, StatusRepository statusRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketToAnimalTagRepository = ticketToAnimalTagRepository;
        this.ticketToUserRepository = ticketToUserRepository;
        this.resolutionRepository = resolutionRepository;
        this.speciesRepository = speciesRepository;
        this.upperSpeciesRepository = upperSpeciesRepository;
        this.regionRepository = regionRepository;
        this.statusRepository = statusRepository;
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> findById(UUID id) {
        return ticketRepository.findById(id);
    }

    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Transactional
    public void deleteById(UUID id) {
        ticketRepository.deleteById(id);
        ticketToAnimalTagRepository.deleteByTicketId(id);
        ticketToUserRepository.deleteByTicketId(id);
    }

    public List<Ticket> getAllTickets(){ return ticketRepository.findAll();}

    public Ticket getTicketById(UUID id){
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        return ticketOptional.orElse(null);
    }

    // Add methods to fetch species, resolutions, and statuses if needed
    public List<Species> getAllSpecies() {
        return speciesRepository.findAll();
    }
    public List<UpperSpecies> getAllUpperSpecies() {
        return upperSpeciesRepository.findAll();
    }

    public List<Resolution> getAllResolutions() {
        return resolutionRepository.findAll();
    }

    public List<Status> getAllStatuses() {
        return statusRepository.findAll();
    }

    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }
    }
