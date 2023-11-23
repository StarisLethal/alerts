package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PersonAndMedicalDTO {

    private List<Object[]> personDetails;
    private List<Object[]> medicalRecords;

}
