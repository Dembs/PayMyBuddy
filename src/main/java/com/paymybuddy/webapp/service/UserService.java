package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

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
        //Nouveau et ancien mot de passe pas vide
        if (currentPassword == null) {
            throw new IllegalArgumentException("Le mot de passe actuel est requis");
        }
        if (newPassword == null) {
            throw new IllegalArgumentException("Le nouveau mot de passe est requis");
        }
        //longueur du mot de passe
        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("Le nouveau mot de passe doit contenir au moins 8 caractères");
        }

        // nouveau mot de passe différent de l'ancien
        if (currentPassword.equals(newPassword)) {
            throw new IllegalArgumentException("Le nouveau mot de passe doit être différent de l'ancien");
        }
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


}

