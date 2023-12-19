package com.safetynet.alerts.controller;

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

    @PostMapping("/firestation")
    public ResponseEntity<List<Firestation>> createFirestation(@RequestBody Firestation firestation) {
        try {
            List<Firestation> createdFirestation = firestationService.addFirestation(firestation);
            logger.info("POST request from /firestation successful");
            return new ResponseEntity<>(createdFirestation, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error processing POST request to /firestation", e);
            return null;
        }
    }

    @PutMapping("/firestation/{address}")
    public ResponseEntity<Firestation> updateFirestation(@PathVariable String address, @RequestBody String station) {
        try {

            Optional<Firestation> updatedFirestation = firestationService.editFirestationNumber(address, station);

            logger.info("PUT request to /firestation/" + address + " successful");
            return ResponseEntity.ok(updatedFirestation.get());
        } catch (Exception e) {
            logger.error("Error processing PUT request to /firestation/" + address, e);
            return null;
        }
    }

    @DeleteMapping("/firestation")
    public Map<String, Boolean> deleteFirestation(
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "station", required = false) String station) {
        try {
            if (address != null) {
                firestationService.deleteFirestationByAddress(address);
            } else if (station != null) {
                firestationService.deleteFirestationByStation(station);
            } else {
                return (Map<String, Boolean>) ResponseEntity.badRequest().build();
            }

            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        } catch (Exception e) {
            logger.error("Error processing DELETE request to /firestation", e);
            return null;
        }
    }
}