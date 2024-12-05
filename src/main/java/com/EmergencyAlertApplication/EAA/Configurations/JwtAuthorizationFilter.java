package com.EmergencyAlertApplication.EAA.Configurations;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.Base64;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final String jwtSecret;
    private final UserDetailsService userDetailsService;

    public JwtAuthorizationFilter(String jwtSecret, UserDetailsService userDetailsService) {
        this.jwtSecret = jwtSecret;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            System.out.println("JWT token received: " + token);

            try {
                // Decode the secret
                byte[] secretBytes = Base64.getDecoder().decode(jwtSecret);
                Key key = Keys.hmacShaKeyFor(secretBytes);

                // Parse the JWT token
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                username = claims.getSubject();
                System.out.println("Extracted username from JWT: " + username);

            } catch (Exception e) {
                System.out.println("Invalid JWT Token: " + e.getMessage());
            }
        } else {
            System.out.println("JWT token not found");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("Loaded user details for username: " + username);

            if (Jwts.parserBuilder().setSigningKey(Base64.getDecoder().decode(jwtSecret)).build().parseClaimsJws(token).getBody().getSubject().equals(userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Authentication set in SecurityContext for username: " + username);

            } else {
                System.out.println("JWT token does not match user details");
            }
        } else {
            System.out.println("JWT token not valid or user details not found");
        }

        chain.doFilter(request, response);
    }
}

