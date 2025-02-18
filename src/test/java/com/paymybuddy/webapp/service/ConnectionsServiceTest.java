package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConnectionsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ConnectionsService connectionsService;

    private User testUser;
    private User friendUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("user@test.com");
        testUser.setConnections(new ArrayList<>());

        friendUser = new User();
        friendUser.setId(2);
        friendUser.setEmail("friend@test.com");
    }

    @Test
    void getConnectionsByUserTest() {

        List<User> connections = Arrays.asList(
                friendUser,
                new User(),
                new User()
        );
        testUser.setConnections(connections);

        Map<String, Object> result = connectionsService.getConnectionsByUser(testUser, 0, 2);

        assertNotNull(result);
        assertEquals(2, ((List<User>) result.get("connections")).size());
        assertEquals(0, result.get("currentPage"));
        assertEquals(2, result.get("totalPages"));
    }

    @Test
    void addConnectionTest() {

        when(userRepository.findByEmail("friend@test.com")).thenReturn(Optional.of(friendUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        connectionsService.addConnection(testUser, "friend@test.com");

        verify(userRepository).save(testUser);
        assertTrue(testUser.getConnections().contains(friendUser));
    }

    @Test
    void addConnectionSelfAddTest() {

        assertThrows(RuntimeException.class, () ->
                connectionsService.addConnection(testUser, testUser.getEmail())
        );
    }

    @Test
    void addConnectionErrorTest() {

        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                connectionsService.addConnection(testUser, "nonexistent@test.com")
        );
    }

    @Test
    void addConnectionAlreadyExistTest() {
        testUser.getConnections().add(friendUser);
        when(userRepository.findByEmail("friend@test.com")).thenReturn(Optional.of(friendUser));

        assertThrows(RuntimeException.class, () ->
                connectionsService.addConnection(testUser, "friend@test.com")
        );
    }
}