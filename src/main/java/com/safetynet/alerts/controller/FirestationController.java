package com.safetynet.alerts.controller;

import com.safetynet.alerts.exception.ResourceNotFoundException;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repositories.FirestationRepositories;
import com.safetynet.alerts.service.FirestationService;
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
//@RequestMapping("/firestation")
public class FirestationController {

    private static final Logger logger = LogManager.getLogger(PersonController.class);
    @Autowired
    FirestationRepositories firestationRepositories;
    @Autowired
    private FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping("/firestations")
    public Iterable<Firestation> list() {
        try {
            logger.info("GET request to /firestations successful");
            return firestationService.list();
        } catch (Exception e) {
            logger.error("Error processing GET request to /firestations", e);
            throw e;
        }
    }

    @GetMapping("/firestation/{id}")
    public Optional<Firestation> getFirestation(@PathVariable("id") final Long id) {
        try {
            Optional<Firestation> firestation = firestationService.get(id);
            if (firestation.isPresent()) {
                logger.info("GET request to /firestation/" + id + " successful");
            } else {
                logger.info("GET request to /firestation/" + id + " returned no data");
            }
            return firestation;
        } catch (Exception e) {
            logger.error("Error processing GET request to /firestation/" + id, e);
            throw e;
        }
    }

    @PostMapping("/firestation")
    public ResponseEntity<Firestation> createFirestation(@RequestBody Firestation firestation) {
        try {
            Firestation createdFirestation = firestationService.save(firestation);
            logger.info("POST request from /firestation successful");
            return new ResponseEntity<>(createdFirestation, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error processing POST request to /firestation", e);
            throw e;
        }
    }

    @PutMapping("/firestation/{id}")
    public ResponseEntity<Firestation> updateFirestation(@PathVariable long id, @RequestBody Firestation firestationDetail) {
        try {
            Firestation updatedFirestation = firestationRepositories.findById(id).orElseThrow(() -> new ResourceNotFoundException("Firestation not exist with id: " + id));

            updatedFirestation.setStation(firestationDetail.getStation());

            firestationRepositories.save(updatedFirestation);

            logger.info("PUT request to /firestation/" + id + " successful");
            return ResponseEntity.ok(firestationDetail);
        } catch (Exception e) {
            logger.error("Error processing PUT request to /firestation/" + id, e);
            throw e;
        }
    }

    @DeleteMapping("/deleteFirestationByAddress")
    public Map<String, Boolean> deleteFirestationByAddress(@RequestParam(name = "address") String address) {
        try {
            Long idDeletedFirestation = firestationRepositories.deleteByAddress(address);

            firestationRepositories.deleteById(idDeletedFirestation);
            logger.info("Delete request to /medicalRecord/" + address + " successful");
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;

        } catch (Exception e) {
            logger.error("Error processing DELETE request to /medicalRecord/" + address, e);
            throw e;
        }
    }

    @DeleteMapping("/deleteFirestationByStation")
    public Map<String, Boolean> deleteFirestationByStation(@RequestParam(name = "station") String station) {
        try {
            Long idDeletedFirestation = firestationRepositories.deleteByAddress(station);

            firestationRepositories.deleteById(idDeletedFirestation);
            logger.info("Delete request to /medicalRecord/" + station + " successful");
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        } catch (Exception e) {
            logger.error("Error processing DELETE request to /medicalRecord/" + station, e);
            throw e;
        }
    }
}