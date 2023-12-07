package com.safetynet.alerts.controller;

import com.safetynet.alerts.exception.ResourceNotFoundException;
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
import java.util.Optional;

@RestController
@Controller
//@RequestMapping("/persons")
public class PersonController {

    private static final Logger logger = LogManager.getLogger(PersonController.class);

    @Autowired
    private PersonService personService;

    @Autowired
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
            throw e;
        }
    }

    @GetMapping("/person/{id}")
    public Optional<Person> getPerson(@PathVariable("id") final Long id) {
        try {
            Optional<Person> person = personService.get(id);
            if (person.isPresent()) {
                logger.info("GET request to /person/" + id + " successful");
            } else {
                logger.info("GET request to /person/" + id + " returned no data");
            }
            return person;
        } catch (Exception e) {
            logger.error("Error processing GET request to /person/" + id, e);
            throw e;
        }
    }

    @PostMapping("/person")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        try {
            Person createdPerson = personService.save(person);
            logger.info("POST request from /person successful");
            return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error processing POST request to /person", e);
            throw e;
        }
    }

    @PutMapping("/person/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable long id, @RequestBody Person personDetails) {
        try {
            Person updatedPerson = personRepositories.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not exist with id: " + id));

            updatedPerson.setAddress(personDetails.getAddress());
            updatedPerson.setCity(personDetails.getCity());
            updatedPerson.setZip(personDetails.getZip());
            updatedPerson.setPhone(personDetails.getPhone());
            updatedPerson.setEmail(personDetails.getEmail());

            personRepositories.save(updatedPerson);

            logger.info("PUT request to /person/" + id + " successful");
            return ResponseEntity.ok(personDetails);
        } catch (Exception e) {
            logger.error("Error processing PUT request to /person/" + id, e);
            throw e;
        }
    }

    @DeleteMapping("/person")
    public Map<String, Boolean> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        try {
            Long idDeletedPerson = personRepositories.findIdByFirstNameAndLastName(firstName, lastName);

            personRepositories.deleteById(idDeletedPerson);
            logger.info("Delete request to /person/" + firstName + " " + lastName + " successful");
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        } catch (Exception e) {
            logger.error("Error processing DELETE request to /person/" + firstName + " " + lastName, e);
            throw e;
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
            throw e;
        }
    }
}


