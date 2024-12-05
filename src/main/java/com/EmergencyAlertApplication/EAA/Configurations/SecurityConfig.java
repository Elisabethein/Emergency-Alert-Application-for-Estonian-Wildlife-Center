package com.EmergencyAlertApplication.EAA.Configurations;

import com.EmergencyAlertApplication.EAA.Repositories.UserRepository;
import com.EmergencyAlertApplication.EAA.Services.CustomUserDetailsService;
import com.EmergencyAlertApplication.EAA.Services.UserToRegionService;
import com.EmergencyAlertApplication.EAA.Services.UserToRoleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    private final UserRepository userRepository;
    private final UserToRoleService userToRoleService;
    private final UserToRegionService userToRegionService;

    public SecurityConfig(UserRepository userRepository, UserToRoleService userToRoleService, UserToRegionService userToRegionService) {
        this.userRepository = userRepository;
        this.userToRoleService = userToRoleService;
        this.userToRegionService = userToRegionService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt hashing algorithm
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService(userRepository, userToRoleService, userToRegionService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        long jwtExpirationMs = 3600000;
        CustomAuthenticationFilter customAuthenticationFilter =
                new CustomAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtSecret, jwtExpirationMs);

        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("http://193.40.11.165", "http://193.40.11.165:8080", "http://localhost:8081", "http://wildlife.cloud.ut.ee", "https://wildlife.cloud.ut.ee")); // Add your frontend origin
                    //configuration.setAllowedOrigins(List.of("*"));
                    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                    configuration.setAllowCredentials(true); // Allow credentials for CORS
                    return configuration;
                }))
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/login") // CSRF ignored for login endpoint
                        .ignoringRequestMatchers("/api/**")) // CSRF ignored for API endpoints
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/login").permitAll() // Allow unauthenticated access to /api/login
                        .requestMatchers("/api/get-key").permitAll()
                        .requestMatchers("/api/applications/addApplication").permitAll()
                        .requestMatchers("/api/applicationToTags/addApplicationToTag").permitAll()
                        .requestMatchers("/api/regions/all").permitAll()
                        .requestMatchers("/api/tags/helpOptions").permitAll()
                        .requestMatchers("/api/animalTags/injuries").permitAll()
                        .requestMatchers("/api/upperSpecies/all").permitAll()
                        .requestMatchers("/api/species/**").permitAll()
                        .requestMatchers("/api/statuses/all").permitAll()
                        .requestMatchers("/api/resolutions/all").permitAll()
                        .requestMatchers("/api/species/all").permitAll()
                        .requestMatchers("/api/tickets/add").permitAll()
                        .requestMatchers("/api/uploadToDrive").permitAll()
                        .requestMatchers("/api/filtered").permitAll()
                        .requestMatchers("/api/get-folderId").permitAll()

                        .anyRequest().authenticated()) // Protect other requests
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(new JwtAuthorizationFilter(jwtSecret, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
