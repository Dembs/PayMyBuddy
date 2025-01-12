package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showProfile(HttpSession session, Model model) {
        //Récupération de la session à partir de l'ID
        Integer userID = (Integer) session.getAttribute("userId");
        if (userID == null){
            return "redirect:/login";
        }

        User user = userService.getUserById(userID)
                               .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user",user);
        return ("/profile");
    }
}
