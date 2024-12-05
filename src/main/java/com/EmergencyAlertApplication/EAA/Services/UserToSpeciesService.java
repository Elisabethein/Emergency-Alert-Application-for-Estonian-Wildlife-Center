package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.DTOs.SpeciesDTO;
import com.EmergencyAlertApplication.EAA.Entities.UpperSpecies;
import com.EmergencyAlertApplication.EAA.Entities.User;
import com.EmergencyAlertApplication.EAA.Entities.UserToSpecies;
import com.EmergencyAlertApplication.EAA.Repositories.UserToSpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserToSpeciesService {
    private final UserToSpeciesRepository userToSpeciesRepository;
    private final UpperSpeciesService upperSpeciesService;

    @Autowired
    public UserToSpeciesService(UserToSpeciesRepository userToSpeciesRepository, UpperSpeciesService upperSpeciesService) {
        this.userToSpeciesRepository = userToSpeciesRepository;
        this.upperSpeciesService = upperSpeciesService;
    }

    public List<UserToSpecies> getUserSpecies(UUID id) {
        return userToSpeciesRepository.findByUserId(id);
    }

    @Transactional
    public void editUserSpecies(UUID id, User editedUser, List<SpeciesDTO> species) {
        try {
            userToSpeciesRepository.deleteByUserId(id);
        } catch (Exception e) {
            System.err.println("Error deleting user regions: " + e.getMessage());
            throw e;
        }
        species.forEach(speciesDTO -> {
            UpperSpecies upperSpecies = upperSpeciesService.getUpperSpeciesByName(speciesDTO.getName());
            UserToSpecies userToSpecies = new UserToSpecies();
            userToSpecies.setUser(editedUser);
            userToSpecies.setUpperSpecies(upperSpecies);
            userToSpecies.setExpert(speciesDTO.isExpert());
            userToSpeciesRepository.save(userToSpecies);
        });
    }
}
