package com.safetynet.alerts.controller;

import com.safetynet.alerts.exception.ResourceNotFoundException;
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
import java.util.Map;
import java.util.Optional;

@RestController
@Controller
//@RequestMapping("/medicalrecord")
public class MedicalRecordController {

    private static final Logger logger = LogManager.getLogger(PersonController.class);
    @Autowired
    private MedicalRecordService medicalRecordService;
    @Autowired
    private MedicalRecordRepositories medicalRecordRepositories;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/medicalrecord")
    public Iterable<MedicalRecord> list() {
        try {
            logger.info("GET request to /medicalrecord successful");
            return medicalRecordService.list();
        } catch (Exception e) {
            logger.error("Error processing GET request to /medicalrecord", e);
            throw e;
        }
    }

    @GetMapping("/medicalRecord/{id}")
    public Optional<MedicalRecord> getMedicalRecord(@PathVariable("id") final Long id) {
        try {
            Optional<MedicalRecord> medicalRecord = medicalRecordService.get(id);
            if (medicalRecord.isPresent()) {
                logger.info("GET request to /medicalRecord/" + id + " successful");
            } else {
                logger.info("GET request to /medicalRecord/" + id + " returned no data");
            }
            return medicalRecord;
        } catch (Exception e) {
            logger.error("Error processing GET request to /medicalRecord/" + id, e);
            throw e;
        }
    }

    @PostMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        try {
            MedicalRecord createdMedicalRecord = medicalRecordRepositories.save(medicalRecord);
            logger.info("POST request from /medicalRecord successful");
            return new ResponseEntity<>(createdMedicalRecord, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error processing POST request to /medicalRecord", e);
            throw e;
        }
    }

    @PutMapping("/medicalRecord/{id}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable Long id, @RequestBody MedicalRecord medicalRecord) throws ResourceNotFoundException {
        try {
            MedicalRecord updatedMedicalRecord = medicalRecordRepositories.findById(id).orElseThrow(() -> new ResourceNotFoundException("Medical Record not exist with id: " + id));

            updatedMedicalRecord.setBirthdate(medicalRecord.getBirthdate());
            updatedMedicalRecord.setMedications(medicalRecord.getMedications());
            updatedMedicalRecord.setAllergies(medicalRecord.getAllergies());

            medicalRecordService.save(updatedMedicalRecord);

            logger.info("PUT request to /medicalRecord/" + id + " successful");
            return ResponseEntity.ok(updatedMedicalRecord);
        } catch (Exception e) {
            logger.error("Error processing PUT request to /medicalRecord/" + id, e);
            throw e;
        }
    }

    @DeleteMapping("/medicalRecord")
    public Map<String, Boolean> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) {
        try {
            Long idDeletedMedicalRecord = medicalRecordRepositories.findIdByFirstNameAndLastName(firstName, lastName);

            medicalRecordRepositories.deleteById(idDeletedMedicalRecord);
            logger.info("Delete request to /medicalRecord/" + firstName + " " + lastName + " successful");
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        } catch (Exception e) {
            logger.error("Error processing DELETE request to /medicalRecord/" + firstName + " " + lastName, e);
            throw e;
        }
    }
}
