package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FloodAndFireDTO;
import com.safetynet.alerts.dto.PersonFireInfoDTO;
import com.safetynet.alerts.dto.personInfoDTO;
import com.safetynet.alerts.service.FirestationService;
import com.safetynet.alerts.service.MedicalRecordService;
import com.safetynet.alerts.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("")
public class PersonInfoController {

    @Autowired
    private PersonService personService;
    @Autowired
    private FirestationService firestationService;
    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping("/personInfo")
    public personInfoDTO getAddressInfo(@RequestParam String firstName, @RequestParam String lastName) {
        List<Object[]> personList = personService.getPersonByCompleteName(firstName, lastName);
        List<Object[]> medicalRecordList = medicalRecordService.getMedicalRecordByCompleteName(firstName, lastName);

        return new personInfoDTO(personList, medicalRecordList);
    }

    @GetMapping("/fire")
    public FloodAndFireDTO getFireInfo(@RequestParam String address) {

        List<Object[]> personList = personService.getPersonByAddress(address);
        List<Object[]> firestationNumber = firestationService.getFireStationByAddress(address);
        List<PersonFireInfoDTO> persons = new ArrayList<>();

        for (Object[] personData : personList) {
            String firstName = personData[0].toString();
            String lastName = personData[1].toString();
            String phone = personData[2].toString();

            List<Object[]> medicalRecordList = medicalRecordService.getMedicalRecordByCompleteName(firstName, lastName);
            int age = medicalRecordService.getAgeByCompleteName(firstName, lastName);
            List<String> personDetails = Arrays.asList(firstName, lastName, phone);

            persons.add(new PersonFireInfoDTO(personDetails, age,medicalRecordList));
        }

        return new FloodAndFireDTO(persons, firestationNumber);
    }
}
