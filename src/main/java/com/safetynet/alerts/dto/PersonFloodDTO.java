package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PersonFloodDTO {

    private String firstName;
    private String lastName;
    private List<Object[]> medicalRecords;
    private int age;
    private String Phone;

}
