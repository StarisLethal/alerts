package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
@Controller
//@RequestMapping("/persons")
public class PersonController {

    private PersonService personService;

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
    @GetMapping("/communityEmail/{city}")
    public Iterable<Person> getAllCityMail(@PathVariable("city") final String city){
        return personService
    }
}


