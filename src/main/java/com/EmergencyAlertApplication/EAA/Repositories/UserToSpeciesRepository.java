package com.EmergencyAlertApplication.EAA.Repositories;

import com.EmergencyAlertApplication.EAA.Entities.UserToSpecies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserToSpeciesRepository extends JpaRepository<UserToSpecies, UUID> {
    List<UserToSpecies> findByUserId(UUID id);

    void deleteByUserId(UUID id);
}
