package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @GetMapping
    public String showSignupForm() {
        return "signup";
    }

    @PostMapping
    public String processSignup(@RequestParam String username,
                                @RequestParam String email,
                                @RequestParam String password,
                                Model model) {
        try {
            UserDTO userDTO = new UserDTO(email, password,username);
            signUpService.registerNewUser(userDTO);

            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";
        }
    }
}