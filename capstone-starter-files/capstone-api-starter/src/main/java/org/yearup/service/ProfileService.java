package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Profile;
import org.yearup.repository.ProfileRepository;

@Service
public class ProfileService
{
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository)
    {
        this.profileRepository = profileRepository;
    }

    public Profile create(Profile profile)
    {
        return profileRepository.save(profile);
    }

    public Profile GetProfileById(int id)
    {
        return profileRepository.findById(id).orElse(null);
    }

    public Profile update(int id, Profile profile)
    {
        Profile oldProfile = profileRepository.findById(id).orElse(null);
        oldProfile.setAddress(profile.getAddress());
        oldProfile.setCity(profile.getCity());
//        Bruh...
        oldProfile.setZip(profile.getZip());
        oldProfile.setState(profile.getState());
        oldProfile.setEmail(profile.getEmail());
        oldProfile.setPhone(profile.getPhone());
        oldProfile.setFirstName(profile.getFirstName());
        oldProfile.setLastName(profile.getLastName());
        return profileRepository.save(oldProfile);
    }

}
