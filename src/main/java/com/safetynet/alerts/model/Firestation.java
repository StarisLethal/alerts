package com.safetynet.alerts.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Firestation {

    private String address;
    private String station;

    public Firestation() {

    }
}
