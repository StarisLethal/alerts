package com.safetynet.alerts.controller;

import com.safetynet.alerts.exception.ResourceNotFoundException;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.MedicalRecordRepositories;
import com.safetynet.alerts.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import javax.swing.text.html.parser.Entity;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Controller
//@RequestMapping("/medicalrecord")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;
    @Autowired
    private MedicalRecordRepositories medicalRecordRepositories;
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/medicalrecord")
    public Iterable<MedicalRecord> list(){
        return medicalRecordService.list();
    }

    @GetMapping("/medicalRecord/{id}")
    public Optional<MedicalRecord> getMedicalRecord(@PathVariable("id") final Long id){
        return medicalRecordService.get(id);
    }

    @PostMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecord medicalRecord){
        MedicalRecord createdMedicalRecord = medicalRecordRepositories.save(medicalRecord);
        return new ResponseEntity<>(createdMedicalRecord, HttpStatus.CREATED);
    }

    @PutMapping("/medicalRecord/{id}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable Long id, @RequestBody MedicalRecord medicalRecord) throws ResourceNotFoundException{

        MedicalRecord updatedMedicalRecord = medicalRecordRepositories.findById(id).orElseThrow(() -> new ResourceNotFoundException("Medical Record not exist with id: " + id));

        updatedMedicalRecord.setBirthdate(medicalRecord.getBirthdate());
        updatedMedicalRecord.setMedications(medicalRecord.getMedications());
        updatedMedicalRecord.setAllergies(medicalRecord.getAllergies());

        medicalRecordService.save(updatedMedicalRecord);

        return ResponseEntity.ok(updatedMedicalRecord);
    }

    @DeleteMapping("/medicalRecord")
    public Map<String,Boolean> deleteMedicalRecord (@RequestParam String firstName, @RequestParam String lastName){

        Long idDeletedMedicalRecord = medicalRecordRepositories.findIdByFirstNameAndLastName(firstName, lastName);

        medicalRecordRepositories.deleteById(idDeletedMedicalRecord);
        Map<String,Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


}
