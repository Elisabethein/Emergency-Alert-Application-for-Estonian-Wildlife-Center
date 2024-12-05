package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.Entities.Species;
import com.EmergencyAlertApplication.EAA.Entities.UpperSpecies;
import com.EmergencyAlertApplication.EAA.Repositories.SpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SpeciesService {
    private final SpeciesRepository speciesRepository;
    private final UpperSpeciesService upperSpeciesService;

    @Autowired
    public SpeciesService(SpeciesRepository speciesRepository, UpperSpeciesService upperSpeciesService) {
        this.speciesRepository = speciesRepository;
        this.upperSpeciesService = upperSpeciesService;
    }

    public List<Species> findAll() {
        return speciesRepository.findAll();
    }

    public Optional<Species> findById(UUID id) {
        return speciesRepository.findById(id);
    }

    public Species save(Species species) {
        return speciesRepository.save(species);
    }

    public void deleteById(UUID id) {
        speciesRepository.deleteById(id);
    }

    public List<Species> getAll(String upperSpecies) {
        UpperSpecies upperSpeciesObject = upperSpeciesService.findByName(upperSpecies);
        return speciesRepository.findAllByUpperSpecies(upperSpeciesObject);
    }

    public Species getSpeciesByName(String species) {
        return speciesRepository.findByName(species);
    }

}
