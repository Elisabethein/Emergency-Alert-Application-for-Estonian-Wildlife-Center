package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.Entities.*;
import com.EmergencyAlertApplication.EAA.Repositories.TicketToUserRepository;
import com.EmergencyAlertApplication.EAA.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketToUserService {
    private final TicketToUserRepository ticketToUserRepository;

    @Autowired
    public TicketToUserService(TicketToUserRepository ticketToUserRepository, UserRepository userRepository) {
        this.ticketToUserRepository = ticketToUserRepository;
    }
    public List<TicketToUser> findAll() {
        return ticketToUserRepository.findAll();
    }

    public Optional<TicketToUser> findById(UUID id) {
        return ticketToUserRepository.findById(id);
    }

    public TicketToUser save(TicketToUser ticketToUser) {
        return ticketToUserRepository.save(ticketToUser);
    }

    public void deleteById(UUID id) {
        ticketToUserRepository.deleteById(id);
    }

    public List<User> getTicketUsers (UUID id){
        List<TicketToUser> ticketUsers = ticketToUserRepository.findByTicketId(id);
        return ticketUsers.stream().map(TicketToUser::getUser).toList();
    }

    public List<TicketToUser> findByTicketAndUser(Ticket ticket, User user) {
        return ticketToUserRepository.findByTicketAndUser(ticket, user);
    }

    @Transactional
    public void deleteByTicketId(UUID id) {
        ticketToUserRepository.deleteByTicketId(id);
    }

    public List<TicketToUser> findByTicket(Ticket ticket) {
        return ticketToUserRepository.findByTicket(ticket);
    }

    public void delete(TicketToUser existing) {
        ticketToUserRepository.delete(existing);
    }

    public List<Ticket> getTicketToUserByUserID(UUID id) {
        return ticketToUserRepository.findByUserId(id).stream().map(TicketToUser::getTicket).toList();
    }

    public List<User> getTicketToUserByTicketID(UUID id) {
        return ticketToUserRepository.findByTicketId(id).stream().map(TicketToUser::getUser).toList();
    }

    public void deleteByUserId(UUID id) {
        ticketToUserRepository.deleteByUserId(id);
    }
}
