package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FireDTO {

    private List<PersonFireInfoDTO> personList;
    private List<Object[]> firestationNumber;


}
