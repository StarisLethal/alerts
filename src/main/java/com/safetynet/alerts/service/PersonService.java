package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.PersonRepositories;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Service
public class PersonService {

    @Autowired
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

    public  List<String> getPersonByCompleteName (String firstName, String lastName){
        return personRepositories.findPersonInfoByCompleteName(firstName, lastName);
    }

    public  List<Object[]> getPersonByAddress (String address){
        return personRepositories.findByAddressForFire(address);
    }

    public  List<Object[]> getPhoneByAddress (String address){
        return personRepositories.findByAddressForPhoneAlert(address);
    }

    public List<Object[]> getPersonForFirestations (String address){
        return  personRepositories.findByAddressForFirestation(address);
    }
}