package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.DTOs.NewAnimalTagDTO;
import com.EmergencyAlertApplication.EAA.Entities.AnimalTag;
import com.EmergencyAlertApplication.EAA.Services.AnimalTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/animalTags", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class AnimalTagController {

    private final AnimalTagService animalTagService;

    @Autowired
    public AnimalTagController(AnimalTagService animalTagService) {
        this.animalTagService = animalTagService;
    }

    @GetMapping("/injuries")
    public List<AnimalTag> getAnimalTags() {
        return animalTagService.getAnimalTags();
    }

    @PutMapping("/add")
    public ResponseEntity<String> addAnimalTag(@RequestBody NewAnimalTagDTO newAnimalTagDTO) {
        try {
            AnimalTag newAnimalTag = new AnimalTag();
            newAnimalTag.setInjury(newAnimalTagDTO.getName());
            animalTagService.save(newAnimalTag);
            return ResponseEntity.ok("Animal Tag added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
