package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConnectionsService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Map<String, Object> getConnectionsByUser(User user, int page, int size) {
        List<User> allConnections = user.getConnections();
        int start = page * size;
        int end = Math.min((start + size), allConnections.size());

        List<User> paginatedConnections = allConnections.subList(start, end);
        int totalPages = (int) Math.ceil((double) allConnections.size() / size);

        Map<String, Object> result = new HashMap<>();
        result.put("connections", paginatedConnections);
        result.put("currentPage", page);
        result.put("totalPages", totalPages);

        return result;
    }

    @Transactional
    public void addConnection(User user, String friendEmail){

       if(user.getEmail().equals(friendEmail)){
           throw new RuntimeException("Ajout impossible");
       }

        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(()-> new RuntimeException(friendEmail + " introuvable"));

        //Check to see if the connection already exists
        boolean connectionExists = user.getConnections().stream()
                                       .anyMatch(connection -> connection.getId() == friend.getId());

        if (connectionExists) {
            throw new RuntimeException("Contact déjà rajouté");
        }

        user.getConnections().add(friend);
        userRepository.save(user);
    }
}
