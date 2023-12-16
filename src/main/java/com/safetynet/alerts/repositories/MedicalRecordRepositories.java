package com.safetynet.alerts.repositories;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MedicalRecordRepositories{

    @Getter
    @Setter
    List<MedicalRecord> medicalRecords;
}
