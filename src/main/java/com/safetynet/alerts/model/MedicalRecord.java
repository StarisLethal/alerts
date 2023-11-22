package com.safetynet.alerts.model;


import com.fasterxml.jackson.annotation.JsonFilter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Entity
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonFilter("birthdayfilter")

    private Long id;
    private String firstName;
    private String lastName;
    private String birthdate;

    @ElementCollection

    private List<String> medications;

    @ElementCollection

    private List<String> allergies;

}
