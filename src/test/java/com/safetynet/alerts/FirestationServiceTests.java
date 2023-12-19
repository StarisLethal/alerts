package com.safetynet.alerts;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repositories.FirestationRepositories;
import com.safetynet.alerts.service.FirestationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class FirestationServiceTests {

    @MockBean
    private FirestationRepositories firestationRepositories;
    @Autowired
    private FirestationService firestationService;

    @Test
    void testListFirestations() {
        List<Firestation> firestationList = new ArrayList<>();
        firestationList.add(new Firestation("Test Address1", "Test Firestation1"));
        firestationList.add(new Firestation("Test Address2", "Test Firestation2"));

        when(firestationService.list()).thenReturn(firestationList);

        Iterable<Firestation> result = firestationService.list();

        assertThat(result).containsExactlyInAnyOrderElementsOf(firestationList);
    }

    @Test
    void testSaveFirestation() {
        Firestation firestation = new Firestation("Test Address1", "Test Firestation1");
        List<Firestation> firestationList = new ArrayList<>(Collections.singleton(firestation));

        when(firestationService.addFirestation(firestation)).thenReturn(firestationList);

        List<Firestation> result = firestationService.addFirestation(firestation);

        assertEquals(firestationList, result);
    }

    @Test
    public void testEditFirestationNumber() {
        Firestation firestation = new Firestation("Test Address1", "Test Firestation1");
        List<Firestation> firestationList = new ArrayList<>(Collections.singleton(firestation));
        String currentAddress = "Test Address1";
        String newStationNumber = "Test NewStation";

        when(firestationRepositories.getFirestations()).thenReturn(firestationList);

        Firestation updatedFirestation = new Firestation("Test Address1", "Test NewStation");

        Optional<Firestation> result = firestationService.editFirestationNumber(currentAddress, newStationNumber);

        assertEquals(newStationNumber, result.get().getStation());
    }

    @Test
    public void testDeleteFirestationByStation() {
        Firestation firestation1 = new  Firestation("Test Address1", "Test Firestation1");
        Firestation firestation2 = new Firestation("Test Address2", "Test Firestation2");
        Firestation firestation3 = new Firestation("Test Address3", "Test Firestation3");
        List<Firestation> firestationList = Arrays.asList(firestation1,firestation2,firestation3);
        String firestationNumber = "Test Firestation1";

        when(firestationRepositories.getFirestations()).thenReturn(firestationList);

        List<Firestation> updatedFirestations = firestationService.deleteFirestationByStation(firestationNumber);

        assertEquals(2, updatedFirestations.size());
    }

    @Test
    public void testdeleteFirestationByAddress() {
        Firestation firestation1 = new  Firestation("Test Address1", "Test Firestation1");
        Firestation firestation2 = new Firestation("Test Address2", "Test Firestation2");
        Firestation firestation3 = new Firestation("Test Address3", "Test Firestation3");
        List<Firestation> firestationList = Arrays.asList(firestation1,firestation2,firestation3);
        String firestationAddress = "Test Address2";

        when(firestationRepositories.getFirestations()).thenReturn(firestationList);

        List<Firestation> updatedFirestations = firestationService.deleteFirestationByAddress(firestationAddress);

        System.out.println("Updated Firestations: " + updatedFirestations);

        assertEquals(2, updatedFirestations.size());
    }

    @Test
    public void testGetFireStationByAddress() {
        Firestation firestation1 = new  Firestation("Test Address1", "Test Firestation1");
        Firestation firestation2 = new Firestation("Test Address2", "Test Firestation2");
        Firestation firestation3 = new Firestation("Test Address3", "Test Firestation3");
        List<Firestation> firestationList = Arrays.asList(firestation1,firestation2,firestation3);
        String address = "Test Address1";

        when(firestationRepositories.getFirestations()).thenReturn(firestationList);

        String station = firestationService.getFireStationByAddress(address);

        assertEquals("Test Firestation1", station);
    }

    @Test
    void testGetAddressByFireStationNumber() {
        Firestation firestation1 = new  Firestation("Test Address1", "Test Firestation1");
        Firestation firestation2 = new Firestation("Test Address2", "Test Firestation2");
        Firestation firestation3 = new Firestation("Test Address3", "Test Firestation3");
        List<Firestation> firestationList = Arrays.asList(firestation1,firestation2,firestation3);
        String firestationNumber = "Test Firestation1";

        when(firestationRepositories.getFirestations()).thenReturn(firestationList);

        List<String> address = firestationService.getAddressByFireStationNumber(firestationNumber);

        assertTrue(address.contains("Test Address1"));
    }
}

