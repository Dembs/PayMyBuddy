package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.AccountService;
import com.paymybuddy.webapp.service.TransactionService;
import com.paymybuddy.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/transfer")
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

        model.addAttribute("balance", balance);

        return "transfer";
    }
}