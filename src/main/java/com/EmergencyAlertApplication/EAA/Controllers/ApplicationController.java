package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.DTOs.ApplicationDTO;
import com.EmergencyAlertApplication.EAA.Entities.Application;
import com.EmergencyAlertApplication.EAA.Services.ApplicationService;
import com.EmergencyAlertApplication.EAA.Services.ApplicationToTagService;
import com.EmergencyAlertApplication.EAA.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/applications", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ApplicationController {

    private final ApplicationService applicationService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationToTagService applicationToTagService;

    @Autowired
    public ApplicationController(ApplicationService applicationService, UserService userService, PasswordEncoder passwordEncoder,ApplicationToTagService applicationToTagService) {
        this.applicationService = applicationService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.applicationToTagService = applicationToTagService;
    }

    @PostMapping("/addApplication")
    public ResponseEntity<Object> addApplication(@RequestBody ApplicationDTO application) {
        try {
            if (userService.emailExists(application.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }
            Application newApplication = new Application();
            newApplication.setIsAccepted(false);
            newApplication.setFirstName(application.getFirstName());
            newApplication.setLastName(application.getLastName());
            newApplication.setPhoneNr(application.getPhone());
            newApplication.setEmail(application.getEmail());
            newApplication.setBirthDate(application.getDob());
            newApplication.setStreetName(application.getStreet());
            newApplication.setStreetNr(application.getStreetNr());
            newApplication.setCity(application.getCity());
            newApplication.setCounty(application.getRegion());
            newApplication.setPostalCode(application.getPostalCode());
            newApplication.setQuestion1(application.getReason());
            newApplication.setQuestion2(application.getExperience());
            newApplication.setCreatedAt(System.currentTimeMillis());
            String hashedPassword = passwordEncoder.encode(application.getPassword());
            newApplication.setPassword(hashedPassword);

            Application savedApplication = applicationService.save(newApplication);
            return ResponseEntity.ok(savedApplication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Application>> getAllApplications() {
        try {
            List<Application> applications = applicationService.findAll();
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteApplication(@PathVariable UUID id) {
        try {
            applicationService.deleteById(id);
            return ResponseEntity.ok().body("Application deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting application");
        }
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<List<String>> getApplicationTags(@PathVariable UUID id) {
        try {
            List<String> tags = applicationToTagService.getApplicationTags(id);
            return ResponseEntity.ok(tags);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonList("Error occurred: " + e.getMessage()));
        }
    }

}
