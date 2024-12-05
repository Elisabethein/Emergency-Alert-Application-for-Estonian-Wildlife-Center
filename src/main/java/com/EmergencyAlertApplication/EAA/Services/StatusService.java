package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.Entities.Status;
import com.EmergencyAlertApplication.EAA.Repositories.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StatusService {
    private final StatusRepository statusRepository;

    @Autowired
    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }
    public List<Status> findAll() {
        return statusRepository.findAll();
    }

    public Optional<Status> findById(UUID id) {
        return statusRepository.findById(id);
    }

    public Status save(Status status) {
        return statusRepository.save(status);
    }

    public void deleteById(UUID id) {
        statusRepository.deleteById(id);
    }

    public List<Status> getStatuses() {
        return statusRepository.findAll();
    }

    public Status findByName(String status) {
        return statusRepository.findByName(status);
    }
}
