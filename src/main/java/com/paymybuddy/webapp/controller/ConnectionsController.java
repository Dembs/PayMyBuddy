package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.service.ConnectionsService;
import com.paymybuddy.webapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/connections")
public class ConnectionsController {
/*
    @Autowired
    private UserService userService;

    @Autowired
    private ConnectionsService connectionsService;

 */
    @Autowired
    private UserRepository userRepository;

    private final UserService userService;
    private final ConnectionsService connectionsService;

    // Constructeur au lieu de @Autowired
    public ConnectionsController(UserService userService,
                                 ConnectionsService connectionsService) {
        this.userService = userService;
        this.connectionsService = connectionsService;
    }

    @GetMapping
    public String showConnectionForm (Model model){
        return "/connections";
    }

    @PostMapping
    public String processConnection(@RequestParam String email,
                                    Model model,
                                    @AuthenticationPrincipal UserDetails userDetails){

        try {
            User currentUser = userRepository.findByEmail(userDetails.getUsername())
                                          .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

            connectionsService.addConnection(currentUser, email);
            model.addAttribute("successMessage", "Relation Rajoutée avec succes");
            return "connections";

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "connections";
        }
    }
}
