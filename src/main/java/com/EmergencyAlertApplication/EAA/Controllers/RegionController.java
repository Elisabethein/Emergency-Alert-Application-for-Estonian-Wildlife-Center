package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.DTOs.NewRegionDTO;
import com.EmergencyAlertApplication.EAA.Entities.Region;
import com.EmergencyAlertApplication.EAA.Services.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
public class RegionController {
    private final RegionService regionService;
    @Autowired
    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<String>> getAllRegions() {
        try {
            List<Region> regions = regionService.getAll();
            return ResponseEntity.ok(regions.stream().map(Region::getName).toList());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @PostMapping("/add")
    public ResponseEntity<String> addRegion(@RequestBody NewRegionDTO newRegionDTO) {
        try {
            if (regionService.getRegionByName(newRegionDTO.getName()) != null) {
                return ResponseEntity.badRequest().body("Region already exists");
            }
            Region newRegion = new Region();
            newRegion.setName(newRegionDTO.getName());
            regionService.save(newRegion);
            return ResponseEntity.ok("Region added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
