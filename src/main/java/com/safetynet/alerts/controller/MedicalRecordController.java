package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repositories.MedicalRecordRepositories;
import com.safetynet.alerts.service.MedicalRecordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Controller
//@RequestMapping("/medicalrecord")
public class MedicalRecordController {

    private static final Logger logger = LogManager.getLogger(PersonController.class);
    @Autowired
    private MedicalRecordService medicalRecordService;

    private MedicalRecordRepositories medicalRecordRepositories;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/medicalrecords")
    public Iterable<MedicalRecord> list() {
        try {
            logger.info("GET request to /medicalrecord successful");
            return medicalRecordService.list();
        } catch (Exception e) {
            logger.error("Error processing GET request to /medicalrecord", e);
            return null;
        }
    }

    @PostMapping("/medicalRecord")
    public ResponseEntity<List<MedicalRecord>> createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        try {
            List<MedicalRecord> createdMedicalRecord = medicalRecordService.addMedicalRecord(medicalRecord);
            logger.info("POST request from /medicalRecord successful");
            return new ResponseEntity<>(createdMedicalRecord, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error processing POST request to /medicalRecord", e);
            return null;
        }
    }

    @PutMapping("/medicalRecord")
    public ResponseEntity<Void> updateMedicalRecord(@PathVariable String firstName, @PathVariable String lastName, @RequestBody MedicalRecord medicalRecord){
        try {

            boolean successfulRequest = medicalRecordService.editMedicalRecords(firstName, lastName, medicalRecord);

            if (successfulRequest) {
                return new ResponseEntity<>(HttpStatus.OK);}
            logger.info("PUT request to /medicalRecord/" + firstName + " " + lastName + " successful");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error processing PUT request to /medicalRecord/" + firstName +" "+ lastName, e);
            return null;
        }
    }

    @DeleteMapping("/medicalRecord")
    public Map<String, Boolean> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) {
        try {

            medicalRecordService.deleteMedicalRecordByName(firstName, lastName);

            logger.info("Delete request to /medicalRecord/" + firstName + " " + lastName + " successful");
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        } catch (Exception e) {
            logger.error("Error processing DELETE request to /medicalRecord/" + firstName + " " + lastName, e);
            return null;
        }
    }
}
