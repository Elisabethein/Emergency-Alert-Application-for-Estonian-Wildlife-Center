package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.Entities.ApplicationToTag;
import com.EmergencyAlertApplication.EAA.Repositories.ApplicationToTagRepository;
import com.EmergencyAlertApplication.EAA.Repositories.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ApplicationToTagService {
    private final ApplicationToTagRepository applicationToTagRepository;

    @Autowired
    public ApplicationToTagService(ApplicationToTagRepository applicationToTagRepository, TagRepository tagRepository) {
        this.applicationToTagRepository = applicationToTagRepository;
    }

    public void save(ApplicationToTag applicationToTag) {
        applicationToTagRepository.save(applicationToTag);
    }

    public List<String> getApplicationTags(UUID id) {
        List<ApplicationToTag> applicationTags = applicationToTagRepository.findByApplicationId(id);
        return applicationTags.stream().map(applicationToTag -> applicationToTag.getTag().getFunction()).toList();
    }
    @Transactional
    public void deleteByApplicationId(UUID applicationId) {
        applicationToTagRepository.deleteByApplicationId(applicationId);
    }
}
