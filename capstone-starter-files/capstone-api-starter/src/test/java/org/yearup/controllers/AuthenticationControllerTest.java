package org.yearup.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.User;
import org.yearup.models.authentication.LoginDto;
import org.yearup.models.authentication.RegisterUserDto;
import org.yearup.security.jwt.TokenProvider;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest
{
    private TokenProvider tokenProvider;
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private ProfileService profileService;
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp()
    {
        tokenProvider = mock(TokenProvider.class);
        authenticationManager = mock(AuthenticationManager.class);
        userService = mock(UserService.class);
        profileService = mock(ProfileService.class);
        authenticationController = new AuthenticationController(tokenProvider, authenticationManager, userService, profileService);
    }

    @Test
    void login_returnsTokenAndUser_whenCredentialsValid()
    {
        // arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("user");
        loginDto.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(tokenProvider.createToken(any(), anyBoolean())).thenReturn("jwt-token");
        when(userService.getByUserName("user")).thenReturn(new User(1, "user", "password", "ROLE_USER"));

        // act
        ResponseEntity<?> result = authenticationController.login(loginDto);

        // assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getHeaders().getFirst("Authorization")).isEqualTo("Bearer jwt-token");
    }

    @Test
    void login_throwsUnauthorized_whenCredentialsInvalid()
    {
        // arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("user");
        loginDto.setPassword("wrong");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad"));

        // act + assert
        assertThrows(ResponseStatusException.class, () -> authenticationController.login(loginDto));
    }

    @Test
    void register_createsUserAndProfile_returnsCreated()
    {
        // arrange
        RegisterUserDto newUser = new RegisterUserDto();
        newUser.setUsername("newuser");
        newUser.setPassword("password");
        newUser.setConfirmPassword("password");
        newUser.setRole("ROLE_USER");

        when(userService.exists("newuser")).thenReturn(false);
        when(userService.create(any(User.class))).thenReturn(new User(5, "newuser", "password", "ROLE_USER"));

        // act
        ResponseEntity<User> result = authenticationController.register(newUser);

        // assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody().getId()).isEqualTo(5);
        verify(profileService).create(any());
    }

    @Test
    void register_throwsBadRequest_whenUserExists()
    {
        // arrange
        RegisterUserDto newUser = new RegisterUserDto();
        newUser.setUsername("user");
        newUser.setPassword("password");
        newUser.setConfirmPassword("password");
        newUser.setRole("ROLE_USER");
        when(userService.exists("user")).thenReturn(true);

        // act + assert
        assertThrows(ResponseStatusException.class, () -> authenticationController.register(newUser));
    }
}
