package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;


    public Account saveAccount(Account account) {

        if (account.getUser() == null) {
            throw new IllegalArgumentException("Un compte doit être associé à un utilisateur");
        }
        return accountRepository.save(account);
    }


}
