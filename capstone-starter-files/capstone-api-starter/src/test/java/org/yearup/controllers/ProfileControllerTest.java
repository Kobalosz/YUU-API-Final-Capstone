package org.yearup.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProfileControllerTest
{
    private ProfileService profileService;
    private UserService userService;
    private ProfileController profileController;

    @BeforeEach
    void setUp()
    {
        profileService = mock(ProfileService.class);
        userService = mock(UserService.class);
        profileController = new ProfileController(profileService, userService);
    }

    @Test
    void getProfile_returnsProfileForLoggedInUser()
    {
        // arrange
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("user");
        when(userService.getByUserName("user")).thenReturn(new User(1, "user", "password", "ROLE_USER"));
        Profile profile = new Profile(1, "Joe", "Joesephus", "800-555-1234", "joe@email.com", "789 Oak", "Dallas", "TX", "75051");
        when(profileService.GetProfileById(1)).thenReturn(profile);

        // act
        Profile result = profileController.getProfile(principal);

        // assert
        assertThat(result.getFirstName()).isEqualTo("Joe");
    }

    @Test
    void updateProfile_updatesProfileForLoggedInUser()
    {
        // arrange
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("user");
        when(userService.getByUserName("user")).thenReturn(new User(1, "user", "password", "ROLE_USER"));
        Profile updated = new Profile(1, "Joseph", "Smith", "111", "new@email.com", "1 New St", "Austin", "TX", "73301");
        when(profileService.update(1, updated)).thenReturn(updated);

        // act
        Profile result = profileController.updateProfile(principal, updated);

        // assert
        assertThat(result.getFirstName()).isEqualTo("Joseph");
        verify(profileService).update(1, updated);
    }
}
