package com.safetynet.alerts.controller;

import com.safetynet.alerts.exception.ResourceNotFoundException;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.PersonRepositories;
import com.safetynet.alerts.service.PersonService;
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

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepositories personRepositories;


    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/persons")
    public Iterable<Person> getPersons() {
        return personService.list();
    }

    @GetMapping("/person/{id}")
    public Optional<Person> getPerson(@PathVariable("id") final Long id){
        return personService.get(id);
    }

    @PostMapping("/person")
    public ResponseEntity<Person> createPerson (@RequestBody Person person) {
        Person createdPerson = personService.save(person);
        return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
    }

    @PutMapping ("/person/{id}")
    public ResponseEntity<Person> updatePerson (@PathVariable long id, @RequestBody Person personDetails) {
        Person updatedPerson = personRepositories.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not exist with id: " + id));

        updatedPerson.setAddress(personDetails.getAddress());
        updatedPerson.setCity(personDetails.getCity());
        updatedPerson.setZip(personDetails.getZip());
        updatedPerson.setPhone(personDetails.getPhone());
        updatedPerson.setEmail(personDetails.getEmail());

        personRepositories.save(updatedPerson);

        return ResponseEntity.ok(personDetails);
    }

    @DeleteMapping("/person{firstName}{lastName}")
    public Map<String,Boolean> deletePerson (@RequestParam String firstName, @RequestParam String lastName){

        Long idDeletedPerson = personRepositories.findByFirstNameAndLastName(firstName, lastName);

        personRepositories.deleteById(idDeletedPerson);
        Map<String,Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @GetMapping("/communityEmail")
    public List<String> getAllCityMail(@RequestParam final String city){
        return personService.getAllEmails(city);
    }

/*    @GetMapping("/personInfo")
    public ResponseEntity<Person> getNAAM (@RequestParam String firstName, @RequestParam String lastName){
        Optional<Person> personOptional = personService.getPersonNAAM(firstName, lastName);

        return personOptional.map(person -> new ResponseEntity<>(person, HttpStatus.OK)).orElseThrow();
    }*/
}


