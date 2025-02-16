package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showProfile(@AuthenticationPrincipal User user, Model model) {
        User updatedUser = userService.getUserById(user.getId());
        model.addAttribute("user",updatedUser);
        return ("/profile");
    }

    @PostMapping("/update")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam(required = false) String username,
                                @RequestParam(required = false) String email,
                                RedirectAttributes redirectAttributes) {
        try {
            if (username != null) {
                userService.updateUsername(user.getId(), username);
                redirectAttributes.addFlashAttribute("successMessage", "Nom d'utilisateur mis à jour");
            }
            if (email != null) {
                userService.updateEmail(user.getId(), email);
                redirectAttributes.addFlashAttribute("successMessage", "Email mis à jour");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/password")
    public String updatePassword(@AuthenticationPrincipal User user,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (!newPassword.equals(confirmPassword)) {
                throw new RuntimeException("Les mots de passe ne correspondent pas");
            }
            userService.updatePassword(user.getId(), currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Mot de passe mis à jour avec succès");
            return "redirect:/profile";
        }
        catch (IllegalArgumentException e) {
            // Erreurs de validation
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/profile";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/profile";
        }
    }
}
