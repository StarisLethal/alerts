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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("")
public class PersonInfoController {

    private static final Logger logger = LogManager.getLogger(PersonInfoController.class);
    @Autowired
    private PersonService personService;
    @Autowired
    private FirestationService firestationService;
    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping("/personInfo")
    public PersonFireInfoDTO getAddressInfo(@RequestParam String firstName, @RequestParam String lastName) {
        try {
            List<String> personList = personService.getPersonByCompleteName(firstName, lastName);
            int age = medicalRecordService.getAgeByCompleteName(firstName, lastName);
            List<Object[]> medicalRecordList = medicalRecordService.getMedicalRecordByCompleteName(firstName, lastName);

            logger.info("Request GET received for /personInfo with parameters: firstName="+firstName+", lastName="+lastName+" successful");
            return new PersonFireInfoDTO(personList, age, medicalRecordList);
        }catch (Exception e){
            logger.error("Error processing GET for /personInfo with parameters: firstName="+firstName+", lastName="+lastName, e);
            throw e;
        }
    }

    @GetMapping("/fire")
    public FireDTO getFireInfo(@RequestParam String address) {
        try {
            List<Object[]> personList = personService.getPersonByAddress(address);
            String firestationNumber = firestationService.getFireStationByAddress(address);
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
            logger.info("Request GET received for /fire with parameters: address="+address+" successful");
            return new FireDTO(persons, firestationNumber);
        }catch (Exception e){
            logger.error("Error processing GET for /fire with parameters: address="+address, e);
            throw e;
        }
    }

    @GetMapping("/flood/stations")
    public List<FloodDTO> getFloodInfo(@RequestParam List<String> stations) {
        try {
            List<FloodDTO> result = new ArrayList<>();

            for (String firestationNumber : stations) {

                List<String> firestationAdressServed = firestationService.getAddressByFireStationNumber(firestationNumber);


                if (firestationAdressServed != null && !firestationAdressServed.isEmpty()) {

                    for (int i = 0; i < firestationAdressServed.size(); i++) {

                        String address = firestationAdressServed.get(i).toString();
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
            logger.info("Request GET received for /flood/stations with parameters: stations="+stations+" successful");
            return result;
        }catch (Exception e){
            logger.error("Error processing GET for /flood/stations with parameters: stations="+stations, e);
            throw e;
        }

    }

    @GetMapping("/phoneAlert")
    public List<PhoneAlertDTO> getFloodInfoByFirestation(@RequestParam String firestation) {
        try {
            List<Object> phoneList = new ArrayList<>();
            List<PhoneAlertDTO> result = new ArrayList<>();
            List<String> firestationAdressServed = firestationService.getAddressByFireStationNumber(firestation);

            if (firestationAdressServed != null && !firestationAdressServed.isEmpty()) {

                for (int i = 0; i < firestationAdressServed.size(); i++) {

                    String address = firestationAdressServed.get(i).toString();
                    List<Object[]> personList = personService.getPhoneByAddress(address);


                    for (Object[] personData : personList) {

                        Object phone = personData[0];

                        phoneList.add(phone);
                    }
                }
            }
            result.add(new PhoneAlertDTO(phoneList));
            logger.info("Request GET received for /phoneAlert with parameters: firestation="+firestation+" successful");
            return result;
        }catch (Exception e){
            logger.error("Error processing GET for /phoneAlert with parameters: firestation="+firestation, e);
            throw e;
        }

    }

    @GetMapping("/childAlert")
    public List<ChildAlertDTO> getChildInfoBy(@RequestParam String address) {
        try {
            List<Object[]> personList = personService.getPersonByAddress(address);
            if (!personList.isEmpty()){
                List<ChildAlertDTO> result = new ArrayList<>();

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
                }
                result.add(new ChildAlertDTO(childInfo, otherFamilyMember));
                logger.info("Request GET received for /childAlert with parameters: address="+address+" successful");
                return result;
            } else {
                logger.info("Request GET received for /childAlert with parameters: address="+address+" return no data");
                return Collections.emptyList();
            }
        }catch (Exception e){
            logger.error("Error processing GET for /childAlert with parameters: address="+address, e);
            throw e;
        }
    }

    @GetMapping("/firestation")
    public List<FirestationDTO> getInfoByFirestation(@RequestParam String stationNumber) {
        try {
            List<FirestationDTO> result = new ArrayList<>();
            List<FirestationInfoDTO> personInfo = new ArrayList<>();
            List<String> firestationAdressServed = firestationService.getAddressByFireStationNumber(stationNumber);
            int numberOfAdult = 0;
            int numberOfChild = 0;

            if (firestationAdressServed != null && !firestationAdressServed.isEmpty())

                for (int i = 0; i < firestationAdressServed.size(); i++) {

                    String address = firestationAdressServed.get(i).toString();
                    List<Object[]> personList = personService.getPersonForFirestations(address);

                    for (Object[] personData : personList) {

                        String firstName = personData[0].toString();
                        String lastName = personData[1].toString();
                        String addressPerson = personData[2].toString();
                        String phone = personData[3].toString();
                        boolean childOrNot = medicalRecordService.childOrNot(firstName, lastName);

                        if (childOrNot) {
                            numberOfChild++;
                        } else {
                            numberOfAdult++;
                        }

                        personInfo.add(new FirestationInfoDTO(firstName, lastName, addressPerson, phone));

                    }
                }
            result.add(new FirestationDTO(personInfo, numberOfAdult, numberOfChild));
            logger.info("Request GET received for /firestation with parameters: stationNumber="+stationNumber+" successful");
            return result;
        }catch (Exception e){
            logger.error("Error processing GET for /firestation with parameters: stationNumber="+stationNumber, e);
            throw e;
        }
    }
}