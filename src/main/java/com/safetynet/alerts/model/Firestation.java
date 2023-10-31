package com.safetynet.alerts.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
public class Firestation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    private String address;
    private String station;

    public Firestation() {

    }
}
