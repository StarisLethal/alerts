package com.safetynet.alerts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controller.FirestationController;
import com.safetynet.alerts.controller.MedicalRecordController;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.FirestationRepositories;
import com.safetynet.alerts.repositories.MedicalRecordRepositories;
import com.safetynet.alerts.service.FirestationService;
import com.safetynet.alerts.service.MedicalRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
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

    @MockBean
    FirestationService firestationService;
    @MockBean
    FirestationRepositories firestationRepositories;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getFirestationsTest() throws Exception {

        List<Firestation> listTestFirestation= Arrays.asList(
                new Firestation(1L,"Test Address1","Test Firestation1"),
                new Firestation(2L,"Test Address2","Test Firestation2"));

                when(firestationService.list()).thenReturn(listTestFirestation);

                mockMvc.perform(get("/firestations"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].id").value("1"))
                        .andExpect(jsonPath("$[0].address").value("Test Address1"))
                        .andExpect(jsonPath("$[0].station").value("Test Firestation1"))
                        .andExpect(jsonPath("$[1].id").value("2"))
                        .andExpect(jsonPath("$[1].address").value("Test Address2"))
                        .andExpect(jsonPath("$[1].station").value("Test Firestation2"));

    }

    @Test
    public void testGetFirestation() throws Exception {
        Firestation firestation = new Firestation(1L,"Test Address1","Test Firestation1");

        Long id = 1L;
        when(firestationService.get(id)).thenReturn(Optional.of(firestation));
        mockMvc.perform(get("/firestation/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Test Address1"))
                .andExpect(jsonPath("$.station").value("Test Firestation1"));
    }

    @Test
    public void testPostFirestation() throws Exception {
        Firestation firestation = new Firestation(1L,"Test Address1","Test Firestation1");
        final ObjectMapper objectMapper = new ObjectMapper();

        when(firestationService.save(any(Firestation.class))).thenReturn(firestation);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address").value("Test Address1"))
                .andExpect(jsonPath("$.station").value("Test Firestation1"));
    }

    @Test
    public void testPutFirestation() throws Exception {
        Firestation firestationSource = new Firestation(1L, "Test Address1", "Test Firestation1");
        Firestation firestationModified = new Firestation(1L, "Test Address1", "Test Firestation1");

        Long id = 1L;

        when(firestationRepositories.findById(id)).thenReturn(Optional.of(firestationSource));

        ResultActions response = mockMvc.perform(put("/firestation/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firestationModified)));

        response.andExpect(status().isOk());

        assertEquals(firestationModified.getAddress(), firestationSource.getAddress());
        assertEquals(firestationModified.getStation(), firestationSource.getStation());
    }

    @Test
    public void testDeleteFirestationByAddress() throws Exception {
        Firestation firestation = new Firestation(1L, "Test Address1", "Test Firestation1");
        String address = "Test Address1";

        mockMvc.perform(delete("/deleteFirestationByAddress")
                        .param("address", "Test Address1")

                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"deleted\":true}"));
    }

    @Test
    public void testDeleteFirestationByStation() throws Exception {
        Firestation firestation = new Firestation(1L, "Test Address1", "Test Firestation1");
        String address = "Test Address1";

        mockMvc.perform(delete("/deleteFirestationByStation")
                        .param("station", "Test Firestation1")

                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"deleted\":true}"));
    }
}
