package com.taskmanagement.service;

import com.taskmanagement.model.Profile;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public Optional<Profile> getProfileByUser(User user) {
        return profileRepository.findByUser(user);
    }

    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }
}
