package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repositories.MedicalRecordRepositories;
import com.safetynet.alerts.repositories.PersonRepositories;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {
    private MedicalRecordRepositories medicalRecordRepositories;

    public MedicalRecordService(MedicalRecordRepositories medicalRecordRepositories) {
        this.medicalRecordRepositories = medicalRecordRepositories;
    }

    public Iterable<MedicalRecord> list(){
        return medicalRecordRepositories.findAll();
    }

    public Optional<MedicalRecord> get(Long id) {
        return medicalRecordRepositories.findById(id);
    }

    public MedicalRecord save(MedicalRecord medicalRecord){
        return medicalRecordRepositories.save(medicalRecord);
    }

    public Iterable<MedicalRecord> save(List<MedicalRecord> medicalRecords) {
        return medicalRecordRepositories.saveAll(medicalRecords);
    }

}

