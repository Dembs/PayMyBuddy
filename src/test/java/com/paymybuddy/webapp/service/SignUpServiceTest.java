package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SignUpService signUpService;

    private UserDTO testUserDTO;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUserDTO = new UserDTO("test@test.com", "password123", "testUser");

        testUser = new User();
        testUser.setEmail(testUserDTO.getEmail());
        testUser.setUsername(testUserDTO.getUsername());
        testUser.setPassword("encodedPassword");
    }

    @Test
    void registerNewUserTest() {
        when(userRepository.existsByEmail(testUserDTO.getEmail())).thenReturn(false); //Utilisateur pas enregistré encore
        when(passwordEncoder.encode(testUserDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(accountService.saveAccount(any(Account.class))).thenReturn(new Account());

        User result = signUpService.registerNewUser(testUserDTO);

        assertNotNull(result);
        assertEquals(testUserDTO.getEmail(), result.getEmail());
        assertEquals(testUserDTO.getUsername(), result.getRealUsername());
        verify(accountService).saveAccount(any(Account.class));
    }

    @Test
    void registerNewUserEmailErrorTest() {

        when(userRepository.existsByEmail(testUserDTO.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class, () ->
                signUpService.registerNewUser(testUserDTO)
        );
    }
    @Test
    void registerNewUserPasswordErrorTest() {
        testUserDTO.setPassword("short");

        assertThrows(NullPointerException.class, () ->
                signUpService.registerNewUser(testUserDTO)
        );
    }

}