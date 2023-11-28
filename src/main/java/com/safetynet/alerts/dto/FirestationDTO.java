package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FirestationDTO {

    private List<FirestationInfoDTO> personInfo;
    private int numberOfAdult;
    private int numberOfChild;

}
