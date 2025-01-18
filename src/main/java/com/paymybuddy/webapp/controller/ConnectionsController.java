package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.ConnectionsService;
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
@RequestMapping("/connections")
public class ConnectionsController {
    @Autowired
    private UserService userService;

    @Autowired
    private ConnectionsService connectionsService;

    @GetMapping
    public String showConnectionForm (HttpSession session, Model model){
        //Check if the user is logged in
        Integer userId = (Integer) session.getAttribute("userId");
        if(userId == null){
            return "redirect:/login";
        }
        return "/connections";
    }

    @PostMapping
    public String processConnection(@RequestParam String email,
                                    HttpSession session,
                                    Model model){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            User currentUser = userService.getUserById(userId)
                                          .orElseThrow(() -> new RuntimeException("User not found"));

            connectionsService.addConnection(currentUser, email);
            model.addAttribute("successMessage", "Relation Rajoutée avec succes");
            return "connections";

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "connections";
        }
    }
}
