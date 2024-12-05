package com.EmergencyAlertApplication.EAA.unit;

import com.EmergencyAlertApplication.EAA.DTOs.ApplicationDTO;
import com.EmergencyAlertApplication.EAA.DTOs.ApplicationToTagDTO;
import com.EmergencyAlertApplication.EAA.Entities.Application;
import com.EmergencyAlertApplication.EAA.Entities.ApplicationToTag;
import com.EmergencyAlertApplication.EAA.Entities.Tag;
import com.EmergencyAlertApplication.EAA.Services.ApplicationToTagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ApplicationSubmitTests {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ApplicationToTagService applicationToTagService;

    private final ApplicationDTO validApplication = new ApplicationDTO("Test", "User", "123", "testUser@example.com", dateFormat.parse("2022-12-12"), "test", "123", "test", "Harjumaa", "123", "test", "test", "testPass");
    private final ApplicationDTO invalidEmailApplication = new ApplicationDTO("Test", "User", "123", "alice.johnson@example.com", dateFormat.parse("2022-12-12"), "test", "123", "test", "Harjumaa", "123", "test", "test", "testPass");

    public ApplicationSubmitTests() throws ParseException {
    }


    @Test
    void testSubmitApplication() throws Exception {
        String json = objectMapper.writeValueAsString(validApplication);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/applications/addApplication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ObjectMapper objectMapper = new ObjectMapper();
                    Application responseApplication = objectMapper.readValue(content, Application.class);

                    assertEquals(responseApplication.getIsAccepted(), false);
                    assertEquals(validApplication.getFirstName(), responseApplication.getFirstName());
                    assertEquals(validApplication.getLastName(), responseApplication.getLastName());
                    assertEquals(validApplication.getEmail(), responseApplication.getEmail());
                    assertEquals(validApplication.getPhone(), responseApplication.getPhoneNr());
                    assertEquals(validApplication.getDob(), responseApplication.getBirthDate());
                    assertEquals(validApplication.getStreet(), responseApplication.getStreetName());
                    assertEquals(validApplication.getStreetNr(), responseApplication.getStreetNr());
                    assertEquals(validApplication.getCity(), responseApplication.getCity());
                    assertEquals(validApplication.getRegion(), responseApplication.getCounty());
                    assertEquals(validApplication.getPostalCode(), responseApplication.getPostalCode());
                    assertEquals(validApplication.getReason(), responseApplication.getQuestion1());
                    assertEquals(validApplication.getExperience(), responseApplication.getQuestion2());
                });

    }

    @Test
    void testSubmitApplicationWithInvalidEmail() throws Exception {
        String json = objectMapper.writeValueAsString(invalidEmailApplication);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/applications/addApplication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testConnectApplicationToTags() throws Exception {
        Application application = new Application();
        Tag tag = new Tag();

        ApplicationToTagDTO dto = new ApplicationToTagDTO();
        dto.setApplication(application);
        dto.setTag(tag);

        String json = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/applicationToTags/addApplicationToTag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(applicationToTagService, Mockito.times(1)).save(any(ApplicationToTag.class));
    }


}
