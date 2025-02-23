package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.service.ConnectionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ConnectionsServiceIT {

    @Autowired
    private ConnectionsService connectionsService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private User friendUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setUsername("TestUser");
        testUser.setConnections(new ArrayList<>());
        userRepository.save(testUser);

        friendUser = new User();
        friendUser.setEmail("friend@example.com");
        friendUser.setPassword("password");
        friendUser.setUsername("FriendUser");
        friendUser.setConnections(new ArrayList<>());
        userRepository.save(friendUser);
    }

    @Test
    void addConnectionTest() {
        connectionsService.addConnection(testUser, friendUser.getEmail());

        Map<String, Object> result = connectionsService.getConnectionsByUser(testUser, 0, 10);
        List<User> connections = (List<User>) result.get("connections");

        assertEquals(1, connections.size());
        assertEquals(friendUser.getEmail(), connections.get(0).getEmail());
    }

    @Test
    void addDuplicateConnectionTest() {
        connectionsService.addConnection(testUser, friendUser.getEmail());

        assertThrows(RuntimeException.class, () -> {
            connectionsService.addConnection(testUser, friendUser.getEmail());
        });
    }
    @Test
    void addConnectionErrorTest() {
        assertThrows(RuntimeException.class, () -> {
            connectionsService.addConnection(testUser, "nonexistent@example.com");
        });
    }
}