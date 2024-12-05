package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.DTOs.ApplicationToTagDTO;
import com.EmergencyAlertApplication.EAA.Entities.Application;
import com.EmergencyAlertApplication.EAA.Entities.ApplicationToTag;
import com.EmergencyAlertApplication.EAA.Entities.Tag;
import com.EmergencyAlertApplication.EAA.Services.ApplicationToTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping(value = "/api/applicationToTags", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ApplicationToTagController {
    private final ApplicationToTagService applicationToTagService;
    @Autowired
    public ApplicationToTagController(ApplicationToTagService applicationToTagService) {
        this.applicationToTagService = applicationToTagService;
    }

    @PostMapping("/addApplicationToTag")
    public ResponseEntity<String> createApplicationTags(@RequestBody List<ApplicationToTagDTO> applicationToTagDTOList) {
        try {
            for (ApplicationToTagDTO dto : applicationToTagDTOList) {
                Application app = dto.getApplication();
                Tag tag = dto.getTag();

                ApplicationToTag applicationToTag = new ApplicationToTag();
                applicationToTag.setApplication(app);
                applicationToTag.setTag(tag);
                applicationToTagService.save(applicationToTag);
            }
            return ResponseEntity.ok("ApplicationToTag created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }
}
