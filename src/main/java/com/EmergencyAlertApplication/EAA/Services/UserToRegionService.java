package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.Entities.Region;
import com.EmergencyAlertApplication.EAA.Entities.User;
import com.EmergencyAlertApplication.EAA.Entities.UserToRegion;
import com.EmergencyAlertApplication.EAA.Repositories.UserToRegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserToRegionService {
    private final UserToRegionRepository userToRegionRepository;
    private final RegionService regionService;

    @Autowired
    public UserToRegionService(UserToRegionRepository userToRegionRepository, RegionService regionService) {
        this.userToRegionRepository = userToRegionRepository;
        this.regionService = regionService;
    }

    public List<String> getUserRegions(UUID id) {
        List<UserToRegion> userToRegions = userToRegionRepository.findByUserId(id);
        return userToRegions.stream().map(userToRegion -> userToRegion.getRegion().getName()).toList();
    }

    @Transactional
    public void editUserRegions(UUID id, User user, List<String> regions) {
        try {
            userToRegionRepository.deleteByUserId(id);
        } catch (Exception e) {
            System.err.println("Error deleting user regions: " + e.getMessage());
            throw e;
        }
        regions.forEach(region -> {
            Region regionObject = regionService.getRegionByName(region);
            UserToRegion userToRegion = new UserToRegion();
            userToRegion.setUser(user);
            userToRegion.setRegion(regionObject);
            userToRegionRepository.save(userToRegion);
        });
    }

    public void addUserToRegion(User user, Region region) {
        UserToRegion userToRegion = new UserToRegion();
        userToRegion.setUser(user);
        userToRegion.setRegion(region);
        userToRegionRepository.save(userToRegion);
    }
}
