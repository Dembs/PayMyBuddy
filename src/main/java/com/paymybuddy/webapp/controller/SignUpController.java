package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showSignupForm(Model model) {
        if (!model.containsAttribute("userDTO")) {
            model.addAttribute("userDTO", new UserDTO());
        }
        return "signup";
    }

    @PostMapping
    public String processSignup(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        try {
            User registeredUser = userService.registerNewUser(userDTO);

            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";
        }
    }
}