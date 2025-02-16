package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.TransactionService;
import com.paymybuddy.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/transfert")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String showTransferPage(@AuthenticationPrincipal User user, Model model,
                                   @RequestParam(defaultValue = "0") int page){
        User currentUser = userService.getUserById(user.getId());

        double balance = transactionService.getUserBalance(currentUser.getId());

        Map<String, Object> pageData = transactionService.getTransactionByUser(currentUser.getId(), page, 5);

        model.addAttribute("user", currentUser);
        model.addAttribute("balance", balance);
        model.addAttribute("transactions", pageData.get("transactions"));
        model.addAttribute("currentPage", pageData.get("currentPage"));
        model.addAttribute("totalPages", pageData.get("totalPages"));

        return "transfert";
    }

    @PostMapping("/add")
    public String addTransaction(@AuthenticationPrincipal User user,
                                 @RequestParam String type,
                                 @RequestParam Double amount,
                                 @RequestParam(required = false) String description,
                                 @RequestParam(required = false) Integer receiverId,
                                 RedirectAttributes redirectAttributes) {
        try {
            transactionService.processTransaction(user.getId(), type, amount,
                    description, receiverId);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Transaction effectuée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/transfert";
    }
}