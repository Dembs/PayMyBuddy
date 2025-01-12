package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.AccountRepository;
import com.paymybuddy.webapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    public User saveUser(User user){

        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUser(int id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        throw new IllegalArgumentException("Utilisateur non trouvé avec l'ID : " + id);
    }
    public User updateUser(int id, User updatedUser) {

        User existingUser = userRepository.findById(id)
                                          .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID : " + id));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());

       /* // Only update password if a new password is provided
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(updatedUser.getPassword());
        }*/

        // Save and return the updated user
        return userRepository.save(existingUser);
    }

    @Transactional
    public User registerNewUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());

        User savedUser = userRepository.save(user);

        // Create and associate an account
        Account account = new Account();
        account.setUser(savedUser);
        accountService.saveAccount(account);

        return savedUser;
    }

}

