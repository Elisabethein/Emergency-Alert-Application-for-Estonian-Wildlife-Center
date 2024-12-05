package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.DTOs.NewResolutionDTO;
import com.EmergencyAlertApplication.EAA.Entities.Resolution;
import com.EmergencyAlertApplication.EAA.Services.ResolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/resolutions", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ResolutionController {
    private final ResolutionService resolutionService;

    @Autowired
    public ResolutionController(ResolutionService resolutionService) {
        this.resolutionService = resolutionService;
    }

    @GetMapping("/all")
    public List<Resolution> getResolutions() {
        return resolutionService.getResolutions();
    }

    @PutMapping("/add")
    public ResponseEntity<String> addResolution(@RequestBody NewResolutionDTO newResolutionDTO) {
        try {
            Resolution newResolution = new Resolution();
            newResolution.setName(newResolutionDTO.getName());
            resolutionService.save(newResolution);
            return ResponseEntity.ok("Resolution added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
