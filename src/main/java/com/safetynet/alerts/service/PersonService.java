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
        return personRepositories.getPersons();
    }

    public List<Person> addPerson(Person newPerson) {
        List<Person> person = personRepositories.getPersons();
        List<Person> updatedPerson = person.stream()
                .toList();

        person.add(newPerson);

        return person;
    }

    public boolean editPerson(String firstName, String lastName, Person person) {
        List<Person> persons = personRepositories.getPersons();

        Optional<Person> updatedPerson = persons.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst();

        if (updatedPerson.isPresent()) {
            Person SavedUpdatedPerson = updatedPerson.get();
            SavedUpdatedPerson.setAddress(person.getAddress());
            SavedUpdatedPerson.setPhone(person.getPhone());
            SavedUpdatedPerson.setCity(person.getCity());
            SavedUpdatedPerson.setZip(person.getZip());
            SavedUpdatedPerson.setEmail(person.getEmail());

            return true;
        }

        return false;
    }

    public List<Person> deletePersonByName(String firstName, String lastName) {
        List<Person> persons = personRepositories.getPersons();

        List<Person> personFromName = persons.stream()
                .filter(p -> !p.getFirstName().equalsIgnoreCase(firstName) && !p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());

        personRepositories.setPersons(personFromName);

        return personFromName;
    }

    public List<String> getAllEmails(String city) {

        if ("Culver".equalsIgnoreCase(city)) {

            List<Person> personList = personRepositories.getPersons();
            return personList.stream().map(Person::getEmail)
                    .collect(Collectors.toList());

        } else {
            return null;
        }
    }

    public List<Object[]> getPersonByCompleteName(String firstName, String lastName) {
        List<Person> persons = personRepositories.getPersons();
        List<Object[]> personFromName = (List<Object[]>) persons.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName))
                .map(p -> new Object[]{p.getFirstName(), p.getLastName(), p.getAddress(), p.getEmail()})
                .collect(Collectors.toList());
        return personFromName;
    }
    public List<Object[]> getFLPByAddress(String address) {
        List<Person> persons = personRepositories.getPersons();
        List<Object[]>  personsFromAddress = (List<Object[]> ) persons.stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .map(p -> new Object[]{p.getFirstName(), p.getLastName(), p.getPhone()})
                .collect(Collectors.toList());
        return personsFromAddress;
    }
    public List<Object[]> getPersonByAddress(String address) {
        List<Person> persons = personRepositories.getPersons();
        List<Object[]>  personsFromAddress = (List<Object[]> ) persons.stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .map(p -> new Object[]{p.getFirstName(), p.getLastName()})
                .collect(Collectors.toList());
        return personsFromAddress;
    }

    public List<Object[]> getPhoneByAddress(String address) {
        List<Person> persons = personRepositories.getPersons();
        List<Object[]> personsFromAddress = persons.stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .map(p -> new Object[]{p.getPhone()})
                .collect(Collectors.toList());

        return personsFromAddress;
    }

    public List<Object[]> getPersonForFirestations(String address) {
        List<Person> persons = personRepositories.getPersons();
        List<Object[]> personsFromAddress = persons.stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .map(p -> new Object[]{p.getFirstName(), p.getLastName(), p.getAddress(), p.getPhone()})
                .collect(Collectors.toList());
        return personsFromAddress;
    }
}