package com.safetynet.alerts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controller.PersonController;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.PersonRepositories;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PersonController.class)
@AutoConfigureMockMvc
public class PersonControllerTests {

    @Autowired
    private PersonController personController;
    @MockBean
    PersonService personService;
    @MockBean
    PersonRepositories personRepositories;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetPersons() throws Exception {
        Iterable<Person> expectedPersons = Arrays.asList(
                new Person("Test FirstName","Test LastName","Test Address","Test City","Test Zip","Test Phone","Test Mail"),
                new Person("Test FirstName2","Test LastName2","Test Address2","Test City2","Test Zip2","Test Phone2","Test Mail2"));

        when(personService.list()).thenReturn(expectedPersons);

        mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].firstName").value("Test FirstName"))
                .andExpect(jsonPath("$[0].lastName").value("Test LastName"))
                .andExpect(jsonPath("$[0].address").value("Test Address"))
                .andExpect(jsonPath("$[1].firstName").value("Test FirstName2"))
                .andExpect(jsonPath("$[1].lastName").value("Test LastName2"))
                .andExpect(jsonPath("$[1].address").value("Test Address2"));
    }

    @Test
    public void testPostPerson() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();

        Person person = new Person();
        person.setFirstName("Test FirstName");
        person.setLastName("Test LastName");
        person.setAddress("Test Address");
        person.setCity("Test City");
        person.setZip("Test Zip");
        person.setPhone("Test Phone");
        person.setEmail("Test Mail");
        List<Person> personList = List.of(person);
        when(personService.addPerson(any(Person.class))).thenReturn(personList);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].firstName").value("Test FirstName"))
                .andExpect(jsonPath("$[0].lastName").value("Test LastName"));
    }

    @Test
    public void testUpdatePerson() {
        Person person = new Person("Test FirstName", "Test LastName", "Test Address",
                "Test City", "Test Zip", "Test Phone", "Test Mail");

        when(personService.editPerson("Test FirstName", "Test LastName", person)).thenReturn(true);

        ResponseEntity<Person> response = personController.updatePerson("Test FirstName", "Test LastName", person);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeletePerson() throws Exception {
        String firstName = "testname";
        String lastName = "testlastname";
        Person personTest = new Person();
        personTest.setFirstName(firstName);
        personTest.setLastName(lastName);

        mockMvc.perform(delete("/person")
                        .param("firstName", firstName)
                        .param("lastName", lastName)


                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"deleted\":true}"));
    }

    @Test
    public void testCommunityEmail() throws Exception {
        String city = "Test City";
        List<String> testmail = Arrays.asList("test@mail.fr", "test2@mail.fr");
                when(personService.getAllEmails(city)).thenReturn(testmail);
        mockMvc.perform(get("/communityEmail")
                        .param("city", city))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("test@mail.fr"))
                .andExpect(jsonPath("$[1]").value("test2@mail.fr"));
    }
}
