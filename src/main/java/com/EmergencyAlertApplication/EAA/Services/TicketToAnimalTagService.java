package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.Entities.AnimalTag;
import com.EmergencyAlertApplication.EAA.Entities.Ticket;
import com.EmergencyAlertApplication.EAA.Entities.TicketToAnimalTag;
import com.EmergencyAlertApplication.EAA.Repositories.AnimalTagRepository;
import com.EmergencyAlertApplication.EAA.Repositories.TicketToAnimalTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketToAnimalTagService {
    private final TicketToAnimalTagRepository ticketToAnimalTagRepository;
    private final AnimalTagRepository animalTagRepository;

    @Autowired
    public TicketToAnimalTagService(TicketToAnimalTagRepository ticketToAnimalTagRepository, AnimalTagRepository animalTagRepository) {
        this.ticketToAnimalTagRepository = ticketToAnimalTagRepository;
        this.animalTagRepository = animalTagRepository;
    }

    public List<TicketToAnimalTag> findAll() {
        return ticketToAnimalTagRepository.findAll();
    }

    public Optional<TicketToAnimalTag> findById(UUID id) {
        return ticketToAnimalTagRepository.findById(id);
    }

    public TicketToAnimalTag save(TicketToAnimalTag ticketToAnimalTag) {
        return ticketToAnimalTagRepository.save(ticketToAnimalTag);
    }

    public void deleteById(UUID id) {
        ticketToAnimalTagRepository.deleteById(id);
    }

    public List<String> getTicketAnimalTags(UUID id) {
        List<TicketToAnimalTag> ticketToAnimalTags = ticketToAnimalTagRepository.findByTicketId(id);
        return ticketToAnimalTags.stream().map(ticketToAnimalTag -> ticketToAnimalTag.getAnimalTag().getInjury()).toList();
    }

    @Transactional
    public void editTicketAnimalTags(UUID id, Ticket editedTicket, List<String> tags) {
        try {
            ticketToAnimalTagRepository.deleteByTicketId(id);
        } catch (Exception e) {
            throw e;
        }
        tags.forEach(tag -> {
            AnimalTag tagObject = animalTagRepository.findByInjury(tag);
            TicketToAnimalTag ticketToAnimalTag = new TicketToAnimalTag();
            ticketToAnimalTag.setTicket(editedTicket);
            ticketToAnimalTag.setAnimalTag(tagObject);
            ticketToAnimalTagRepository.save(ticketToAnimalTag);
        });
    }

    @Transactional
    public void deleteByTicketId(UUID id) {
        ticketToAnimalTagRepository.deleteByTicketId(id);
    }

    public List<TicketToAnimalTag> findByTicket(Ticket ticket) {
        return ticketToAnimalTagRepository.findByTicket(ticket);
    }

    public void delete(TicketToAnimalTag existingTag) {
        ticketToAnimalTagRepository.delete(existingTag);
    }
}
