package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.Entities.Role;
import com.EmergencyAlertApplication.EAA.Entities.User;
import com.EmergencyAlertApplication.EAA.Entities.UserToRole;
import com.EmergencyAlertApplication.EAA.Repositories.UserToRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserToRoleService {
    private final UserToRoleRepository userToRoleRepository;
    private final RoleService roleService;

    @Autowired
    public UserToRoleService(UserToRoleRepository userToRoleRepository, RoleService roleService) {
        this.userToRoleRepository = userToRoleRepository;
        this.roleService = roleService;
    }

    public List<String> getUserRoles(UUID id) {
        List<UserToRole> userToRoles = userToRoleRepository.findByUserId(id);
        return userToRoles.stream().map(userToRole -> userToRole.getRole().getRole()).toList();
    }

    @Transactional
    public void editUserRoles(UUID id, User editedUser, List<String> roles) {
        try {
            userToRoleRepository.deleteByUserId(id);
        } catch (Exception e) {
            System.err.println("Error deleting user roles: " + e.getMessage());
            throw e;
        }
        roles.forEach(role -> {
            Role roleObject = roleService.getRoleByName(role);
            UserToRole userToRole = new UserToRole();
            userToRole.setUser(editedUser);
            userToRole.setRole(roleObject);
            userToRoleRepository.save(userToRole);
        });
    }

    public void save(UserToRole userToRole) {
        userToRoleRepository.save(userToRole);
    }
}
