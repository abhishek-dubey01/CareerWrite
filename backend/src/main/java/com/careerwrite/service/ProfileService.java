package com.careerwrite.service;

import com.careerwrite.entity.Profile;
import com.careerwrite.repository.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile getByUserId(Long userId) {
        // If the job seeker hasn't filled in their profile yet, just return
        // an empty one instead of a 404 - the frontend edit form still works.
        return profileRepository.findByUserId(userId)
                .orElse(new Profile(userId, "", "", "", ""));
    }

    public Profile saveOrUpdate(Long userId, Profile incoming) {
        Profile profile = profileRepository.findByUserId(userId).orElse(new Profile());
        profile.setUserId(userId);
        profile.setPhone(incoming.getPhone());
        profile.setSkills(incoming.getSkills());
        profile.setEducation(incoming.getEducation());
        profile.setExperience(incoming.getExperience());
        return profileRepository.save(profile);
    }
}
