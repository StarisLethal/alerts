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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MedicalRecordController.class)
@AutoConfigureMockMvc
public class MedicalRecordControllerTests {

    @Autowired
    MedicalRecordController medicalRecordController;
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
       Iterable<MedicalRecord> listTestMedicalRecord = Arrays.asList(
                new MedicalRecord("Test FirstName1", "Test LastName1", "11/11/1111", Arrays.asList("Medication Test1", "Medication Test2"), Arrays.asList("Allergies Test1", "Allergies Test2")),
                new MedicalRecord("Test FirstName2", "Test LastName2", "22/02/1112", Arrays.asList("Medication Test3", "Medication Test4"), Arrays.asList("Allergies Test3", "Allergies Test4")));

        when(medicalRecordService.list()).thenReturn(listTestMedicalRecord);

        mockMvc.perform(get("/medicalrecords"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

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
    public void testPostMedicalRecord() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();

        MedicalRecord medicalRecord = new MedicalRecord("Test FirstName1", "Test LastName1", "11/11/1111", Arrays.asList("Medication Test1", "Medication Test2"), Arrays.asList("Allergies Test1", "Allergies Test2"));
        List<MedicalRecord> listTestMedicalRecord = List.of(medicalRecord);

        when(medicalRecordService.addMedicalRecord(medicalRecord)).thenReturn(listTestMedicalRecord);

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].birthdate").value("11/11/1111"))
                .andExpect(jsonPath("$[0].medications[0]").value("Medication Test1"))
                .andExpect(jsonPath("$[0].medications[1]").value("Medication Test2"))
                .andExpect(jsonPath("$[0].allergies[0]").value("Allergies Test1"))
                .andExpect(jsonPath("$[0].allergies[1]").value("Allergies Test2"));
    }

    @Test
    public void testPutMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord("Test FirstName1", "Test LastName1", "11/11/1111", Arrays.asList("Medication Test1", "Medication Test2"), Arrays.asList("Allergies Test1", "Allergies Test2"));

        String firstName = "Test FirstName1";
        String lastName = "Test LastName1";
        when(medicalRecordService.editMedicalRecords(firstName, lastName, medicalRecord)).thenReturn(true);

        ResponseEntity<Void> response = medicalRecordController.updateMedicalRecord(firstName, lastName, medicalRecord);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteMedicalRecord() throws Exception {
        String firstName = "testname";
        String lastName = "testlastname";
        MedicalRecord medicalRecord = new MedicalRecord();
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

