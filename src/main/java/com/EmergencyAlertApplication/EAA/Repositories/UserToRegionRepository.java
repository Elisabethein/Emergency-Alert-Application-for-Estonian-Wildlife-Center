package com.EmergencyAlertApplication.EAA.Repositories;

import com.EmergencyAlertApplication.EAA.Entities.User;
import com.EmergencyAlertApplication.EAA.Entities.UserToRegion;
import com.EmergencyAlertApplication.EAA.Entities.UserToRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserToRegionRepository extends JpaRepository<UserToRegion, UUID> {
    List<UserToRegion> findByUserId(UUID id);

    void deleteByUserId(UUID id);

}
