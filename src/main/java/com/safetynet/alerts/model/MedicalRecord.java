package com.safetynet.alerts.model;



import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MedicalRecord {

    private String firstName;
    private String lastName;
    private String birthdate;

    private List<String> medications;

    private List<String> allergies;

    public MedicalRecord() {

    }
}
