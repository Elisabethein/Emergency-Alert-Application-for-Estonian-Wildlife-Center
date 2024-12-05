package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.Entities.UpperSpecies;
import com.EmergencyAlertApplication.EAA.Repositories.UpperSpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UpperSpeciesService {
    private final UpperSpeciesRepository upperSpeciesRepository;

    @Autowired
    public UpperSpeciesService(UpperSpeciesRepository upperSpeciesRepository) {
        this.upperSpeciesRepository = upperSpeciesRepository;
    }

    public UpperSpecies findByName(String upperSpecies) {
        return upperSpeciesRepository.findByName(upperSpecies);
    }

    public List<UpperSpecies> findAll() {
        return upperSpeciesRepository.findAll();
    }

    public Optional<UpperSpecies> findById(UUID id) {
        return upperSpeciesRepository.findById(id);
    }

    public UpperSpecies save(UpperSpecies upperSpecies) {
        return upperSpeciesRepository.save(upperSpecies);
    }

    public void deleteById(UUID id) {
        upperSpeciesRepository.deleteById(id);
    }

    public UpperSpecies getUpperSpeciesByName(String name) {
        return upperSpeciesRepository.findByName(name);
    }
}
