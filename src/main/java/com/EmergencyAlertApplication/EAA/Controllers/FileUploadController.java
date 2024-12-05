package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.Entities.Picture;
import com.EmergencyAlertApplication.EAA.Entities.Ticket;
import com.EmergencyAlertApplication.EAA.Repositories.PictureRepository;
import com.EmergencyAlertApplication.EAA.Services.GoogleDriveService;
import com.EmergencyAlertApplication.EAA.Services.PictureToTicketService;
import com.EmergencyAlertApplication.EAA.Services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FileUploadController {
    private final PictureRepository pictureRepository;

    private final PictureToTicketService pictureToTicketService;

    private final TicketService ticketService;

    @Autowired
    public FileUploadController(PictureRepository pictureRepository, PictureToTicketService pictureToTicketService, TicketService ticketService) {
        this.pictureRepository = pictureRepository;
        this.pictureToTicketService = pictureToTicketService;
        this.ticketService = ticketService;
    }

    @PostMapping("/uploadToDrive")
    public ResponseEntity<Map<String, String>> uploadFileToDrive(@RequestParam("file") MultipartFile file,
                                                                 String folderId,
                                                                 @RequestParam("ticketId") UUID ticketId) {
        try {
            System.out.println("received file: " + file.getOriginalFilename());
            System.out.println("received folderId: " + folderId);
            System.out.println("received ticketId: " + ticketId);
            // Faili üleslaadimine Google Drive'i
            String fileId = GoogleDriveService.uploadFile(file.getOriginalFilename(), file, folderId);

            System.out.println("fileId: " + fileId);

            // Salvestame faili info andmebaasi
            Picture picture = new Picture();
            picture.setDriveId(fileId);
            picture.setName(file.getOriginalFilename());
            pictureRepository.save(picture);


            // Ticketi ja pildi sidumine
            Ticket ticket = ticketService.getTicketById(ticketId);
            pictureToTicketService.linkPictureToTicket(picture, ticket);


            // Tagasta faili ID
            Map<String, String> response = new HashMap<>();
            response.put("fileId", fileId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Failed to upload file to Google Drive" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to upload file to Google Drive"));
        }
    }
}