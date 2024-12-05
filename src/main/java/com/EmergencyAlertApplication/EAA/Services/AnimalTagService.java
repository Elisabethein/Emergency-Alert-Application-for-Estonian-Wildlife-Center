package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.Entities.AnimalTag;
import com.EmergencyAlertApplication.EAA.Repositories.AnimalTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnimalTagService {
    private final AnimalTagRepository animalTagRepository;

    @Autowired
    public AnimalTagService(AnimalTagRepository animalTagRepository) {
        this.animalTagRepository = animalTagRepository;
    }

    public List<AnimalTag> findAll() {
        return animalTagRepository.findAll();
    }

    public Optional<AnimalTag> findById(UUID id) {
        return animalTagRepository.findById(id);
    }

    public AnimalTag save(AnimalTag tag) {
        return animalTagRepository.save(tag);
    }

    public void deleteById(UUID id) {
        animalTagRepository.deleteById(id);
    }

    public List<AnimalTag> getAnimalTags() {
        return animalTagRepository.findAll();
    }

    public AnimalTag getByAnimalTagInjury(String tagInjury) {
        return animalTagRepository.findByInjury(tagInjury);

    }
}
