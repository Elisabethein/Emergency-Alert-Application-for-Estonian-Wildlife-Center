package com.EmergencyAlertApplication.EAA.unit;

import com.EmergencyAlertApplication.EAA.DTOs.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasItem;

@SpringBootTest
@AutoConfigureMockMvc
public class LogInTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLoginUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("alice.johnson@example.com");
        loginRequest.setPassword("password123");

        String json = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email").value("alice.johnson@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testUserDoesNotExist() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("doesNotExist@example.com");
        loginRequest.setPassword("password123");

        String json = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string("Invalid email or password"))
                .andDo(MockMvcResultHandlers.print());
    }
}
