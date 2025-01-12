package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.LogInService;
import com.paymybuddy.webapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")

public class LogInController {
    @Autowired
    private UserService userService;

    @Autowired
    private LogInService logInService;

    @GetMapping
    public String showLoginForm(){
        return "login";
    }

    @PostMapping
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               Model model,
                               HttpSession session) {
        try {
            User user = userService.authenticateUser(email,password);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userEmail", user.getEmail());

            return "redirect:/profile";
        }
        catch (RuntimeException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "login";
        }
    }
}
