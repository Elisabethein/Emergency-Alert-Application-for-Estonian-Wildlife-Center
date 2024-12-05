package com.EmergencyAlertApplication.EAA.Configurations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.util.Date;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final String jwtSecret;
    private final long jwtExpirationMs;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, String jwtSecret, long jwtExpirationMs) {
        super.setAuthenticationManager(authenticationManager);
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.println("Attempting login with email: " + email + " and password: " + password);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authResult = getAuthenticationManager().authenticate(authRequest);

        System.out.println("Authentication successful: " + authResult.isAuthenticated());
        return authResult;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String email = authResult.getName(); // Get the email from the authentication result
        System.out.println("Successful authentication for email: " + email);

        // Generate JWT token
        String token = generateToken(email);
        System.out.println("Generated JWT token: " + token);

        // Add the token to the response header
        response.setHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + token + "\"}");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        // You can access the parameters here
        String email = request.getParameter("email");
        System.out.println("Login failed for email: " + email + ", reason: " + failed.getMessage());

        super.unsuccessfulAuthentication(request, response, failed);
    }

    private String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Set expiration time
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes())
                .compact();
    }
}
