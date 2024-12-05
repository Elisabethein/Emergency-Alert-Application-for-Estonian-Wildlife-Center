package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.Entities.Resolution;
import com.EmergencyAlertApplication.EAA.Repositories.ResolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResolutionService {
    private final ResolutionRepository resolutionRepository;

    @Autowired
    public ResolutionService(ResolutionRepository resolutionRepository) {
        this.resolutionRepository = resolutionRepository;
    }
    public Optional<Resolution> findById(UUID id) {
        return resolutionRepository.findById(id);
    }

    public Resolution save(Resolution resolution) {
        return resolutionRepository.save(resolution);
    }

    public void deleteById(UUID id) {resolutionRepository.deleteById(id);}

    public List<Resolution> getResolutions() {
        return resolutionRepository.findAll();
    }

    public Resolution getResolutionByName(String resolution) {
        return resolutionRepository.findByName(resolution);
    }
}
