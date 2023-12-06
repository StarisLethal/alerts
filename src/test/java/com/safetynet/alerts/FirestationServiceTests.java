package com.safetynet.alerts;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repositories.FirestationRepositories;
import com.safetynet.alerts.service.FirestationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        Firestation firestation1 = new Firestation(1L, "TestAddress1", "TestStation1");
        Firestation firestation2 = new Firestation(2L, "TestAddress2", "TestStation2");
        List<Firestation> firestationList = Arrays.asList(firestation1, firestation2);

        when(firestationRepositories.findAll()).thenReturn(firestationList);

        Iterable<Firestation> result = firestationService.list();

        assertThat(result).containsExactlyInAnyOrderElementsOf(firestationList);
    }

    @Test
    void testGetFirestationById() {
        Long id = 1L;
        Firestation firestation = new Firestation();
        firestation.setId(id);
        firestation.setStation("TestStation");

        when(firestationRepositories.findById(id)).thenReturn(Optional.of(firestation));

        Optional<Firestation> result = firestationService.get(id);

        assertEquals(firestation, result.orElse(null));
    }

    @Test
    void testSaveFirestation() {
        Firestation firestation = new Firestation();
        firestation.setStation("1");

        when(firestationRepositories.save(firestation)).thenReturn(firestation);

        Firestation result = firestationService.save(firestation);

        assertEquals(firestation, result);
    }

    @Test
    void testSaveFirestations() {
        List<Firestation> firestations = new ArrayList<>();
        Firestation firestation1 = new Firestation();
        firestation1.setStation("1");
        Firestation firestation2 = new Firestation();
        firestation2.setStation("2");
        firestations.add(firestation1);
        firestations.add(firestation2);

        when(firestationRepositories.saveAll(firestations)).thenReturn(firestations);

        Iterable<Firestation> result = firestationService.save(firestations);

        assertEquals(firestations, result);
    }

    @Test
    void testGetFireStationByAddress() {
        List<Object[]> firestationTest = new ArrayList<>();
        Object[] test1 = {"Marneus", "Calgar"};
        Object[] test2 = {"Sol", "Pyro"};
        firestationTest.add(test1);
        firestationTest.add(test2);

        when(firestationRepositories.findByAddressForFire("42 Solar System")).thenReturn(firestationTest);

        List<Object[]> result = firestationService.getFireStationByAddress("42 Solar System");

        assertEquals(firestationTest, result);
    }

    @Test
    void testGetAddressByFireStationNumber() {
        List<Object[]> firestationTest = new ArrayList<>();
        Object[] test1 = {"42 Solar System"};
        Object[] test2 = {"24 Sleepy dev St"};
        firestationTest.add(test1);
        firestationTest.add(test2);

        when(firestationRepositories.findByFireStationNumber("1")).thenReturn(firestationTest);

        List<Object[]> result = firestationService.getAddressByFireStationNumber("1");

        assertEquals(firestationTest, result);
    }
}

