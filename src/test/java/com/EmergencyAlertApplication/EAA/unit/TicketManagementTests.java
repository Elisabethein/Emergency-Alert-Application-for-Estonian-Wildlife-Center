package com.EmergencyAlertApplication.EAA.unit;

import com.EmergencyAlertApplication.EAA.Controllers.TicketController;
import com.EmergencyAlertApplication.EAA.DTOs.*;
import com.EmergencyAlertApplication.EAA.Entities.*;
import com.EmergencyAlertApplication.EAA.Services.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.text.SimpleDateFormat;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TicketManagementTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private TicketService ticketService;

    @Mock
    private StatusService statusService;

    @Mock
    private RegionService regionService;

    @Mock
    private ResolutionService resolutionService;

    @Mock
    private SpeciesService speciesService;

    @Mock
    private AnimalTagService animalTagService;

    @Mock
    private TicketToAnimalTagService ticketToAnimalTagService;

    @Mock
    private TicketToUserService ticketToUserService;

    @InjectMocks
    private TicketController ticketController;


    private String token;
    private String ticketId;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final AlertDTO validAlert = new AlertDTO(
            "Injured animal",
            "Fox",
            "Forest near Tallinn",
            "Enter through the north gate",
            "John",
            "Doe",
            "+3721234567",
            "john.doe@example.com",
            true,
            null, // Assuming no pictures for simplicity
            59.437,
            24.7536,
            "johnFacebook"
    );

    @BeforeEach
    @Transactional
    void loginAndCreateTicket() throws Exception {
        // Login to get the token for subsequent requests
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "jane.doe@example.com");
        loginRequest.put("password", "password123");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        token = jsonNode.get("token").asText();

        // Now create a ticket to use in the test
        String jsonTicket = objectMapper.writeValueAsString(validAlert); // Assuming validAlert is predefined
        MvcResult ticketResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/tickets/add")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTicket))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String ticketResponseJson = ticketResult.getResponse().getContentAsString();
        Ticket createdTicket = objectMapper.readValue(ticketResponseJson, Ticket.class);
        ticketId = String.valueOf(createdTicket.getId()); // Store the created ticket's ID
    }

    @Test
    @Transactional
    void testFetchAllTickets() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tickets/all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").isNotEmpty());
    }

    @Test
    @Transactional
    void testDeleteTicket() throws Exception {
        // Assuming ticketId is already set with a valid ticket ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tickets/delete/" + ticketId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Ticket deleted successfully"));
    }

//    @Test
//    @Transactional
//    void testEditTicket() throws Exception {
//        // Create the EditTicketDTO object with the necessary data
//        EditTicketDTO editTicketDTO = getEditTicketDTO();
//
//        // Convert the DTO to JSON
//        String json = objectMapper.writeValueAsString(editTicketDTO);
//
//        // Perform the PUT request to the /api/tickets/edit/{id} endpoint
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/tickets/edit/" + ticketId) // `ticketId` should be the id of an existing ticket
//                        .header("Authorization", "Bearer " + token) // Add the Authorization header with the Bearer token
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)) // Set the content as the JSON object created
//                .andExpect(MockMvcResultMatchers.status().isOk()) // Expect status 200 OK
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)) // Expect JSON response
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Lõpetatud")) // Check that the status is updated
//                .andExpect(MockMvcResultMatchers.jsonPath("$.describedAnimal").value("Punahirv")) // Check that the described animal is updated
//                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Injured lion in need of treatment")); // Check that the description is updated
//    }

//    @Test
//    @Transactional
//    void testEditTicket2() throws Exception {
//        // Mock a valid UUID
//        String ticketId = "ba2b345a-2c42-4c9a-b56f-cd0bc3a0b05f";
//
//        // Prepare a valid EditTicketDTO JSON payload
//        String payload = """
//    {
//        "id": "ba2b345a-2c42-4c9a-b56f-cd0bc3a0b05f",
//        "describedAnimal": "Lion",
//        "description": "Injured lion in need of treatment",
//        "directions": "North of the city center",
//        "history": "First report received.",
//        "latitude": 59.437,
//        "longitude": 24.7535,
//        "location": "Tallinn",
//        "reporterName": "John Doe",
//        "reporterPhone": "123456789",
//        "reporterEmail": "reporter@example.com",
//        "notificationSource": "Public report",
//        "region": "Harju",
//        "species": "Punahirv",
//        "upperSpecies": "Suuruluk",
//        "status": "Lõpetatud",
//        "resolution": "Vabastatud",
//        "tags": ["tag1", "tag2"],
//        "transportPossibility": false,
//        "reporterCanWait": true,
//        "hospital": true
//    }
//    """;
//
//        // Perform the PUT request
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/tickets/edit/" + ticketId)
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(payload))
//                .andExpect(status().isOk()) // Expect 200 OK
//                .andReturn();
//
//        // Log the response for debugging
//        String responseContent = result.getResponse().getContentAsString();
//        System.out.println("Response: " + responseContent);
//    }

    private EditTicketDTO getEditTicketDTO() {
        // Create and return an EditTicketDTO with the necessary data
        EditTicketDTO editTicketDTO = new EditTicketDTO();
        editTicketDTO.setId(ticketId);
        editTicketDTO.setStatus("Lõpetatud"); // Example status update
        editTicketDTO.setDescribedAnimal("Lion"); // Example described animal
        editTicketDTO.setDescription("Injured lion in need of treatment"); // Example description
        editTicketDTO.setDirections("North of the city center");
        editTicketDTO.setHistory("First report received.");
        editTicketDTO.setLatitude(59.4370); // Example latitude
        editTicketDTO.setLongitude(24.7535); // Example longitude
        editTicketDTO.setLocation("Tallinn"); // Example location
        editTicketDTO.setNotificationSource("Public report");
        editTicketDTO.setHospital(true); // Example hospital status
        editTicketDTO.setReporterCanWait(true);
        editTicketDTO.setReporterEmail("reporter@example.com");
        editTicketDTO.setReporterName("John Doe");
        editTicketDTO.setReporterPhone("123456789");

        // Set region, resolution, species, and other necessary fields for testing
        editTicketDTO.setResolution("Vabastatud");
        editTicketDTO.setSpecies("Punahirv"); // Example species
        editTicketDTO.setUpperSpecies("Suuruluk");

        // Tags for the ticket
        editTicketDTO.setTags(List.of("tag1", "tag2"));

        return editTicketDTO;
    }
}
