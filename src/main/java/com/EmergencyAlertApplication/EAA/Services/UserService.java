package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.DTOs.EditUserDTO;
import com.EmergencyAlertApplication.EAA.Entities.User;
import com.EmergencyAlertApplication.EAA.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserToTagRepository userToTagRepository;
    private final UserToRoleRepository userToRoleRepository;
    private final UserToRegionRepository userToRegionRepository;
    private final UserToSpeciesRepository userToSpeciesRepository;
    private final TicketToUserService ticketToUserService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserToTagRepository userToTagRepository, UserToRoleRepository userToRoleRepository, UserToRegionRepository userToRegionRepository, UserToSpeciesRepository userToSpeciesRepository, TicketToUserService ticketToUserService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userToTagRepository = userToTagRepository;
        this.userToRoleRepository = userToRoleRepository;
        this.userToRegionRepository = userToRegionRepository;
        this.userToSpeciesRepository = userToSpeciesRepository;
        this.ticketToUserService = ticketToUserService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void deleteById(UUID id) {
        userToTagRepository.deleteByUserId(id);
        userToRoleRepository.deleteByUserId(id);
        userToRegionRepository.deleteByUserId(id);
        userToSpeciesRepository.deleteByUserId(id);
        ticketToUserService.deleteByUserId(id);
        userRepository.deleteById(id);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User editUser(UUID id, EditUserDTO editUserDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstName(editUserDTO.getFirstName());
            user.setLastName(editUserDTO.getLastName());
            user.setBirthDate(editUserDTO.getBirthDate());
            user.setEmail(editUserDTO.getEmail());
            user.setPhoneNr(editUserDTO.getPhoneNr());
            user.setCounty(editUserDTO.getCounty());
            user.setCity(editUserDTO.getCity());
            user.setStreetName(editUserDTO.getStreetName());
            user.setStreetNr(editUserDTO.getStreetNr());
            user.setPostalCode(editUserDTO.getPostalCode());

            if (editUserDTO.getPassword() != null && !editUserDTO.getPassword().isEmpty()) {
                String hashedPassword = passwordEncoder.encode(editUserDTO.getPassword());
                user.setPassword(hashedPassword);
            }

            return userRepository.save(user);
        }
        return null;
    }

    public User getUserById(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }
}
