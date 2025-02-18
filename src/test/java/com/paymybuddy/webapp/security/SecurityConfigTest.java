package com.paymybuddy.webapp.security;

import com.paymybuddy.webapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private UserService userService;

    @Mock
    private HttpSecurity http;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig(userService);
    }

    @Test
    void passwordEncoderTest() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
        String password = "testPassword";
        String encodedPassword = encoder.encode(password);
        assertTrue(encoder.matches(password, encodedPassword));
    }

    @Test
    void authenticationProviderTest() {
        AuthenticationProvider provider = securityConfig.authenticationProvider();

        assertNotNull(provider);
        assertTrue(provider instanceof DaoAuthenticationProvider);
    }
    @Test
    void filterChainTest() throws Exception {
        // Given
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.csrf(any())).thenReturn(http);
        when(http.formLogin(any())).thenReturn(http);
        when(http.logout(any())).thenReturn(http);
        when(http.authenticationProvider(any())).thenReturn(http);

        DefaultSecurityFilterChain mockChain = mock(DefaultSecurityFilterChain.class);
        when(http.build()).thenReturn(mockChain);

        // When
        SecurityFilterChain filterChain = securityConfig.filterChain(http);

        // Then
        assertNotNull(filterChain);
        verify(http).authorizeHttpRequests(any());
        verify(http).csrf(any());
        verify(http).formLogin(any());
        verify(http).logout(any());
        verify(http).authenticationProvider(any());
    }

}