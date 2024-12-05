package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.Entities.Application;
import com.EmergencyAlertApplication.EAA.Repositories.ApplicationRepository;
import com.EmergencyAlertApplication.EAA.Repositories.ApplicationToTagRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationToTagRepository applicationToTagsRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, ApplicationToTagRepository applicationToTagsRepository) {
        this.applicationRepository = applicationRepository;
        this.applicationToTagsRepository = applicationToTagsRepository;
    }

    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    public Optional<Application> findById(UUID id) {
        return applicationRepository.findById(id);
    }

    @Transactional
    public void deleteById(UUID id) {
        applicationToTagsRepository.deleteByApplicationId(id);
        applicationRepository.deleteById(id);
    }

    public Application save(Application application) {
        return applicationRepository.save(application);
    }
    public boolean emailExists(String email) {
        return applicationRepository.existsByEmail(email);
    }

    public Optional<Application> getById(UUID applicationId) {
        return applicationRepository.findById(applicationId);
    }
}
