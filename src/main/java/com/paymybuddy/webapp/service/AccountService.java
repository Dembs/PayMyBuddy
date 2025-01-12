package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account saveAccount(Account account) {
        // Add validation if needed
        if (account.getUser() == null) {
            throw new IllegalArgumentException("Un compte doit être associé à un utilisateur");
        }
        return accountRepository.save(account);
    }

    public Optional<Account> getAccountById (int id){
        return accountRepository.findById(id);
    }

    public List<Account> getAllAccounts (){
        return accountRepository.findAll();
    }

    public boolean deleteAccount(int id) {
        Optional<Account> existingAccount = accountRepository.findById(id);
        if (existingAccount.isPresent()) {
            accountRepository.deleteById(id);
            return true;
        }
        throw new IllegalArgumentException("Compte non trouvé avec l'ID : " + id);
    }

    public Account updateAccount(int id, Account updatedAccount) {
        // Find existing account
        Account existingAccount = accountRepository.findById(id)
                                                   .orElseThrow(() -> new IllegalArgumentException("Compte non trouvé avec l'ID : " + id));

        // Ensure the user association remains the same
        if (updatedAccount.getUser() != null) {
            existingAccount.setUser(updatedAccount.getUser());
        }
        // Save and return the updated account
        return accountRepository.save(existingAccount);
    }


}
