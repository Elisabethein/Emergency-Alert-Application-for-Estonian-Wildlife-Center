package com.EmergencyAlertApplication.EAA.Repositories;

import com.EmergencyAlertApplication.EAA.Entities.TicketToSpecies;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TicketToSpeciesRepository extends JpaRepository<TicketToSpecies, UUID> {
    void deleteByTicketId(UUID id);
}
