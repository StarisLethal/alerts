package com.safetynet.alerts;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class AlertsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlertsApplication.class, args);
    }

/*    @Bean
    CommandLineRunner runner(PersonService personService) {
        return args -> {
            //read json and write to database
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Person>> typeReference = new TypeReference<List<Person>>() {};
            InputStream inputStream = TypeReference.class.getResourceAsStream("/json/data.json");
            try {
                List<Person> persons = mapper.readValue(inputStream, typeReference);
                personService.save(persons);
                System.out.println("Persons saved!");
            } catch (IOException e) {
                System.out.println("Unable to save persons" + e.getMessage());
            }
        };
    }*/


}

