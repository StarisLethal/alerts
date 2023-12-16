package com.safetynet.alerts.dto;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PersonFireInfoDTO {

    private List<Object[]> personDetails;
    private int age;
    private List<Object[]> medicalRecords;

}
