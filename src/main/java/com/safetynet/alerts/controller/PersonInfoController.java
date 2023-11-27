package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FireDTO;
import com.safetynet.alerts.dto.FloodDTO;
import com.safetynet.alerts.dto.PersonFireInfoDTO;
import com.safetynet.alerts.dto.PersonFloodDTO;
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
    public PersonFireInfoDTO getAddressInfo(@RequestParam String firstName, @RequestParam String lastName) {
        List<String> personList = personService.getPersonByCompleteName(firstName, lastName);
        int age = medicalRecordService.getAgeByCompleteName(firstName, lastName);
        List<Object[]> medicalRecordList = medicalRecordService.getMedicalRecordByCompleteName(firstName, lastName);

        return new PersonFireInfoDTO(personList, age, medicalRecordList);
    }

    @GetMapping("/fire")
    public FireDTO getFireInfo(@RequestParam String address) {

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

            persons.add(new PersonFireInfoDTO(personDetails, age, medicalRecordList));
        }

        return new FireDTO(persons, firestationNumber);
    }

    @GetMapping("/flood")
    public List<FloodDTO> getFloodInfo(@RequestParam List<String> stations) {

        List<FloodDTO> result = new ArrayList<>();

        for (String firestationNumber : stations) {

            List<Object[]> firestationAdressServed = firestationService.getAddressByFireStationNumber(firestationNumber);


            if (firestationAdressServed != null && !firestationAdressServed.isEmpty()) {

                for (int i = 0; i < firestationAdressServed.size(); i++) {

                    String address = firestationAdressServed.get(i)[0].toString();
                    List<Object[]> personList = personService.getPersonByAddress(address);
                    List<PersonFloodDTO> persons = new ArrayList<>();

                    for (Object[] personData : personList) {
                        String firstName = personData[0].toString();
                        String lastName = personData[1].toString();
                        String phone = personData[2].toString();

                        List<Object[]> medicalRecordList = medicalRecordService.getMedicalRecordByCompleteName(firstName, lastName);
                        int age = medicalRecordService.getAgeByCompleteName(firstName, lastName);

                        persons.add(new PersonFloodDTO(firstName, lastName, medicalRecordList, age, phone));
                    }
                    result.add(new FloodDTO(address, persons));
                }

            }
        }
        return result;
    }
}
