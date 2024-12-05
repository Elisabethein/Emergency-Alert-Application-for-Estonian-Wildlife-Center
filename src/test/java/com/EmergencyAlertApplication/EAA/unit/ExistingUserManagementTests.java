package com.EmergencyAlertApplication.EAA.unit;

import com.EmergencyAlertApplication.EAA.DTOs.EditUserDTO;
import com.EmergencyAlertApplication.EAA.DTOs.SpeciesDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ExistingUserManagementTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private String token;
    private String id;
    @BeforeEach
    void login() throws Exception {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "alice.johnson@example.com");
        loginRequest.put("password", "password123");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        token = jsonNode.get("token").asText();
        id = jsonNode.get("user").get("id").asText();
    }

    @Test
    void testFetchAllExistingUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").isNotEmpty());
    }
    @Test
    void testFetchUserRoles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + id + "/roles")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").isNotEmpty());
    }
    @Test
    void testFetchUserTags() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + id + "/tags")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").isNotEmpty());
    }
    @Test
    void testFetchUserRegions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + id + "/regions")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").isNotEmpty());
    }
    @Test
    void testFetchUserSpecies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + id + "/species")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").isNotEmpty());
    }
    @Test
    @Transactional
    void testEditUser() throws Exception {
        EditUserDTO editUserDTO = getEditUserDTO();

        String json = objectMapper.writeValueAsString(editUserDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/edit/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Edited Alice"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(" Edited Johnson"));
    }

    @Test
    @Transactional
    void testDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/delete/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User deleted successfully"));
    }

    private EditUserDTO getEditUserDTO() {
        EditUserDTO editUserDTO = new EditUserDTO();
        editUserDTO.setId(id);
        editUserDTO.setFirstName("Edited Alice");
        editUserDTO.setLastName(" Edited Johnson");
        editUserDTO.setEmail("alice.johnson@example.com");
        editUserDTO.setPhoneNr("1234567890");
        editUserDTO.setCounty("Example County");
        editUserDTO.setCity("Example City");
        editUserDTO.setStreetName("Main St");
        editUserDTO.setStreetNr("123");
        editUserDTO.setPostalCode("12345");
        editUserDTO.setTags(List.of("tag1", "tag2"));
        editUserDTO.setRegions(List.of("region1", "region2"));
        editUserDTO.setRoles(List.of("ROLE_USER", "ROLE_ADMIN"));
        editUserDTO.setSpecies(List.of(new SpeciesDTO()));
        return editUserDTO;
    }


}
