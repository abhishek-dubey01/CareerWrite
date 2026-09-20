package com.careerwrite.controller;

import com.careerwrite.entity.Profile;
import com.careerwrite.service.ProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{userId}")
    public Profile getProfile(@PathVariable Long userId) {
        return profileService.getByUserId(userId);
    }

    @PutMapping("/{userId}")
    public Profile updateProfile(@PathVariable Long userId, @RequestBody Profile profile) {
        return profileService.saveOrUpdate(userId, profile);
    }
}
