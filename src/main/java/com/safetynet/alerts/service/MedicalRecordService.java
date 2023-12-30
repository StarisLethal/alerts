package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repositories.MedicalRecordRepositories;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Service
public class MedicalRecordService {


    private MedicalRecordRepositories medicalRecordRepositories;

    public MedicalRecordService(MedicalRecordRepositories medicalRecordRepositories) {
        this.medicalRecordRepositories = medicalRecordRepositories;
    }

    public Iterable<MedicalRecord> list() {
        return medicalRecordRepositories.getMedicalRecords();
    }

    public List<MedicalRecord> addMedicalRecord(MedicalRecord newMedicalRecord) {
        List<MedicalRecord> medicalRecords = medicalRecordRepositories.getMedicalRecords();

        medicalRecords.add(newMedicalRecord);

        return medicalRecords;
    }

    public boolean editMedicalRecords(String firstName, String lastName,MedicalRecord medicalRecord) {
        List<MedicalRecord> medicalRecords = medicalRecordRepositories.getMedicalRecords();

        Optional<MedicalRecord> updatedMedicalRecords = medicalRecords.stream()
                .filter(m -> m.getFirstName().equalsIgnoreCase(firstName) && m.getLastName().equalsIgnoreCase(lastName))
                .findFirst();

        if (updatedMedicalRecords.isPresent()) {
            MedicalRecord savedUpdatedMedicalRecord = updatedMedicalRecords.get();
            savedUpdatedMedicalRecord.setBirthdate(medicalRecord.getBirthdate());
            savedUpdatedMedicalRecord.setAllergies(medicalRecord.getAllergies());
            savedUpdatedMedicalRecord.setMedications(medicalRecord.getMedications());
            return true;
        }

        return false;
    }

    public List<MedicalRecord> deleteMedicalRecordByName(String firstName, String lastName) {
        List<MedicalRecord> fireStations = medicalRecordRepositories.getMedicalRecords();

        List<MedicalRecord> medicalRecordsFromName = fireStations.stream()
                .filter(m -> !m.getFirstName().equalsIgnoreCase(firstName) && !m.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());

        medicalRecordRepositories.setMedicalRecords(medicalRecordsFromName);

        return medicalRecordsFromName;
    }

    public List<Object[]> getMedicalRecordByCompleteName(String firstName, String lastName) {
        List<MedicalRecord> medicalRecords = medicalRecordRepositories.getMedicalRecords();
        List<Object[]> medicalRecordsFromName = medicalRecords.stream()
                .filter(m -> m.getFirstName().equalsIgnoreCase(firstName) && m.getLastName().equalsIgnoreCase(lastName))
                .map(p -> new Object[]{p.getMedications(), p.getAllergies()})
                .collect(Collectors.toList());

        return medicalRecordsFromName;
    }

    public int getAgeByCompleteName(String firstName, String lastName) {
        List<MedicalRecord> medicalRecords = medicalRecordRepositories.getMedicalRecords();
        Optional<MedicalRecord> medicalRecord = medicalRecords.stream()
                .filter(m -> m.getFirstName().equalsIgnoreCase(firstName) && m.getLastName().equalsIgnoreCase(lastName))
                .findFirst();

        if (medicalRecord.isPresent()) {
            String birthday = medicalRecord.get().getBirthdate();
            LocalDate birthdate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            LocalDate currentDate = LocalDate.now();
            int age = Period.between(birthdate, currentDate).getYears();
            return age;
        } else {
            return -404;
        }
    }
    public boolean childOrNot(String firstName, String lastName){
        int age = getAgeByCompleteName(firstName, lastName);

        return age <= 18;
    }
}

