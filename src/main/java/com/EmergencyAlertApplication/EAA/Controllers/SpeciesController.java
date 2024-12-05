package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.DTOs.NewSpeciesDTO;
import com.EmergencyAlertApplication.EAA.Entities.Species;
import com.EmergencyAlertApplication.EAA.Entities.UpperSpecies;
import com.EmergencyAlertApplication.EAA.Services.SpeciesService;
import com.EmergencyAlertApplication.EAA.Services.UpperSpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/species")
public class SpeciesController {
    private final SpeciesService speciesService;
    private final UpperSpeciesService upperSpeciesService;
    @Autowired
    public SpeciesController(SpeciesService speciesService, UpperSpeciesService upperSpeciesService) {
        this.speciesService = speciesService;
        this.upperSpeciesService = upperSpeciesService;
    }

    @GetMapping("/all")
    public List<Species> getAllSpecies() {return speciesService.findAll();}

    @GetMapping("/all/{upperSpecies}")
    public ResponseEntity<List<Species>> getAllSpecies(@PathVariable String upperSpecies) {
        try {
            List<Species> species = speciesService.getAll(upperSpecies);
            return ResponseEntity.ok(species);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addSpecies(@RequestBody NewSpeciesDTO newSpeciesDTO) {
        try {
            UpperSpecies upperSpecies = upperSpeciesService.findByName(newSpeciesDTO.getUpperSpeciesName());
            if (upperSpecies == null) {
                return ResponseEntity.badRequest().body("Upper species does not exist");
            }
            if (speciesService.getSpeciesByName(newSpeciesDTO.getName()) != null) {
                return ResponseEntity.badRequest().body("Species already exists");
            }
            Species species = new Species();
            species.setName(newSpeciesDTO.getName());
            species.setUpperSpecies(upperSpecies);
            speciesService.save(species);
            return ResponseEntity.ok("Species added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
