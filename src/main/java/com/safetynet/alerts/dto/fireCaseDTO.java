package com.safetynet.alerts.dto;

import com.safetynet.alerts.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class fireCaseDTO {

    private List<Object[]> personList;
    private String firestations;

}