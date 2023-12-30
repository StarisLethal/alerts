package com.safetynet.alerts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controller.FirestationController;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repositories.FirestationRepositories;
import com.safetynet.alerts.service.FirestationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FirestationController.class)
@AutoConfigureMockMvc
public class FirestationControllersTests {

    @Autowired
    private FirestationController firestationController;
    @MockBean
    FirestationService firestationService;
    @MockBean
    FirestationRepositories firestationRepositories;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getFirestationsTest() throws Exception {
        List<Firestation> firestationList = new ArrayList<>();
        firestationList.add(new Firestation("Test Address1", "Test Firestation1"));
        firestationList.add(new Firestation("Test Address2", "Test Firestation2"));

        when(firestationService.list()).thenReturn(firestationList);

        mockMvc.perform(get("/firestations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].address").value("Test Address1"))
                .andExpect(jsonPath("$[0].station").value("Test Firestation1"))
                .andExpect(jsonPath("$[1].address").value("Test Address2"))
                .andExpect(jsonPath("$[1].station").value("Test Firestation2"));
    }

    @Test
    public void testPostFirestation() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        Firestation firestation = new Firestation("Test Address1", "Test Firestation1");
        List<Firestation> firestationList = List.of(firestation);

        when(firestationService.addFirestation(any(Firestation.class))).thenReturn(firestationList);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].address").value("Test Address1"))
                .andExpect(jsonPath("$[0].station").value("Test Firestation1"));
    }

    @Test
    public void testUpdateFirestationSuccess() {
        String address = "TestAddress";
        String station = "TestStation";
        Firestation updatedFirestation = new Firestation(address, station);
        when(firestationService.editFirestationNumber(address, station)).thenReturn(Optional.of(updatedFirestation));

        ResponseEntity<Firestation> response = firestationController.updateFirestation(address, station);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteFirestationByAddress() throws Exception {

        mockMvc.perform(delete("/firestation")
                        .param("address", "Test Address1")

                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"deleted\":true}"));
    }

    @Test
    public void testDeleteFirestationByStation() throws Exception {

        mockMvc.perform(delete("/firestation")
                        .param("station", "Test Firestation1")

                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"deleted\":true}"));
    }
}
