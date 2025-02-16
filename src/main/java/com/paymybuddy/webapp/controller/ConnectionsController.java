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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

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
    public String showConnectionsPage(@AuthenticationPrincipal User user,
                                      Model model,
                                      @RequestParam(defaultValue = "0") int page) {
        Map<String, Object> pageData = connectionsService.getConnectionsByUser(user, page, 5);

        model.addAttribute("connections", pageData.get("connections"));
        model.addAttribute("currentPage", pageData.get("currentPage"));
        model.addAttribute("totalPages", pageData.get("totalPages"));

        return "/connections";
    }

    @PostMapping
    public String processConnection(@AuthenticationPrincipal User user,
                                    @RequestParam String email,
                                    RedirectAttributes redirectAttributes,
                                    Model model){

        try {
            connectionsService.addConnection(user,email);
            model.addAttribute("successMessage", "Relation Rajoutée avec succes");
            return "redirect:/connections";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());

            //Retour des données de pagination en cas d'erreur
            Map<String, Object> pageData = connectionsService.getConnectionsByUser(user, 0, 5);

            model.addAttribute("connections", pageData.get("connections"));
            model.addAttribute("currentPage", pageData.get("currentPage"));
            model.addAttribute("totalPages", pageData.get("totalPages"));
            model.addAttribute("errorMessage", e.getMessage());
            return "connections";
        }

    }
}
