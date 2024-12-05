package com.EmergencyAlertApplication.EAA.Services;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

import static com.EmergencyAlertApplication.EAA.DriveQuickstart.*;

public class GoogleDriveService {

    private static final String SERVICE_ACCOUNT_FILE = "/tarkvaraprojekti.json";

    public static String uploadFile(String fileName, MultipartFile file, String folderId) throws IOException, GeneralSecurityException {
        System.out.println("Uploading to folder with ID: " + folderId);

        System.out.println("Uploading file to Google Drive: " + fileName);
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        System.out.println("Creating Drive service...");
        Drive service = GoogleDriveService.getDriveService();
        System.out.println("Drive service created successfully.");

        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList(folderId));

        java.io.File tempFile = new java.io.File(file.getOriginalFilename());
        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            outputStream.write(file.getBytes());
            System.out.println("File saved to temp file: " + tempFile.getAbsolutePath());
        }

        FileContent mediaContent = new FileContent(file.getContentType(), tempFile);

        try {
            // Create the request to upload the file
            Drive.Files.Create createRequest = service.files().create(fileMetadata, mediaContent);

            // Get the uploader to upload the file
            MediaHttpUploader uploader = createRequest.getMediaHttpUploader();

            // Set progress listener to track upload
            uploader.setProgressListener(new MediaHttpUploaderProgressListener() {
                @Override
                public void progressChanged(MediaHttpUploader uploader) throws IOException {
                    switch (uploader.getUploadState()) {
                        case INITIATION_STARTED:
                            System.out.println("Upload initiation started.");
                            break;
                        case INITIATION_COMPLETE:
                            System.out.println("Upload initiation completed.");
                            break;
                        case MEDIA_IN_PROGRESS:
                            System.out.println("Upload in progress: " + uploader.getNumBytesUploaded() + " bytes uploaded.");
                            break;
                        case MEDIA_COMPLETE:
                            System.out.println("Upload completed.");
                            break;
                    }
                }
            });

            // Now start uploading
            File uploadedFile = createRequest.execute();
            System.out.println("File uploaded: " + uploadedFile.getId());
            return uploadedFile.getId();
        } catch (GoogleJsonResponseException e) {
            System.out.println("Google Drive API Error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
            throw new IOException("Unable to upload file to Google Drive: " + e.getDetails());
        } catch (IOException e) {
            System.out.println("IOException during file upload: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            throw new IOException("Unable to upload file: " + e.getMessage());
        }
    }

    public static Drive getDriveService() throws IOException {
        // Load the service account key file
        InputStream serviceAccountStream = GoogleDriveService.class.getResourceAsStream(SERVICE_ACCOUNT_FILE);
        if (serviceAccountStream == null) {
            throw new IOException("Service account file not found in classpath.");
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream)
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        // Create Drive service
        try {
            return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
