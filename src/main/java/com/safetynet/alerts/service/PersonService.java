package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.PersonRepositories;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Service
public class PersonService {

    private PersonRepositories personRepositories;

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

    public  List<String> getAllEmails (String city) {

        if ("Culver".equalsIgnoreCase(city)) {

            List<Person> personList = (List<Person>) personRepositories.findAll();
            return personList.stream().map(Person::getEmail).collect(Collectors.toList());

        }else{
            return null;
        }
    }

/*    public Optional<Person> getPersonNAAM(String firstName, String lastName){
        Optional<Person> personOptional = personRepositories.findByFirstNameAndLastName(firstName, lastName);
        Person person = personOptional.get();

        return Optional.of(new Person(person.getFirstName(),person.getLastName(),person.getAddress(),person.getEmail()));
    }*/
    public void delete (Long id){
        personRepositories.deleteById(id);
    }
}