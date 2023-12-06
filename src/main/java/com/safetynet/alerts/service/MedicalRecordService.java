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
@Data
@Service
public class MedicalRecordService {
    private MedicalRecordRepositories medicalRecordRepositories;

    public MedicalRecordService(MedicalRecordRepositories medicalRecordRepositories) {
        this.medicalRecordRepositories = medicalRecordRepositories;
    }

    public Iterable<MedicalRecord> list() {
        return medicalRecordRepositories.findAll();
    }

    public Optional<MedicalRecord> get(Long id) {
        return medicalRecordRepositories.findById(id);
    }

    public MedicalRecord save(MedicalRecord medicalRecord) {
        return medicalRecordRepositories.save(medicalRecord);
    }

    public Iterable<MedicalRecord> save(List<MedicalRecord> medicalRecords) {
        return medicalRecordRepositories.saveAll(medicalRecords);
    }

    public List<Object[]> getMedicalRecordByCompleteName(String firstName, String lastName) {
        return medicalRecordRepositories.findMedicalRecordByCompleteName(firstName, lastName);
    }

    public int getAgeByCompleteName(String firstName, String lastName) {

        String birthday = medicalRecordRepositories.findBirthDayByCompleteName(firstName, lastName).toString();

        LocalDate birthdate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(birthdate, currentDate).getYears();

        return age;
    }

    public boolean childOrNot(String firstName, String lastName){
        int age = getAgeByCompleteName(firstName, lastName);

        if (age <= 18){
            return true;
        } else {
            return false;
        }
    }
}

