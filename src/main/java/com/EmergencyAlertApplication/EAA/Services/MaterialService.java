package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.Entities.Material;
import com.EmergencyAlertApplication.EAA.Repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {
    private final MaterialRepository materialRepository;

    @Autowired
    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public Material addMaterial(Material material) {
        return materialRepository.save(material);
    }

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    public void deleteMaterial(Long id) {
        materialRepository.deleteById(id);
    }

    public Material updateMaterial(Long id, Material material) {
        // Otsi olemasolevat materjali
        Optional<Material> existingMaterialOpt = materialRepository.findById(id);
        if (existingMaterialOpt.isPresent()) {
            Material existingMaterial = existingMaterialOpt.get();
            // Uuenda olemasoleva materjali andmed
            existingMaterial.setTitle(material.getTitle());
            existingMaterial.setLink(material.getLink());
            existingMaterial.setDescription(material.getDescription());
            existingMaterial.setGeneralMaterial(material.getGeneralMaterial());
            existingMaterial.setSelectedSpeciesGroup(material.getSelectedSpeciesGroup());
            // Salvesta uuendatud materjal
            return materialRepository.save(existingMaterial);
        } else {
            throw new RuntimeException("Materjal ei leitud"); // Või kasuta sobivamat erindit
        }
    }
}

