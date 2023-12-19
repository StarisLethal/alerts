package com.safetynet.alerts;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.PersonRepositories;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        Person person1 = new Person("Test FirstName","Test LastName","Test Address","Test City","Test Zip","Test Phone","Test Mail");
        Person person2 = new Person("Test FirstName2","Test LastName2","Test Address2","Test City2","Test Zip2","Test Phone2","Test Mail2");
        List<Person> personList = Arrays.asList(person1, person2);

        when(personRepositories.getPersons()).thenReturn(personList);

        Iterable<Person> result = personService.list();

        assertEquals(personList, result);
    }

    @Test
    void testSavePerson() {
        Person person = new Person("Test FirstName","Test LastName","Test Address","Test City","Test Zip","Test Phone","Test Mail");
        List<Person> personList = new ArrayList<>(Collections.singleton(person));

        when(personService.addPerson(person)).thenReturn(personList);

        List<Person> result = personService.addPerson(person);

        assertEquals(personList, result);
    }

    @Test
    public void testEditPerson() {
        String firstName = "Test First Name";
        String lastName = "Test Last Name";
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAddress("Test Address");
        person.setPhone("Test Phone");
        person.setCity("Test City");
        person.setZip("Test Zip");
        person.setEmail("Test Mail");
        List<Person> personList = new ArrayList<>(Collections.singleton(person));

        when(personRepositories.getPersons()).thenReturn(personList);

        Person updatedPerson = new Person(firstName, lastName, "Test Address2", "Test City2", "Test Zip2", "Test Phone2", "Test Mail2");

        boolean result = personService.editPerson(firstName, lastName, updatedPerson);

        assertTrue(result);

        assertEquals("Test Address2", person.getAddress());
        assertEquals("Test Phone2", person.getPhone());
        assertEquals("Test City2", person.getCity());
        assertEquals("Test Zip2", person.getZip());
        assertEquals("Test Mail2", person.getEmail());
    }

    @Test
    public void testdeletePersonByName() {
        Person person1 = new Person("Test FirstName","Test LastName","Test Address","Test City","Test Zip","Test Phone","Test Mail");
        Person person2 = new Person("Test FirstName2","Test LastName2","Test Address2","Test City2","Test Zip2","Test Phone2","Test Mail2");
        Person person3 = new Person("Test FirstName3", "Test LastName3", "Test Address3", "Test City3", "Test Zip3", "Test Phone3", "Test Mail3");
        List<Person> personList = Arrays.asList(person1, person2, person3);
        String firstName = "Test FirstName";
        String lastName = "Test LastName";

        when(personRepositories.getPersons()).thenReturn(personList);

        List<Person> updatedPerson = personService.deletePersonByName(firstName, lastName);

        assertEquals(2, updatedPerson.size());
    }

    @Test
    void testGetAllEmails() {
        List<Person> testMail = new ArrayList<>();
        Person person1 = new Person();
        person1.setEmail("sleepy_dev@example.com");
        testMail.add(person1);

        when(personRepositories.getPersons()).thenReturn(testMail);

        List<String> result = personService.getAllEmails("Culver");

        assertEquals(testMail.stream().map(Person::getEmail).collect(Collectors.toList()), result);
    }


    @Test
    void testGetPersonByCompleteName() {
        Person person1 = new Person("Test FirstName","Test LastName","Test Address","Test City","Test Zip","Test Phone","Test Mail");
        Person person2 = new Person("Test FirstName2","Test LastName2","Test Address2","Test City2","Test Zip2","Test Phone2","Test Mail2");
        List<Person> personList = Arrays.asList(person1, person2);
        String firstName = "Test FirstName";
        String lastName = "Test LastName";


        when(personRepositories.getPersons()).thenReturn(personList);

        List<Object[]> result = personService.getPersonByCompleteName(firstName, lastName);

        Object[] personResult = result.get(0);
        assertEquals("Test Address", personResult[2]);
        assertEquals("Test Mail", personResult[3]);
    }

    @Test
    void testGetPersonByAddress() {
        Person person1 = new Person("Test FirstName","Test LastName","Test Address","Test City","Test Zip","Test Phone","Test Mail");
        Person person2 = new Person("Test FirstName2","Test LastName2","Test Address2","Test City2","Test Zip2","Test Phone2","Test Mail2");
        List<Person> personList = Arrays.asList(person1, person2);
        String address = "Test Address";
        when(personRepositories.getPersons()).thenReturn(personList);

        List<Object[]> result = personService.getPersonByAddress(address);

        Object[] personResult = result.get(0);
        assertEquals("Test FirstName", personResult[0]);
        assertEquals("Test LastName", personResult[1]);
    }

    @Test
    void testGetFLPByAddress() {
        Person person1 = new Person("Test FirstName","Test LastName","Test Address","Test City","Test Zip","Test Phone","Test Mail");
        Person person2 = new Person("Test FirstName2","Test LastName2","Test Address2","Test City2","Test Zip2","Test Phone2","Test Mail2");
        List<Person> personList = Arrays.asList(person1, person2);
        String address = "Test Address";
        when(personRepositories.getPersons()).thenReturn(personList);

        List<Object[]> result = personService.getFLPByAddress(address);

        Object[] personResult = result.get(0);
        assertEquals("Test FirstName", personResult[0]);
        assertEquals("Test LastName", personResult[1]);
        assertEquals("Test Phone", personResult[2]);
    }

    @Test
    void testGetPhoneByAddress() {
        Person person1 = new Person("Test FirstName","Test LastName","Test Address","Test City","Test Zip","Test Phone","Test Mail");
        Person person2 = new Person("Test FirstName2","Test LastName2","Test Address2","Test City2","Test Zip2","Test Phone2","Test Mail2");
        List<Person> personList = Arrays.asList(person1, person2);
        String address = "Test Address";
        when(personRepositories.getPersons()).thenReturn(personList);

        List<Object[]> result = personService.getPhoneByAddress(address);

        Object[] personResult = result.get(0);
        assertEquals("Test Phone", personResult[0]);
    }

    @Test
    void testGetPersonForFirestations() {
        Person person1 = new Person("Test FirstName","Test LastName","Test Address","Test City","Test Zip","Test Phone","Test Mail");
        Person person2 = new Person("Test FirstName2","Test LastName2","Test Address2","Test City2","Test Zip2","Test Phone2","Test Mail2");
        List<Person> personList = Arrays.asList(person1, person2);
        String address = "Test Address";
        when(personRepositories.getPersons()).thenReturn(personList);

        List<Object[]> result = personService.getPersonForFirestations(address);

        Object[] personResult = result.get(0);
        assertEquals("Test FirstName", personResult[0]);
        assertEquals("Test LastName", personResult[1]);
        assertEquals("Test Address", personResult[2]);
        assertEquals("Test Phone", personResult[3]);
    }
}

