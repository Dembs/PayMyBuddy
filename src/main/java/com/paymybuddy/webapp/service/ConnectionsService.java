package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionsService {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void addConnection(User user, String friendEmail){
        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(()-> new RuntimeException("Email " + friendEmail + " introuvable"));

        //Check to see if the connection already exists
        if(user.getConnections().contains(friend)){
            throw new RuntimeException("Contact déjà rajouté");
        }

        user.getConnections().add(friend);
        userRepository.save(user);
    }
}
