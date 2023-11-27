package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FloodDTO {

    private String firestationAdressServed;
    private List<PersonFloodDTO> personList;

}
