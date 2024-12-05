package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.DTOs.NewRoleDTO;
import com.EmergencyAlertApplication.EAA.Entities.Role;
import com.EmergencyAlertApplication.EAA.Services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/roles", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class RoleController {
    private final RoleService roleService;
    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/all")
    public List<Role> getRoles() {
        return roleService.getRoles();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addRole(@RequestBody NewRoleDTO newRoleDTO) {
        try {
            Role newrole = new Role();
            newrole.setRole(newRoleDTO.getName());
            roleService.save(newrole);
            return ResponseEntity.ok("Role added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
