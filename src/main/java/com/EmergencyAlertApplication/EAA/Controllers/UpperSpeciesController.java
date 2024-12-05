package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.DTOs.NewUpperSpeciesDTO;
import com.EmergencyAlertApplication.EAA.Entities.UpperSpecies;
import com.EmergencyAlertApplication.EAA.Services.UpperSpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/upperSpecies")
public class UpperSpeciesController {
    private final UpperSpeciesService upperSpeciesService;

    @Autowired
    public UpperSpeciesController(UpperSpeciesService upperSpeciesService) {
        this.upperSpeciesService = upperSpeciesService;
    }

    @GetMapping("/all")
    public List<UpperSpecies> getUpperSpecies() {
        return upperSpeciesService.findAll();
    }

    @PostMapping("/add")
    public ResponseEntity<UpperSpecies> addUpperSpecies(@RequestBody NewUpperSpeciesDTO newUpperSpeciesDTO) {
        try {
            if (upperSpeciesService.getUpperSpeciesByName(newUpperSpeciesDTO.getName()) != null) {
                return ResponseEntity.badRequest().body(null);
            }
            UpperSpecies upperSpecies = new UpperSpecies();
            upperSpecies.setName(newUpperSpeciesDTO.getName());
            upperSpeciesService.save(upperSpecies);
            return ResponseEntity.ok(upperSpecies);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
