package com.EmergencyAlertApplication.EAA.Repositories;

import com.EmergencyAlertApplication.EAA.Entities.Resolution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResolutionRepository extends JpaRepository<Resolution, UUID> {
    Resolution findByName(String name);
}
