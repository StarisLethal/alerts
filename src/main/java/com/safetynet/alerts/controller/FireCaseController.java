package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.fireCaseDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.FirestationRepositories;
import com.safetynet.alerts.service.FirestationService;
import com.safetynet.alerts.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class FireCaseController {

    @Autowired
    private PersonService personService;
    @Autowired
    private FirestationService firestationService;


    @GetMapping("/fire")
    public fireCaseDTO getAddressInfo(@RequestParam String address){
        List<Object[]> personList = personService.getPersonByAddress(address);
        String firestation = firestationService.getFireStationByAddress(address);

        return new fireCaseDTO(personList, firestation);
    }

}
