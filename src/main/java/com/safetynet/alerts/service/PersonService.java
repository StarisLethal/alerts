package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.PersonProxy;
import com.safetynet.alerts.repositories.PersonRepositories;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Data
@Service
public class PersonService {

    private PersonRepositories personRepositories;
    private PersonProxy personProxy;

    public PersonService(PersonRepositories personRepositories) {
        this.personRepositories = personRepositories;
    }

    public Iterable<Person> list() {
        return personRepositories.findAll();
    }

    public Optional<Person> get(Long id) {
        return personRepositories.findById(id);
    }

    public Person save(Person person) {
        return personRepositories.save(person);
    }

    public Iterable<Person> save(List<Person> persons) {
        return personRepositories.saveAll(persons);
    }

    public Iterable<Person> mail(List<Person> persons) {
        return ;
    }
}