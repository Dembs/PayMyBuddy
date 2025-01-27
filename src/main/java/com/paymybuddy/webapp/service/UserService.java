package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private AccountService accountService;

   /* @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;*/

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       @Lazy PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));
    }

    @Transactional
    public User registerNewUser(UserDTO userDTO){
        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new RuntimeException("Addresse Mail déjà utilisée");
        }
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return userRepository.save(user);
    }

    @Transactional
    public void updateUsername(int userId, String newUsername) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier si le username n'est pas déjà utilisé
        if (userRepository.existsByUsername(newUsername) &&
                !user.getUsername().equals(newUsername)) {
            throw new RuntimeException("Ce nom d'utilisateur est déjà utilisé");
        }

        user.setUsername(newUsername);
        userRepository.save(user);
    }

    @Transactional
    public void updateEmail(int userId, String newEmail) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier si l'email n'est pas déjà utilisé
        if (userRepository.existsByEmail(newEmail) &&
                !user.getEmail().equals(newEmail)) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(int userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier l'ancien mot de passe
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Mot de passe actuel incorrect");
        }

        // Encoder et sauvegarder le nouveau mot de passe
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public User getUserById(int userId) {
        return userRepository.findById(userId)
                             .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
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

        return userRepository.save(existingUser);
    }

}

