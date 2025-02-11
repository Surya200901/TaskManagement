package com.taskmanagement.controller;

import com.taskmanagement.model.Profile;
import com.taskmanagement.model.User;
import com.taskmanagement.service.ProfileService;
import com.taskmanagement.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;

    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    public String viewProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOptional = userService.findByUsername(username);
        if (!userOptional.isPresent()) {
            return "redirect:/login";
        }

        User user = userOptional.get();
        Optional<Profile> profileOptional = profileService.getProfileByUser(user);

        Profile profile = profileOptional.orElseGet(() -> {
            Profile newProfile = new Profile();
            newProfile.setUser(user);
            return newProfile;
        });

        model.addAttribute("profile", profile);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute Profile profile, Model model) {
        profileService.saveProfile(profile); // Save updated profile

        // Fetch the updated profile from the database
        Optional<Profile> updatedProfileOptional = profileService.getProfileByUser(profile.getUser());
        if (updatedProfileOptional.isPresent()) {
            model.addAttribute("profile", updatedProfileOptional.get());
        }

        model.addAttribute("profileUpdated", true); // Flag to show updated details
        return "profile"; // Return profile page
    }
}