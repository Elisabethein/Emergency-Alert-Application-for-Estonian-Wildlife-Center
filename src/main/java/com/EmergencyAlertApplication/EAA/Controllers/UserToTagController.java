package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.Services.UserToTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userToTags")
public class UserToTagController {
    private final UserToTagService userToTagService;

    @Autowired
    public UserToTagController(UserToTagService userToTagService) {
        this.userToTagService = userToTagService;
    }
}
