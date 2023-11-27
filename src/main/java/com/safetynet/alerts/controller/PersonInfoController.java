package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.*;
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

    @GetMapping("/flood/stations")
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

    @GetMapping("/phoneAlert")
    public List<PhoneAlertDTO> getFloodInfoByFirestation(@RequestParam String firestation) {

        List<PhoneAlertDTO> result = new ArrayList<>();
        List<Object[]> firestationAdressServed = firestationService.getAddressByFireStationNumber(firestation);

        if (firestationAdressServed != null && !firestationAdressServed.isEmpty()) {

            for (int i = 0; i < firestationAdressServed.size(); i++) {

                String address = firestationAdressServed.get(i)[0].toString();
                List<Object[]> personList = personService.getPhoneByAddress(address);


                for (Object[] personData : personList) {

                    String phone = personData[0].toString();

                    result.add(new PhoneAlertDTO(phone));
                }
            }
        }

        return result;
    }

    @GetMapping("/childAlert")
    public List<ChildAlertDTO> getChildInfoBy(@RequestParam String address) {

        List<ChildAlertDTO> result = new ArrayList<>();

        List<Object[]> personList = personService.getPersonByAddress(address);

        List<FirstAndLastNameDTO> childInfo = new ArrayList<>();
        List<FirstAndLastNameDTO> otherFamilyMember = new ArrayList<>();

        for (Object[] personData : personList) {

            String firstName = personData[0].toString();
            String lastName = personData[1].toString();
            boolean child = medicalRecordService.childOrNot(firstName, lastName);

            if (child == true) {
                childInfo.add(new FirstAndLastNameDTO(firstName, lastName));
            } else {
                otherFamilyMember.add(new FirstAndLastNameDTO(firstName, lastName));
            }
//todo inclure le nombre d'enfant pour ne rien retourner si il n'y en a pas
        }
        result.add(new ChildAlertDTO(childInfo, otherFamilyMember));
        return result;
    }

    @GetMapping("/firestation")
    public List<FirestationDTO> getInfoByFirestation(@RequestParam String stationNumber) {
        List<FirestationDTO> result = new ArrayList<>();
        List<FirestationInfoDTO> personInfo = new ArrayList<>();
        List<Object[]> firestationAdressServed = firestationService.getAddressByFireStationNumber(stationNumber);

        if (firestationAdressServed != null && !firestationAdressServed.isEmpty())
            for (int i = 0; i < firestationAdressServed.size(); i++) {

                String address = firestationAdressServed.get(i)[0].toString();
                List<Object[]>personList = personService.getPersonForFirestations(address);
                for (Object[] personData : personList) {

                    String firstName = personData[0].toString();
                    String lastName = personData[1].toString();
                    String addressPerson = personData[2].toString();
                    String phone = personData[3].toString();

                    personInfo.add(new FirestationInfoDTO(firstName, lastName, addressPerson,phone));
//ToDO décompte nombre d'adulte
                }
            }
        result.add(new FirestationDTO(personInfo));
        return result;
    }
}