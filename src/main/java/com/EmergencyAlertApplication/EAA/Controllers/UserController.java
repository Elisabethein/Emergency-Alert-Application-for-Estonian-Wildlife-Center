package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.DTOs.CreateUserDTO;
import com.EmergencyAlertApplication.EAA.DTOs.EditUserDTO;
import com.EmergencyAlertApplication.EAA.DTOs.SpeciesDTO;
import com.EmergencyAlertApplication.EAA.Entities.*;
import com.EmergencyAlertApplication.EAA.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserToRoleService userToRoleService;
    private final UserToTagService userToTagService;
    private final UserToRegionService userToRegionService;
    private final UserToSpeciesService userToSpeciesService;
    private final RegionService regionService;
    private final TagService tagService;
    private final ApplicationService applicationService;
    private final RoleService roleService;


    @Autowired
    public UserController(UserService userService, UserToRoleService userToRoleService, UserToTagService userToTagService, UserToRegionService userToRegionService, UserToSpeciesService userToSpeciesService, RegionService regionService, TagService tagService, ApplicationService applicationService, RoleService roleService) {
        this.userService = userService;
        this.userToRoleService = userToRoleService;
        this.userToTagService = userToTagService;
        this.userToRegionService = userToRegionService;
        this.userToSpeciesService = userToSpeciesService;
        this.regionService = regionService;
        this.tagService = tagService;
        this.applicationService = applicationService;
        this.roleService = roleService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<List<String>> getUserRoles(@PathVariable UUID id) {
        try {
            List<String> roles = userToRoleService.getUserRoles(id);
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonList("Error occurred: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<List<String>> getUserTags(@PathVariable UUID id) {
        try {
            List<String> tags = userToTagService.getUserTags(id);
            return ResponseEntity.ok(tags);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonList("Error occurred: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}/regions")
    public ResponseEntity<List<String>> getUserRegions(@PathVariable UUID id) {
        try {
            List<String> regions = userToRegionService.getUserRegions(id);
            return ResponseEntity.ok(regions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonList("Error occurred: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}/species")
    public ResponseEntity<List<SpeciesDTO>> getUserSpecies(@PathVariable UUID id) {
        try {
            List<UserToSpecies> species = userToSpeciesService.getUserSpecies(id);
            List<SpeciesDTO> speciesDTOs = species.stream().map(species1 -> new SpeciesDTO(species1.getUpperSpecies().getName(), species1.isExpert())).toList();
            return ResponseEntity.ok(speciesDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<User> editUser(@PathVariable UUID id, @RequestBody EditUserDTO editUserDTO) {
        try {
            userService.editUser(id, editUserDTO);
            User editedUser = userService.getUserById(id);
            //System.out.println(editedUser.getId());
            userToRegionService.editUserRegions(id, editedUser, editUserDTO.getRegions());
            userToRoleService.editUserRoles(id, editedUser, editUserDTO.getRoles());
            userToTagService.editUserTags(id, editedUser, editUserDTO.getTags());
            userToSpeciesService.editUserSpecies(id, editedUser, editUserDTO.getSpecies());
            return ResponseEntity.ok(editedUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addUser(@RequestBody CreateUserDTO createUserDTO) {
        try {
            User userWithEmail = userService.getUserByEmail(createUserDTO.getEmail());
            if (userWithEmail != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with email already exists");
            }
            User user = new User();
            user.setFirstName(createUserDTO.getFirstName());
            user.setLastName(createUserDTO.getLastName());

            String birthDate = createUserDTO.getBirthDate();
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(birthDate);
            java.time.LocalDate localDate = offsetDateTime.toLocalDate();
            Date sqlDate = Date.valueOf(localDate);
            user.setBirthDate(sqlDate);

            user.setEmail(createUserDTO.getEmail());
            user.setPhoneNr(createUserDTO.getPhoneNr());
            user.setCounty(createUserDTO.getCounty());
            user.setCity(createUserDTO.getCity());
            user.setStreetName(createUserDTO.getStreetName());
            user.setStreetNr(createUserDTO.getStreetNr());
            user.setPostalCode(createUserDTO.getPostalCode());
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            user.setPassword(createUserDTO.getPassword());
            user = userService.save(user);

            Region region = regionService.getRegionByName(createUserDTO.getCounty());
            if (region != null) {
                userToRegionService.addUserToRegion(user, region);
            }
            if (createUserDTO.getTags() != null) {
                User finalUser = user;
                createUserDTO.getTags().forEach(tag -> {
                    Tag tagObject = tagService.getByTagFunction(tag);
                    if (tagObject != null) {
                        userToTagService.addUserToTag(finalUser, tagObject);
                    }
                });
            }

            Optional<Application> application = applicationService.getById(UUID.fromString(createUserDTO.getId()));
            if (application.isPresent()) {
                application.get().setIsAccepted(true);
                applicationService.save(application.get());
            }

            Role role = roleService.getRoleByName("Vabatahtlik");
            UserToRole userToRole = new UserToRole();
            userToRole.setUser(user);
            userToRole.setRole(role);
            userToRoleService.save(userToRole);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            System.err.println("Error adding user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User addition failed: " + e.getMessage());
        }
    }

}
