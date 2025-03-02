package com.paymybuddy.webapp.e2e;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.TransactionRepository;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.service.SignUpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PayMyBuddyE2E {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private User testUser;
    private User friendUser;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        userRepository.deleteAll();

        // Créer l'utilisateur principal
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("John Doe");
        userDTO.setEmail("john@test.com");
        userDTO.setPassword("Password123!");
        testUser = signUpService.registerNewUser(userDTO);

        // Créer un ami
        UserDTO friendDTO = new UserDTO();
        friendDTO.setUsername("Jane Smith");
        friendDTO.setEmail("jane@test.com");
        friendDTO.setPassword("Password123!");
        friendUser = signUpService.registerNewUser(friendDTO);
    }

    @Test
    void completeUserJourneyTest() throws Exception {
        // 1. Vérifier l'accès à la page de transfert
        mockMvc.perform(get("/transfert")
                       .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
               .andExpect(status().isOk())
               .andExpect(view().name("transfert"))
               .andExpect(model().attributeExists("balance"));

        // 2. Effectuer un dépôt initial
        mockMvc.perform(post("/transfert/add")
                       .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                       .param("type", "VIREMENT ENTRANT")
                       .param("description", "Dépôt initial")
                       .param("amount", "1000.0"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/transfert"));

        // 3. Ajouter une relation
        mockMvc.perform(post("/connections")
                       .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                       .param("email", friendUser.getEmail()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/connections"));

        // 4. Effectuer un transfert
        mockMvc.perform(post("/transfert/add")
                       .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                       .param("type", "TRANSFERT")
                       .param("description", "Test transfer")
                       .param("amount", "100.0")
                       .param("receiverId", String.valueOf(friendUser.getId())))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/transfert"));

        // 5. Effectuer un virement sortant
        mockMvc.perform(post("/transfert/add")
                       .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                       .param("type", "VIREMENT SORTANT")
                       .param("description", "VIREMENT SORTANT")
                       .param("amount", "200.0"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/transfert"));

        // 6. Vérifier le solde final
        mockMvc.perform(get("/transfert")
                       .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
               .andExpect(status().isOk())
               .andExpect(model().attribute("balance", 698.5));
    }
}