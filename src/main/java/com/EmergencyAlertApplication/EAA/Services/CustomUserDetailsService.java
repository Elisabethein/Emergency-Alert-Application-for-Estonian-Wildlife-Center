package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.DTOs.CustomUserDetails;
import com.EmergencyAlertApplication.EAA.Entities.User;
import com.EmergencyAlertApplication.EAA.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserToRoleService userToRoleService;
    private final UserToRegionService userToRegionService;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, UserToRoleService userToRoleService, UserToRegionService userToRegionService) {
        this.userRepository = userRepository;
        this.userToRoleService = userToRoleService;
        this.userToRegionService = userToRegionService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Attempting to find user with email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        System.out.println("User found: " + user.getEmail());
        List<String> roles = userToRoleService.getUserRoles(user.getId());

        List<String> regions = userToRegionService.getUserRegions(user.getId());

        // Create and return an instance of CustomUserDetails
        return new CustomUserDetails(user, roles, regions);
    }

}
