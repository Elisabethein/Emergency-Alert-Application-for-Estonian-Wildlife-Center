package com.EmergencyAlertApplication.EAA.Repositories;

import com.EmergencyAlertApplication.EAA.Entities.ApplicationToTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApplicationToTagRepository extends JpaRepository<ApplicationToTag, UUID> {
    List<ApplicationToTag> findByApplicationId(UUID id);

    void deleteByApplicationId(UUID applicationId);
}
