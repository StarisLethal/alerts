package com.safetynet.alerts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controller.PersonController;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.PersonRepositories;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PersonController.class)
@AutoConfigureMockMvc
public class PersonControllerTests {

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
        List<Person> listTestPerson= Arrays.asList(
                new Person(1L,"Test FirstName","Test LastName","Test Address","Test City","Test Zip","Test Phone","Test Mail"),
                new Person(2L,"Test FirstName2","Test LastName2","Test Address2","Test City2","Test Zip2","Test Phone2","Test Mail2"));

        when(personService.list()).thenReturn(listTestPerson);

        mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].firstName").value("Test FirstName"))
                .andExpect(jsonPath("$[0].lastName").value("Test LastName"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].firstName").value("Test FirstName2"))
                .andExpect(jsonPath("$[1].lastName").value("Test LastName2"));
    }

    @Test
    public void testGetPerson() throws Exception {
        Person person = new Person(1L,"Test FirstName","Test LastName","Test Address","Test City","Test Zip","Test Phone","Test Mail");

        Long id = 1L;
        when(personService.get(id)).thenReturn(Optional.of(person));
        mockMvc.perform(get("/person/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test FirstName"))
                .andExpect(jsonPath("$.lastName").value("Test LastName"));
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
        when(personService.save(any(Person.class))).thenReturn(person);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Test FirstName"))
                .andExpect(jsonPath("$.lastName").value("Test LastName"));
    }

    @Test
    public void testPutPerson() throws Exception {
        Person personPutTest = new Person();
        personPutTest.setAddress("Test Address");
        personPutTest.setCity("Test City");
        personPutTest.setZip("Test Zip");
        personPutTest.setPhone("Test Phone");
        personPutTest.setEmail("Test Mail");

        long id = 1L;

        Person personTest = new Person();
        personTest.setId(1L);
        personTest.setAddress("Origin Address");
        personTest.setCity("Origin City");
        personTest.setZip("Origin Zip");
        personTest.setPhone("Origin Phone");
        personTest.setEmail("Origin Mail");

        when(personRepositories.findById(id)).thenReturn(Optional.of(personTest));

        ResultActions response = mockMvc.perform(put("/person/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personPutTest)));

        response.andExpect(status().isOk());

        assertEquals(personPutTest.getAddress(), personTest.getAddress());
        assertEquals(personPutTest.getCity(), personTest.getCity());
        assertEquals(personPutTest.getZip(), personTest.getZip());
        assertEquals(personPutTest.getPhone(), personTest.getPhone());
        assertEquals(personPutTest.getEmail(), personTest.getEmail());
    }

    @Test
    public void testDeletePerson() throws Exception {
        String firstName = "testname";
        String lastName = "testlastname";
        long id = 1L;
        Person personTest = new Person();
        personTest.setId(1L);
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
