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

import java.util.List;

@Controller
@RequestMapping("/connections")
public class ConnectionsController {
/*
    @Autowired
    private UserService userService;

    @Autowired
    private ConnectionsService connectionsService;

 */

    private final UserService userService;
    private final ConnectionsService connectionsService;

    // Constructeur au lieu de @Autowired
    public ConnectionsController(UserService userService,
                                 ConnectionsService connectionsService) {
        this.userService = userService;
        this.connectionsService = connectionsService;
    }

    @GetMapping
    public String showConnectionForm (Model model, @AuthenticationPrincipal User user){
        List<User> connections = user.getConnections();
        model.addAttribute("connections",connections);
        return "/connections";
    }

    @PostMapping
    public String processConnection(@RequestParam String email,
                                    Model model,
                                    @AuthenticationPrincipal User user){

        try {
            connectionsService.addConnection(user,email);
            model.addAttribute("successMessage", "Relation Rajoutée avec succes");

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("connections",user.getConnections());
        return "connections";
    }
}
