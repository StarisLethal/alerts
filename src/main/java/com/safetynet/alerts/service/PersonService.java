package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.PersonRepositories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private PersonRepositories personRepositories;

    public PersonService(PersonRepositories personRepositories) {
        this.personRepositories = personRepositories;
    }

    public Iterable<Person> list(){
        return personRepositories.findAll();
    }

    public Person save(Person person){
        return personRepositories.save(person);
    }

    public Iterable<Person> save(List<Person> persons) {
        return personRepositories.saveAll(persons);
    }
}
