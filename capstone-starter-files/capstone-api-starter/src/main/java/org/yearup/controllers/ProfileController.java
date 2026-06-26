package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("profile")
@PreAuthorize("isAuthenticated()")
public class ProfileController
{
    private final ProfileService profileService;
    private final UserService userService;

    public ProfileController(ProfileService profileService, UserService userService)
    {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    public Profile getProfile(Principal principal)
    {
        String username = principal.getName();
        User user = userService.getByUserName(username);
        return profileService.GetProfileById(user.getId());
    }

    @PutMapping
    public Profile updateProfile(Principal principal, @RequestBody Profile profile)
    {
        String username = principal.getName();
        User user = userService.getByUserName(username);
        return profileService.update(user.getId(), profile);
    }


}
