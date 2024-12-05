package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.DTOs.CustomUserDetails;
import com.EmergencyAlertApplication.EAA.DTOs.LoginRequest;
import com.EmergencyAlertApplication.EAA.DTOs.LoginResponse;
import com.EmergencyAlertApplication.EAA.Entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SecurityController {
    private final AuthenticationManager authenticationManager;
    @Autowired
    public SecurityController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Value("${LOCATIONIQ_TOKEN}")
    private String LOCATIONIQ_TOKEN;

    @Value("${FOLDERID}")
    private String FOLDERID;

    @GetMapping("/get-key")
    public String getKey() {
        return LOCATIONIQ_TOKEN;
    }

    @GetMapping("/get-folderId")
    public String getFolderKey() {
        return FOLDERID;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        // Log the email being used for login
        System.out.println("Login attempt for email: " + loginRequest.getEmail());

        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            // Log successful authentication
            System.out.println("Authentication successful for email: " + loginRequest.getEmail());

            // Retrieve user information using CustomUserDetails
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal(); // Use CustomUserDetails
            User user = userDetails.getUser(); // Get User from CustomUserDetails
            System.out.println("Retrieved user details for email: " + user.getEmail());

            // Get user roles from your UserToRole table or similar logic
            List<String> roles = userDetails.getRoles(); // Fetch roles from CustomUserDetails
            System.out.println("User roles retrieved: " + roles);

            List<String> regions = userDetails.getRegions(); // Fetch regions from CustomUserDetails
            System.out.println("User regions retrieved: " + regions);

            // Generate JWT token
            String token = generateToken(user);

            // Create a response object with user, roles, and token
            LoginResponse response = new LoginResponse(user, roles, regions, token); // Pass the token to LoginResponse
            System.out.println("Login response created successfully for email: " + loginRequest.getEmail());

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            // Log specific error for bad credentials
            System.out.println("Login failed for email: " + loginRequest.getEmail() + ". Invalid credentials.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch (Exception e) {
            // Log general error message
            System.out.println("Login failed for email: " + loginRequest.getEmail() + ". Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login");
        }
    }

    private String generateToken(User user) {
        long expirationTime = 3600000; // 1 hour in milliseconds
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        // Ensure jwtSecret is Base64-encoded and long enough for HMAC
        var key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key) // Use the secure key
                .compact();
    }
}
