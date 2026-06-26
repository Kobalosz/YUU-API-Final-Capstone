package org.yearup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.yearup.models.User;
import org.yearup.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest
{
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp()
    {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void getByUserName_returnsTheCorrectUser()
    {
        // arrange
        User user = new User(1, "user", "password", "ROLE_USER");
        when(userRepository.findByUsername("user")).thenReturn(user);

        // act
        User result = userService.getByUserName("user");

        // assert
        assertThat(result.getUsername()).isEqualTo("user");
    }

    @Test
    void getIdByUsername_returnsTheId_whenUserExists()
    {
        // arrange
        User user = new User(7, "user", "password", "ROLE_USER");
        when(userRepository.findByUsername("user")).thenReturn(user);

        // act
        int result = userService.getIdByUsername("user");

        // assert
        assertThat(result).isEqualTo(7);
    }

    @Test
    void getIdByUsername_returnsNegativeOne_whenUserMissing()
    {
        // arrange
        when(userRepository.findByUsername("ghost")).thenReturn(null);

        // act
        int result = userService.getIdByUsername("ghost");

        // assert
        assertThat(result).isEqualTo(-1);
    }

    @Test
    void exists_returnsTrue_whenUsernameTaken()
    {
        // arrange
        when(userRepository.existsByUsername("user")).thenReturn(true);

        // act
        boolean result = userService.exists("user");

        // assert
        assertThat(result).isTrue();
    }

    @Test
    void create_encodesPassword_beforeSaving()
    {
        // arrange
        User user = new User(0, "newuser", "plaintext", "ROLE_USER");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // act
        userService.create(user);

        // assert
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.getPassword()).isNotEqualTo("plaintext");
        assertThat(new BCryptPasswordEncoder().matches("plaintext", saved.getPassword())).isTrue();
    }
}
