package com.safetynet.alerts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controller.PersonInfoController;
import com.safetynet.alerts.dto.PersonFireInfoDTO;
import com.safetynet.alerts.repositories.FirestationRepositories;
import com.safetynet.alerts.repositories.MedicalRecordRepositories;
import com.safetynet.alerts.repositories.PersonRepositories;
import com.safetynet.alerts.service.FirestationService;
import com.safetynet.alerts.service.MedicalRecordService;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
public class PersonInfoControllerTests {

    private PersonInfoController personInfoController;

    @MockBean
    MedicalRecordService medicalRecordService;
    @MockBean
    MedicalRecordRepositories medicalRecordRepositories;

    @MockBean
    PersonService personService;
    @MockBean
    PersonRepositories personRepositories;

    @MockBean
    FirestationService firestationService;
    @MockBean
    FirestationRepositories firestationRepositories;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAddressInfo() throws Exception {
        String firstName = "Test First Name";
        String lastName = "Test Last Name";

        List<Object[]> personList = Arrays.asList(new Object[]{"Test Address 1"}, new Object[]{"Test Address 2"});
        when(personService.getPersonByCompleteName(firstName, lastName)).thenReturn(personList);
        when(medicalRecordService.getAgeByCompleteName(firstName, lastName)).thenReturn(30);
        List<Object[]> medicalRecordList = Arrays.asList(new Object[]{"Medication 1", "Allergies 1"}, new Object[]{"Medication 2", "Allergies 2"});
        when(medicalRecordService.getMedicalRecordByCompleteName(firstName, lastName)).thenReturn(medicalRecordList);

        ResultActions resultActions = mockMvc.perform(get("/personInfo")
                .param("firstName", firstName)
                .param("lastName", lastName)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.personDetails[0]").value("Test Address 1"))
                .andExpect(jsonPath("$.personDetails[1]").value("Test Address 2"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.medicalRecords[0][0]").value("Medication 1"))
                .andExpect(jsonPath("$.medicalRecords[0][1]").value("Allergies 1"))
                .andExpect(jsonPath("$.medicalRecords[1][0]").value("Medication 2"))
                .andExpect(jsonPath("$.medicalRecords[1][1]").value("Allergies 2"));
    }

    @Test
    public void testGetFireInfo() throws Exception {
        String address = "TestAddress";


        List<Object[]> personList = Arrays.asList(new Object[][]{new Object[]{"TestFirstName", "TestLastName", "111-111-1111"}});
        String firestationNumber = "TestStation1";
        List<Object[]> medicalRecordList = Collections.emptyList();
        String firstName = "TestFirstName";
        String lastName = "TestLastName";

        PersonFireInfoDTO personFireInfoDTO = new PersonFireInfoDTO(personList, 30, new ArrayList<>());
        List<PersonFireInfoDTO> persons = Collections.singletonList(personFireInfoDTO);

        when(personService.getFLPByAddress(address)).thenReturn(personList);
        when(firestationService.getFireStationByAddress(address)).thenReturn(firestationNumber);
        when(medicalRecordService.getMedicalRecordByCompleteName(firstName, lastName)).thenReturn(medicalRecordList);
        when(medicalRecordService.getAgeByCompleteName(firstName, lastName)).thenReturn(30);


        mockMvc.perform(get("/fire")
                        .param("address", address))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personList[0].personDetails[0][0]").value("TestFirstName"))
                .andExpect(jsonPath("$.personList[0].personDetails[0][1]").value("TestLastName"))
                .andExpect(jsonPath("$.personList[0].personDetails[0][2]").value("111-111-1111"))
                .andExpect(jsonPath("$.personList[0].age").value(30))
                .andExpect(jsonPath("$.firestationNumber").value("TestStation1"));
    }

    @Test
    void testGetFloodInfo() throws Exception {
        List<String> firestationAddress1 = List.of("TestAddress1");
        List<String> firestationAddress2 = List.of("TestAddress2");
        List<Object[]> personList1 = Arrays.asList(new Object[][]{new Object[]{"TestFirstName1", "TestLastName1", "111-111-1111"}});
        List<Object[]> personList2 = Arrays.asList(new Object[][]{new Object[]{"TestFirstName2", "TestLastName2", "222-222-2222"}});

        when(firestationService.getAddressByFireStationNumber("1")).thenReturn(firestationAddress1);
        when(firestationService.getAddressByFireStationNumber("2")).thenReturn(firestationAddress2);
        when(personService.getFLPByAddress("TestAddress1")).thenReturn(personList1);
        when(personService.getFLPByAddress("TestAddress2")).thenReturn(personList2);
        when(medicalRecordService.getMedicalRecordByCompleteName("TestFirstName1", "TestLastName1")).thenReturn(Collections.emptyList());
        when(medicalRecordService.getMedicalRecordByCompleteName("TestFirstName2", "TestLastName2")).thenReturn(Collections.emptyList());
        when(medicalRecordService.getAgeByCompleteName("TestFirstName1", "TestLastName1")).thenReturn(30);
        when(medicalRecordService.getAgeByCompleteName("TestFirstName2", "TestLastName2")).thenReturn(25);

        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firestationAdressServed").value("TestAddress1"))
                .andExpect(jsonPath("$[0].personList[0].firstName").value("TestFirstName1"))
                .andExpect(jsonPath("$[0].personList[0].lastName").value("TestLastName1"))
                .andExpect(jsonPath("$[0].personList[0].phone").value("111-111-1111"))
                .andExpect(jsonPath("$[0].personList[0].age").value(30))
                .andExpect(jsonPath("$[1].firestationAdressServed").value("TestAddress2"))
                .andExpect(jsonPath("$[1].personList[0].firstName").value("TestFirstName2"))
                .andExpect(jsonPath("$[1].personList[0].lastName").value("TestLastName2"))
                .andExpect(jsonPath("$[1].personList[0].phone").value("222-222-2222"))
                .andExpect(jsonPath("$[1].personList[0].age").value(25));
    }

    @Test
    void testGetPhoneAlert() throws Exception {
        String firestationNumber = "1";
        String address = "TestAddress1";
        when(firestationService.getAddressByFireStationNumber("1")).thenReturn(Collections.singletonList(address));
        when(personService.getPhoneByAddress("TestAddress1")).thenReturn(Collections.singletonList(new Object[]{"111-111-1111"}));

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", firestationNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].phoneNumber[0]").value("111-111-1111"));
    }

    @Test
    void testGetChildAlert() throws Exception {
        String address = "TestAddress";
        String firstName = "TestFirstName";
        String lastName = "TestLastName";

        when(personService.getPersonByAddress(address)).thenReturn(Collections.singletonList(new Object[]{"TestFirstName", "TestLastName"}));
        when(medicalRecordService.childOrNot(firstName, lastName)).thenReturn(true);

        mockMvc.perform(get("/childAlert")
                        .param("address", address))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].childInfo[0].firstName").value("TestFirstName"))
                .andExpect(jsonPath("$[0].childInfo[0].lastName").value("TestLastName"))
                .andExpect(jsonPath("$[0].familyMembers").isArray())
                .andExpect(jsonPath("$[0].familyMembers").isEmpty());
    }

