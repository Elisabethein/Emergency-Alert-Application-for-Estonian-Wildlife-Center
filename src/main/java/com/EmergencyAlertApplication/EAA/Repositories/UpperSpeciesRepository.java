package com.EmergencyAlertApplication.EAA.Repositories;

import com.EmergencyAlertApplication.EAA.Entities.UpperSpecies;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UpperSpeciesRepository extends JpaRepository<UpperSpecies, UUID> {
    UpperSpecies findByName(String name);
}
