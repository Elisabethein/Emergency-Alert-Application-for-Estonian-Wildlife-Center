package com.EmergencyAlertApplication.EAA.Repositories;

import com.EmergencyAlertApplication.EAA.Entities.AnimalTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnimalTagRepository extends JpaRepository<AnimalTag, UUID> {
    AnimalTag findByInjury(String tagInjury);
}
