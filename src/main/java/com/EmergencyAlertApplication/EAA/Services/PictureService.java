package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.Repositories.PictureRepository;
import org.springframework.stereotype.Service;

@Service
public class PictureService {
    private final PictureRepository pictureRepository;

    public PictureService(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }
}
