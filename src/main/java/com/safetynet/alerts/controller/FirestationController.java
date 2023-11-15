package com.safetynet.alerts.controller;

import com.safetynet.alerts.exception.ResourceNotFoundException;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.FirestationRepositories;
import com.safetynet.alerts.repositories.PersonRepositories;
import com.safetynet.alerts.service.FirestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/firestation")
public class FirestationController {

    @Autowired
    private FirestationService firestationService;
    @Autowired
    FirestationRepositories firestationRepositories;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping("/firestations")
    public Iterable<Firestation> list() {
        return firestationService.list();
    }

    @PostMapping("/firestation")
    public ResponseEntity<Firestation> createFirestation(@RequestBody Firestation firestation) {

        Firestation createdFirestation = firestationService.save(firestation);
        return new ResponseEntity<>(createdFirestation, HttpStatus.CREATED);

    }

    @PutMapping("/firestation/{id}")
    public ResponseEntity<Firestation> updateFirestation(@PathVariable long id, @RequestBody Firestation firestationDetail){

        Firestation updatedFirestation = firestationRepositories.findById(id).orElseThrow(() -> new ResourceNotFoundException("Firestation not exist with id: " + id));

        updatedFirestation.setStation(firestationDetail.getStation());

        firestationRepositories.save(updatedFirestation);

        return ResponseEntity.ok(firestationDetail);
    }
    @DeleteMapping("/firestation/{id}")
    public Map<String, Boolean> deleteFirestation(@PathVariable long id) throws ResourceNotFoundException{
        Firestation deletedFirestation = firestationRepositories.findById(id).orElseThrow(() -> new ResourceNotFoundException("Firestation not exist with id: " + id));

        firestationRepositories.delete(deletedFirestation);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}