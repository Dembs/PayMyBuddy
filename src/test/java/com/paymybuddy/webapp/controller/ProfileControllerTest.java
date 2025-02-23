package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private ProfileController profileController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("test@test.com");
        testUser.setUsername("testUser");
    }

    @Test
    void updateProfileUsernameTest() {
        String viewName = profileController.updateProfile(
                testUser,
                "newUsername",
                null,
                redirectAttributes
        );

        assertEquals("redirect:/profile", viewName);
        verify(userService).updateUsername(1, "newUsername");
        verify(redirectAttributes).addFlashAttribute("successMessage", "Nom d'utilisateur mis à jour");
    }

    @Test
    void updateProfileEmailTest() {
        String viewName = profileController.updateProfile(
                testUser,
                null,
                "new@test.com",
                redirectAttributes
        );

        assertEquals("redirect:/profile", viewName);
        verify(userService).updateEmail(1, "new@test.com");
        verify(redirectAttributes).addFlashAttribute("successMessage", "Email mis à jour");
    }

    @Test
    void updateProfilePasswordTest() {
        String viewName = profileController.updatePassword(
                testUser,
                "currentPass",
                "newPass",
                "newPass",
                redirectAttributes
        );

        assertEquals("redirect:/profile", viewName);
        verify(userService).updatePassword(1, "currentPass", "newPass");
        verify(redirectAttributes).addFlashAttribute("successMessage", "Mot de passe mis à jour avec succès");
    }

    @Test
    void updateProfilePasswordErrorTest() {
        String viewName = profileController.updatePassword(
                testUser,
                "currentPass",
                "newPass",
                "differentPass",
                redirectAttributes
        );

        assertEquals("redirect:/profile", viewName);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Les mots de passe ne correspondent pas");
        verify(userService, never()).updatePassword(anyInt(), anyString(), anyString());
    }
}