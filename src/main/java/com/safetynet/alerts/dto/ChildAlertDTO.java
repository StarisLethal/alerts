package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChildAlertDTO {

    List<FirstAndLastNameDTO> childInfo;
    List<FirstAndLastNameDTO> familyMembers;

}
