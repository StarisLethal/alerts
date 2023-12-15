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
import java.util.List;
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
            return null;
        }
    }

/*    @GetMapping("/firestation/{id}")
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
            return null;
        }
    }*/

    @PostMapping("/firestation")
    public ResponseEntity<List<Firestation>> createFirestation(@RequestBody List<Firestation> firestation) {
        try {
            List<Firestation> createdFirestation = firestationService.addFirestation(firestation);
            logger.info("POST request from /firestation successful");
            return new ResponseEntity<>(createdFirestation, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error processing POST request to /firestation", e);
            return null;
        }
    }

    /*@PutMapping("/firestation/{id}")
    public ResponseEntity<Firestation> updateFirestation(@PathVariable String address, @RequestBody Firestation firestationDetail) {
        try {

            updatedFirestation.setStation(firestationDetail.getStation());

            firestationRepositories.save(updatedFirestation);

            logger.info("PUT request to /firestation/" + id + " successful");
            return ResponseEntity.ok(firestationDetail);
        } catch (Exception e) {
            logger.error("Error processing PUT request to /firestation/" + id, e);
            return null;
        }
    }*/

    @DeleteMapping("/deleteFirestationByAddress")
    public Map<String, Boolean> deleteFirestationByAddress(@RequestParam(name = "address") String address) {
        try {
            firestationService.deleteFirestationByAddress(address);
            logger.info("Delete request to /medicalRecord/" + address + " successful");
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;

        } catch (Exception e) {
            logger.error("Error processing DELETE request to /medicalRecord/" + address, e);
            return null;
        }
    }

    @DeleteMapping("/deleteFirestationByStation")
    public Map<String, Boolean> deleteFirestationByStation(@RequestParam(name = "station") String station) {
        try {
            firestationService.deleteFirestation(station);
            logger.info("Delete request to /medicalRecord/" + station + " successful");
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        } catch (Exception e) {
            logger.error("Error processing DELETE request to /medicalRecord/" + station, e);
            return null;
        }
    }
}