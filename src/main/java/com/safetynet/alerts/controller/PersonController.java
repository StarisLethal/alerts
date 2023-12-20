package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.PersonRepositories;
import com.safetynet.alerts.service.PersonService;
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
//@RequestMapping("/persons")
public class PersonController {

    private static final Logger logger = LogManager.getLogger(PersonController.class);

    @Autowired
    private PersonService personService;

    private PersonRepositories personRepositories;


    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/persons")
    public Iterable<Person> getPersons() {
        try {
            logger.info("GET request to /persons successful");
            return personService.list();
        } catch (Exception e) {
            logger.error("Error processing GET request to /persons", e);
            return null;
        }
    }

    @PostMapping("/person")
    public ResponseEntity<List<Person>> createPerson(@RequestBody Person person) {
        try {
            List<Person> createdPerson = personService.addPerson(person);
            logger.info("POST request from /person successful");
            return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error processing POST request to /person", e);
            return null;
        }
    }

    @PutMapping("/person")
    public ResponseEntity<Person> updatePerson(@PathVariable String firstName, @PathVariable String lastName, @RequestBody Person personDetails) {
        try {
            boolean successfulRequest = personService.editPerson(firstName, lastName, personDetails);

            if (successfulRequest) {
                return new ResponseEntity<>(HttpStatus.OK);}

            logger.info("PUT request to /person/" + firstName + " " + lastName + " successful");
            return ResponseEntity.ok(personDetails);
        } catch (Exception e) {
            logger.error("Error processing PUT request to /person/" + firstName + " " + lastName, e);
            return null;
        }
    }

    @DeleteMapping("/person")
    public Map<String, Boolean> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        try {

            personService.deletePersonByName(firstName, lastName);

            logger.info("Delete request to /person/" + firstName + " " + lastName + " successful");
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        } catch (Exception e) {
            logger.error("Error processing DELETE request to /person/" + firstName + " " + lastName, e);
            return null;
        }
    }

    @GetMapping("/communityEmail")
    public List<String> getAllCityMail(@RequestParam final String city) {
        try {
            List<String> communityEmail = personService.getAllEmails(city);
            if (communityEmail.isEmpty()) {
                logger.info("GET request to /person/" + city + " returned no data");
            } else {
                logger.info("GET request to /person/" + city + " successful");
            }
            return communityEmail;
        } catch (Exception e) {
            logger.error("Error processing GET request to /communityEmail", e);
            return null;
        }
    }
}


