package com.paymybuddy.webapp.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthentificationIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void userLoginTest() throws Exception {
        mockMvc.perform(formLogin("/login")
                       .user("email", "test@test.com")
                       .password("password123"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/transfert"))
               .andExpect(authenticated());
    }
    @Test
    public void userLoginError() throws Exception {
        mockMvc.perform(formLogin("/login").user("email","test@test.com").password("wrongpassword"))
               .andExpect(unauthenticated());
    }
}