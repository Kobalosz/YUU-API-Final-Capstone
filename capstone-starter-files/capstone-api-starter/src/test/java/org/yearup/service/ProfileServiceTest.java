package org.yearup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yearup.models.Profile;
import org.yearup.repository.ProfileRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProfileServiceTest
{
    private ProfileRepository profileRepository;
    private ProfileService profileService;

    @BeforeEach
    void setUp()
    {
        profileRepository = mock(ProfileRepository.class);
        profileService = new ProfileService(profileRepository);
    }

    @Test
    void create_savesAndReturnsProfile()
    {
        // arrange
        Profile profile = new Profile(1, "Joe", "Joesephus", "800-555-1234", "joe@email.com", "789 Oak", "Dallas", "TX", "75051");
        when(profileRepository.save(profile)).thenReturn(profile);

        // act
        Profile result = profileService.create(profile);

        // assert
        assertThat(result.getFirstName()).isEqualTo("Joe");
        verify(profileRepository).save(profile);
    }

    @Test
    void getProfileById_returnsTheCorrectProfile()
    {
        // arrange
        Profile profile = new Profile(1, "Joe", "Joesephus", "800-555-1234", "joe@email.com", "789 Oak", "Dallas", "TX", "75051");
        when(profileRepository.findById(1)).thenReturn(Optional.of(profile));

        // act
        Profile result = profileService.GetProfileById(1);

        // assert
        assertThat(result.getLastName()).isEqualTo("Joesephus");
    }

    @Test
    void update_existingProfile()
    {
        // arrange
        Profile existing = new Profile(1, "Joe", "Joesephus", "800-555-1234", "joe@email.com", "789 Oak", "Dallas", "TX", "75051");
        when(profileRepository.findById(1)).thenReturn(Optional.of(existing));
        when(profileRepository.save(existing)).thenReturn(existing);

        Profile updated = new Profile(1, "Joseph", "Smith", "111-222-3333", "new@email.com", "1 New St", "Austin", "TX", "73301");

        // act
        profileService.update(1, updated);

        // assert
        assertThat(existing.getFirstName()).isEqualTo("Joseph");
        assertThat(existing.getCity()).isEqualTo("Austin");
        assertThat(existing.getEmail()).isEqualTo("new@email.com");
    }
}
