package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.DTOs.NewTagDTO;
import com.EmergencyAlertApplication.EAA.Entities.Tag;
import com.EmergencyAlertApplication.EAA.Services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/tags", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class TagController {
    private final TagService tagService;
    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/helpOptions")
    public List<Tag> getTags() {
        return tagService.getTags();
    }

    @PutMapping("/add")
    public ResponseEntity<String> addTag(@RequestBody NewTagDTO newTagDTO) {
        try {
            if (tagService.getByTagFunction(newTagDTO.getName()) != null) {
                return ResponseEntity.badRequest().body("Tag already exists");
            }
            Tag newTag = new Tag();
            newTag.setFunction(newTagDTO.getName());
            tagService.save(newTag);
            return ResponseEntity.ok("Tag added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
