package com.safetynet.alerts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controller.MedicalRecordController;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repositories.MedicalRecordRepositories;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MedicalRecordController.class)
@AutoConfigureMockMvc
public class MedicalRecordControllerTests {

    @MockBean
    MedicalRecordService medicalRecordService;
    @MockBean
    MedicalRecordRepositories medicalRecordRepositories;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetMedicalRecords() throws Exception {
        List<MedicalRecord> listTestMedicalRecord = Arrays.asList(
                new MedicalRecord(1L, "Test FirstName1", "Test LastName1", "11/11/1111", Arrays.asList("Medication Test1", "Medication Test2"), Arrays.asList("Allergies Test1", "Allergies Test2")),
                new MedicalRecord(2L, "Test FirstName2", "Test LastName2", "22/02/1112", Arrays.asList("Medication Test3", "Medication Test4"), Arrays.asList("Allergies Test3", "Allergies Test4")));

        when(medicalRecordService.list()).thenReturn(listTestMedicalRecord);

        mockMvc.perform(get("/medicalrecord"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].firstName").value("Test FirstName1"))
                .andExpect(jsonPath("$[0].lastName").value("Test LastName1"))
                .andExpect(jsonPath("$[0].birthdate").value("11/11/1111"))
                .andExpect(jsonPath("$[0].medications[0]").value("Medication Test1"))
                .andExpect(jsonPath("$[0].medications[1]").value("Medication Test2"))
                .andExpect(jsonPath("$[0].allergies[0]").value("Allergies Test1"))
                .andExpect(jsonPath("$[0].allergies[1]").value("Allergies Test2"))
                .andExpect(jsonPath("$[1].firstName").value("Test FirstName2"))
                .andExpect(jsonPath("$[1].lastName").value("Test LastName2"))
                .andExpect(jsonPath("$[1].birthdate").value("22/02/1112"))
                .andExpect(jsonPath("$[1].medications[0]").value("Medication Test3"))
                .andExpect(jsonPath("$[1].medications[1]").value("Medication Test4"))
                .andExpect(jsonPath("$[1].allergies[0]").value("Allergies Test3"))
                .andExpect(jsonPath("$[1].allergies[1]").value("Allergies Test4"));
    }

    @Test
    public void testGetMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord(1L, "Test FirstName1", "Test LastName1", "11/11/1111", Arrays.asList("Medication Test1", "Medication Test2"), Arrays.asList("Allergies Test1", "Allergies Test2"));
        Long id = 1L;
        when(medicalRecordService.get(id)).thenReturn(Optional.of(medicalRecord));

        mockMvc.perform(get("/medicalRecord/{id}", id))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.firstName").value("Test FirstName1"))
                .andExpect(jsonPath("$.lastName").value("Test LastName1"))
                .andExpect(jsonPath("$.birthdate").value("11/11/1111"))
                .andExpect(jsonPath("$.medications[0]").value("Medication Test1"))
                .andExpect(jsonPath("$.medications[1]").value("Medication Test2"))
                .andExpect(jsonPath("$.allergies[0]").value("Allergies Test1"))
                .andExpect(jsonPath("$.allergies[1]").value("Allergies Test2"));
    }

    @Test
    public void testPostMedicalRecord() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();

        MedicalRecord medicalRecord = new MedicalRecord(1L, "Test FirstName1", "Test LastName1", "11/11/1111", Arrays.asList("Medication Test1", "Medication Test2"), Arrays.asList("Allergies Test1", "Allergies Test2"));



        when(medicalRecordRepositories.save(medicalRecord)).thenReturn(medicalRecord);


        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.birthdate").value("11/11/1111"))
                .andExpect(jsonPath("$.medications[0]").value("Medication Test1"))
                .andExpect(jsonPath("$.medications[1]").value("Medication Test2"))
                .andExpect(jsonPath("$.allergies[0]").value("Allergies Test1"))
                .andExpect(jsonPath("$.allergies[1]").value("Allergies Test2"));
    }

    @Test
    public void testPutMedicalRecord() throws Exception {

        MedicalRecord medicalRecordOrigin = new MedicalRecord();
        medicalRecordOrigin.setId(1L);
        medicalRecordOrigin.setFirstName("Test FirstName");
        medicalRecordOrigin.setLastName("Test LastName");
        medicalRecordOrigin.setBirthdate("11/11/1111");
        medicalRecordOrigin.setMedications(Arrays.asList("Medication Test1", "Medication Test2"));
        medicalRecordOrigin.setAllergies(Arrays.asList("Allergies Test1", "Allergies Test2"));

        MedicalRecord medicalRecordModified = new MedicalRecord();
        medicalRecordModified.setBirthdate("11/11/1112");
        medicalRecordModified.setMedications(Arrays.asList("Medication Test3", "Medication Test4"));
        medicalRecordModified.setAllergies(Arrays.asList("Allergies Test3", "Allergies Test4"));

        Long id = 1L;


        when(medicalRecordRepositories.findById(id)).thenReturn(Optional.of(medicalRecordOrigin));

        ResultActions response = mockMvc.perform(put("/medicalRecord/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicalRecordModified)));

        response.andExpect(status().isOk());

        assertEquals(medicalRecordModified.getBirthdate(), medicalRecordOrigin.getBirthdate());
        assertEquals(medicalRecordModified.getMedications(), medicalRecordOrigin.getMedications());
        assertEquals(medicalRecordModified.getAllergies(), medicalRecordOrigin.getAllergies());
    }

    @Test
    public void testDeleteMedicalRecord() throws Exception {
        String firstName = "testname";
        String lastName = "testlastname";
        long id = 1L;
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(1L);
        medicalRecord.setFirstName(firstName);
        medicalRecord.setLastName(lastName);

        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", firstName)
                        .param("lastName", lastName)


                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"deleted\":true}"));
    }

}