    @Test
    void testGetInfoByFirestation() throws Exception {
        String stationNumber = "TestStationNumber";
        List<String> addressList = Arrays.asList("TestAddress1", "TestAddress2");
        when(firestationService.getAddressByFireStationNumber(stationNumber)).thenReturn(addressList);
        when(personService.getPersonForFirestations("TestAddress1")).thenReturn(Collections.singletonList(new Object[]{"TestFirstName1", "TestLastName1", "TestAddress1", "TestPhone1"}));
        when(personService.getPersonForFirestations("TestAddress2")).thenReturn(Collections.singletonList(new Object[]{"TestFirstName2", "TestLastName2", "TestAddress2", "TestPhone2"}));
        when(medicalRecordService.childOrNot("TestFirstName1", "TestLastName1")).thenReturn(true);
        when(medicalRecordService.childOrNot("TestFirstName2", "TestLastName2")).thenReturn(false);

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", stationNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].personInfo[0].firstName").value("TestFirstName1"))
                .andExpect(jsonPath("$[0].personInfo[0].lastName").value("TestLastName1"))
                .andExpect(jsonPath("$[0].personInfo[0].address").value("TestAddress1"))
                .andExpect(jsonPath("$[0].personInfo[0].phone").value("TestPhone1"))
                .andExpect(jsonPath("$[0].numberOfAdult").value(1))
                .andExpect(jsonPath("$[0].numberOfChild").value(1));
    }
}