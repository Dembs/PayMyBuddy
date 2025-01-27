package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.AccountService;
import com.paymybuddy.webapp.service.TransactionService;
import com.paymybuddy.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/transfert")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String showTransferPage(@AuthenticationPrincipal User user, Model model){
        User currentUser = userService.getUserById(user.getId());

        double balance = transactionService.getUserBalance(currentUser.getId());

        List<Transaction>transactions = transactionService.getTransactionByUser(currentUser.getId());

        model.addAttribute("user", currentUser);
        model.addAttribute("balance", balance);
        model.addAttribute("transactions",transactions);

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
            // Vérification que le montant est positif
            if (amount <= 0) {
                throw new IllegalArgumentException("Le montant doit être positif");
            }
            User currentUser = userService.getUserById(user.getId());
            Transaction transaction = new Transaction();
            transaction.setType(type);
            transaction.setSender(currentUser);
            transaction.setDate(new Timestamp(System.currentTimeMillis()));

            // Le signe du montant est géré ici, pas dans l'input
            if (type.contains("VIREMENT")) {
                transaction.setReceiver(currentUser);
                transaction.setDescription(type);
                transaction.setAmount(type.equals("VIREMENT SORTANT") ? -amount : amount);
            } else if (type.equals("TRANSFERT")) {
                User receiver = userService.getUserById(receiverId);
                transaction.setReceiver(receiver);
                transaction.setDescription(description);
                transaction.setAmount(-amount); // Transfert sortant
            }

            transactionService.saveTransaction(transaction);
            redirectAttributes.addFlashAttribute("successMessage", "Transaction effectuée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/transfert";
    }
}