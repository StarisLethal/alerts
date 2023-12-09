package com.safetynet.alerts;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.MedicalRecordRepositories;
import com.safetynet.alerts.service.MedicalRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordTests {

    @MockBean
    MedicalRecordRepositories medicalRecordRepositories;
    @Autowired
    MedicalRecordService medicalRecordService;

    @Test
    void testListPersons() {
        List<MedicalRecord> listTestMedicalRecord = Arrays.asList(
                new MedicalRecord(1L, "Test FirstName1", "Test LastName1", "11/11/1111", Arrays.asList("Medication Test1", "Medication Test2"), Arrays.asList("Allergies Test1", "Allergies Test2")),
                new MedicalRecord(2L, "Test FirstName2", "Test LastName2", "22/02/1112", Arrays.asList("Medication Test3", "Medication Test4"), Arrays.asList("Allergies Test3", "Allergies Test4")));

        when(medicalRecordRepositories.findAll()).thenReturn(listTestMedicalRecord);

        Iterable<MedicalRecord> result = medicalRecordService.list();

        assertEquals(listTestMedicalRecord, result);
    }

    @Test
    void testGetPersonById() {
        MedicalRecord medicalRecord = new MedicalRecord(1L, "Test FirstName1", "Test LastName1", "11/11/1111", Arrays.asList("Medication Test1", "Medication Test2"), Arrays.asList("Allergies Test1", "Allergies Test2"));
        Long id = 1L;

        when(medicalRecordRepositories.findById(id)).thenReturn(Optional.of(medicalRecord));

        Optional<MedicalRecord> result = medicalRecordService.get(id);

        assertEquals(medicalRecord, result.orElse(null));
    }

    @Test
    void testSavePerson() {
        MedicalRecord medicalRecord = new MedicalRecord();

        medicalRecord.setFirstName("Test FirstName");
        medicalRecord.setLastName("Test LastName");
        medicalRecord.setBirthdate("11/11/1111");
        medicalRecord.setMedications(Arrays.asList("Medication Test1", "Medication Test2"));
        medicalRecord.setAllergies(Arrays.asList("Allergies Test1", "Allergies Test2"));

        when(medicalRecordRepositories.save(medicalRecord)).thenReturn(medicalRecord);

        MedicalRecord result = medicalRecordService.save(medicalRecord);

        assertEquals(medicalRecord, result);
    }

    @Test
    void testSavePersons() {
        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(1L);
        medicalRecord.setFirstName("Test FirstName1");
        medicalRecord.setLastName("Test LastName1");
        medicalRecord.setBirthdate("11/11/1111");
        medicalRecord.setMedications(Arrays.asList("Medication Test1", "Medication Test2"));
        medicalRecord.setAllergies(Arrays.asList("Allergies Test1", "Allergies Test2"));

        MedicalRecord medicalRecord1 = new MedicalRecord();
        medicalRecord1.setId(1L);
        medicalRecord1.setFirstName("Test FirstName");
        medicalRecord1.setLastName("Test LastName");
        medicalRecord1.setBirthdate("11/11/1222");
        medicalRecord1.setMedications(Arrays.asList("Medication Test3", "Medication Test4"));
        medicalRecord1.setAllergies(Arrays.asList("Allergies Test3", "Allergies Test4"));

        medicalRecordList.add(medicalRecord);
        medicalRecordList.add(medicalRecord1);

        when(medicalRecordRepositories.saveAll(medicalRecordList)).thenReturn(medicalRecordList);

        Iterable<MedicalRecord> result = medicalRecordService.save(medicalRecordList);

        assertEquals(medicalRecordList, result);
    }

    @Test
    void testGetMedicalRecordByCompleteName() {
        String firstName = "Marneus";
        String lastName = "Calgar";
        List<Object[]> expectedMedicalRecords = Collections.singletonList(new Object[]{"Aspirin", "100mg"});

        when(medicalRecordRepositories.findMedicalRecordByCompleteName(firstName, lastName)).thenReturn(expectedMedicalRecords);

        List<Object[]> result = medicalRecordService.getMedicalRecordByCompleteName(firstName, lastName);

        assertEquals(expectedMedicalRecords, result);
    }

    @Test
    void testGetAgeByCompleteName() {
        String firstName = "Marneus";
        String lastName = "Calgar";
        String birthday = "06/15/1986";
        LocalDate fakeCurrentDate = LocalDate.parse("01/01/2023", DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        when(medicalRecordRepositories.findBirthDayByCompleteName(firstName, lastName)).thenReturn(birthday);

        int result = medicalRecordService.getAgeByCompleteName(firstName, lastName);

        int expectedAge = fakeCurrentDate.getYear() - LocalDate.parse(birthday, DateTimeFormatter.ofPattern("MM/dd/yyyy")).getYear();

        assertEquals(expectedAge, result);
    }

    @Test
    void testChildOrNot(){
        String birthdate = "09/06/2017";
        String birthdateAdult = "02/28/1928";

        when(medicalRecordRepositories.findBirthDayByCompleteName("Marneus", "Calgar")).thenReturn(birthdate);
        when(medicalRecordRepositories.findBirthDayByCompleteName("Sol", "Pyro")).thenReturn(birthdateAdult);

        boolean test = medicalRecordService.childOrNot("Marneus", "Calgar");
        assertTrue(test);

        boolean test2 = medicalRecordService.childOrNot("Sol", "Pyro");
        assertFalse(test2);
    }
}
