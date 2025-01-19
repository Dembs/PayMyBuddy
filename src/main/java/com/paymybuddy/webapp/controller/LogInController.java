package com.paymybuddy.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")

public class LogInController {

    @GetMapping
    public String showLoginForm(){
        return "login";
    }
}
