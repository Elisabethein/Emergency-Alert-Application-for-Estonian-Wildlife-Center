package com.EmergencyAlertApplication.EAA.Repositories;

import com.EmergencyAlertApplication.EAA.Entities.Species;
import com.EmergencyAlertApplication.EAA.Entities.UpperSpecies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpeciesRepository extends JpaRepository<Species, UUID> {
    List<Species> findAllByUpperSpecies(UpperSpecies upperSpeciesObject);
    Species findByName(String species);
}
