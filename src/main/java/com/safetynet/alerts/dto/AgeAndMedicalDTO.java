package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AgeAndMedicalDTO {

    private int age;
    private List<String> medications;
    private List<String> allergies;

}
