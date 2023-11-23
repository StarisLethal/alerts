package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PersonFireInfoDTO {

    private List<String> personDetails;
    private int age;
    private List<Object[]> medicalRecords;

}
