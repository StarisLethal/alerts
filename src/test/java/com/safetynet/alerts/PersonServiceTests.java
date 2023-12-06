package com.safetynet.alerts;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.PersonRepositories;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonServiceTests {

    @MockBean
    private PersonRepositories personRepositories;
    @Autowired
    private PersonService personService;

    @Test
    void testListPersons() {
        Person person1 = new Person(1L,"Test FirstName","Test LastName","Test Address","Test City","Test Zip","Test Phone","Test Mail");
        Person person2 = new Person(2L,"Test FirstName2","Test LastName2","Test Address2","Test City2","Test Zip2","Test Phone2","Test Mail2");
        List<Person> personList = Arrays.asList(person1, person2);

        when(personRepositories.findAll()).thenReturn(personList);

        Iterable<Person> result = personService.list();

        assertEquals(personList, result);
    }

    @Test
    void testGetPersonById() {
        Long id = 1L;
        Person person = new Person();
        person.setFirstName("Test FirstName");
        person.setLastName("Test LastName");
        person.setAddress("Test Address");
        person.setCity("Test City");
        person.setZip("Test Zip");
        person.setPhone("Test Phone");
        person.setEmail("Test Mail");

        when(personRepositories.findById(id)).thenReturn(Optional.of(person));

        Optional<Person> result = personRepositories.findById(id);

        assertEquals(person, result.orElse(null));
    }

    @Test
    void testSavePerson() {
        Person person = new Person();
        person.setFirstName("Test FirstName");
        person.setLastName("Test LastName");
        person.setAddress("Test Address");
        person.setCity("Test City");
        person.setZip("Test Zip");
        person.setPhone("Test Phone");
        person.setEmail("Test Mail");

        when(personRepositories.save(person)).thenReturn(person);

        Person result = personService.save(person);

        assertEquals(person, result);
    }

    @Test
    void testSavePersons() {
        List<Person> personList = new ArrayList<>();
        Person person1 = new Person();
        person1.setAddress("Test Address");
        person1.setCity("Test City");
        person1.setZip("Test Zip");
        person1.setPhone("Test Phone");
        person1.setEmail("Test Mail");

        Person person2 = new Person();
        person2.setAddress("Origin Address");
        person2.setCity("Origin City");
        person2.setZip("Origin Zip");
        person2.setPhone("Origin Phone");
        person2.setEmail("Origin Mail");
        personList.add(person1);
        personList.add(person2);

        when(personRepositories.saveAll(personList)).thenReturn(personList);

        Iterable<Person> result = personService.save(personList);

        assertEquals(personList, result);
    }
    @Test
    void testGetAllEmails() {
        List<Person> testMail = new ArrayList<>();
        Person person1 = new Person();
        person1.setEmail("sleepy_dev@example.com");
        testMail.add(person1);

        when(personRepositories.findAll()).thenReturn(testMail);

        List<String> result = personService.getAllEmails("Culver");

        assertEquals(testMail.stream().map(Person::getEmail).collect(Collectors.toList()), result);
    }


    @Test
    void testGetPersonByCompleteName() {
        String firstName = "Marneus";
        String lastName = "Calgar";
        List<String> expectedEmails = Arrays.asList("sleepy_dev@example.com");

        when(personRepositories.findPersonInfoByCompleteName(firstName, lastName)).thenReturn(expectedEmails);

        List<String> result = personService.getPersonByCompleteName(firstName, lastName);

        assertEquals(expectedEmails, result);
    }

    @Test
    void testGetPersonByAddress() {
        String address = "42 Solar System";
        List<Object[]> expectedPersonData = Collections.singletonList(new Object[]{"Marneus", "Calgar"});

        when(personRepositories.findByAddressForFire(address)).thenReturn(expectedPersonData);

        List<Object[]> result = personService.getPersonByAddress(address);

        assertEquals(expectedPersonData, result);
    }

    @Test
    void testGetPhoneByAddress() {
        String address = "42 Solar System";
        List<Object[]> expectedPhoneData = Collections.singletonList(new Object[]{"Marneus", "Calgar", "1111-1111-111111"});

        when(personRepositories.findByAddressForPhoneAlert(address)).thenReturn(expectedPhoneData);

        List<Object[]> result = personService.getPhoneByAddress(address);

        assertEquals(expectedPhoneData, result);
    }

    @Test
    void testGetPersonForFirestations() {
        String address = "42 Solar System";
        List<Object[]> expectedPersonData = Collections.singletonList(new Object[]{"Marneus", "Calgar"});

        when(personRepositories.findByAddressForFirestation(address)).thenReturn(expectedPersonData);

        List<Object[]> result = personService.getPersonForFirestations(address);

        assertEquals(expectedPersonData, result);
    }
}

