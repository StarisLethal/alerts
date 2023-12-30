package com.safetynet.alerts;

import com.safetynet.alerts.model.MedicalRecord;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordTests {

    @MockBean
    MedicalRecordRepositories medicalRecordRepositories;
    @Autowired
    MedicalRecordService medicalRecordService;

    @Test
    void testListMedicalRecord() {
        List<MedicalRecord> listTestMedicalRecord = Arrays.asList(
                new MedicalRecord("Test FirstName1", "Test LastName1", "11/11/1111", Arrays.asList("Medication Test1", "Medication Test2"), Arrays.asList("Allergies Test1", "Allergies Test2")),
                new MedicalRecord("Test FirstName2", "Test LastName2", "22/02/1112", Arrays.asList("Medication Test3", "Medication Test4"), Arrays.asList("Allergies Test3", "Allergies Test4")));

        when(medicalRecordService.list()).thenReturn(listTestMedicalRecord);

        Iterable<MedicalRecord> result = medicalRecordService.list();

        assertThat(result).containsExactlyInAnyOrderElementsOf(listTestMedicalRecord);
    }

    @Test
    void testSaveMedicalRecord() {
        MedicalRecord medicalRecord = new MedicalRecord("Test FirstName1", "Test LastName1", "11/11/1111", Arrays.asList("Medication Test1", "Medication Test2"), Arrays.asList("Allergies Test1", "Allergies Test2"));
        List<MedicalRecord> medicalRecordList= new ArrayList<>(Collections.singleton(medicalRecord));

        when(medicalRecordService.addMedicalRecord(medicalRecord)).thenReturn(medicalRecordList);

        List<MedicalRecord> result = medicalRecordService.addMedicalRecord(new MedicalRecord());

        assertEquals(medicalRecordList, result);
    }

    @Test
    public void testEditMedicalRecords() {
        String firstName = "Test FirstName1";
        String lastName = "Test LastName1";
        MedicalRecord medicalRecord = new MedicalRecord("Test FirstName1", "Test LastName1", "11/11/1111", List.of("Medication Test1"), List.of("Allergies Test1"));
        List<MedicalRecord> listTestMedicalRecord = new ArrayList(Collections.singleton(medicalRecord));
        when(medicalRecordRepositories.getMedicalRecords()).thenReturn(listTestMedicalRecord);

        MedicalRecord updatedMedicalRecord = new MedicalRecord(firstName, lastName, "11/11/1222", List.of("Medication Test2"), List.of("Allergies Test2"));

        boolean result = medicalRecordService.editMedicalRecords(firstName, lastName, updatedMedicalRecord);

        assertTrue(result);

        assertEquals("11/11/1222", medicalRecord.getBirthdate());
        assertEquals(List.of("Medication Test2"), medicalRecord.getMedications());
        assertEquals(List.of("Allergies Test2"), medicalRecord.getAllergies());
    }

    @Test
    public void testdeleteMedicalRecordByName() {
        MedicalRecord medicalRecord1 = new MedicalRecord("Test FirstName1", "Test LastName1", "11/11/1111", Arrays.asList("Medication Test1", "Medication Test2"), Arrays.asList("Allergies Test1", "Allergies Test2"));
        MedicalRecord medicalRecord2 = new MedicalRecord("Test FirstName2", "Test LastName2", "11/11/1222", Arrays.asList("Medication Test3", "Medication Test4"), Arrays.asList("Allergies Test3", "Allergies Test4"));
        MedicalRecord medicalRecord3 = new MedicalRecord("Test FirstName3", "Test LastName3", "11/11/1333", Arrays.asList("Medication Test5", "Medication Test6"), Arrays.asList("Allergies Test5", "Allergies Test6"));
        List<MedicalRecord> medicalRecordList = Arrays.asList(medicalRecord1,medicalRecord2,medicalRecord3);
        String firstName = "Test FirstName1";
        String lastName = "Test LastName1";

        when(medicalRecordRepositories.getMedicalRecords()).thenReturn(medicalRecordList);

        List<MedicalRecord> updatedMedicalRecord = medicalRecordService.deleteMedicalRecordByName(firstName, lastName);

        assertEquals(2, updatedMedicalRecord.size());
    }

    @Test
    void testGetMedicalRecordByCompleteName() {
        String firstName = "Test FirstName1";
        String lastName = "Test LastName1";
        List<MedicalRecord> listTestMedicalRecord = Arrays.asList(
                new MedicalRecord("Test FirstName1", "Test LastName1", "11/11/1111", Arrays.asList("Medication Test1"), Arrays.asList("Allergies Test1")),
                new MedicalRecord("Test FirstName2", "Test LastName2", "22/02/1112", Arrays.asList("Medication Test3", "Medication Test4"), Arrays.asList("Allergies Test3", "Allergies Test4")));

        when(medicalRecordRepositories.getMedicalRecords()).thenReturn(listTestMedicalRecord);

        List<Object[]> result = medicalRecordService.getMedicalRecordByCompleteName(firstName, lastName);

        Object[] record1 = result.get(0);
        assertEquals(List.of("Medication Test1"), record1[0]);
        assertEquals(List.of("Allergies Test1"), record1[1]);
    }



    @Test
    void testGetAgeByCompleteName() {
        String firstName = "Test FirstName1";
        String lastName = "Test LastName1";
        String birthday = "11/11/1111";
        List<MedicalRecord> listTestMedicalRecord = List.of(
                new MedicalRecord("Test FirstName1", "Test LastName1", "11/11/1111", List.of("Medication Test1"), List.of("Allergies Test1")));
        LocalDate fakeCurrentDate = LocalDate.parse("01/01/2023", DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        when(medicalRecordRepositories.getMedicalRecords()).thenReturn(listTestMedicalRecord);

        int result = medicalRecordService.getAgeByCompleteName(firstName, lastName);

        int expectedAge = fakeCurrentDate.getYear() - LocalDate.parse(birthday, DateTimeFormatter.ofPattern("MM/dd/yyyy")).getYear();

        assertEquals(expectedAge, result);
    }

    @Test
    void testChildOrNot(){
        List<MedicalRecord> listTestMedicalRecord = Arrays.asList(
                new MedicalRecord("Marneus", "Calgar", "11/11/2015", Arrays.asList("Medication Test1"), Arrays.asList("Allergies Test1")),
                new MedicalRecord("Sol", "Pyro", "11/02/1112", Arrays.asList("Medication Test3", "Medication Test4"), Arrays.asList("Allergies Test3", "Allergies Test4")));

        when(medicalRecordRepositories.getMedicalRecords()).thenReturn(listTestMedicalRecord);

        boolean test = medicalRecordService.childOrNot("Marneus", "Calgar");
        assertTrue(test);

        boolean test2 = medicalRecordService.childOrNot("Sol", "Pyro");
        assertFalse(test2);
    }
}
