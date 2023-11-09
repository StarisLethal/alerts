package com.safetynet.alerts.repositories;

import com.safetynet.alerts.CustomProperties;
import com.safetynet.alerts.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class PersonProxy {

    @Autowired
    private CustomProperties props;

    public Iterable<Person> getPersons(){
        String baseApiUrl = props.getApiUrl();
        String getPersonsUrl = baseApiUrl + "/persons";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Iterable<Person>> response = restTemplate.exchange(
                getPersonsUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Iterable<Person>>() {}

        );

        log.debug("Get person Call " + response.getStatusCode().toString());

        return response.getBody();
    }

    public Person getPerson(int id){

        String baseApiUrl = props.getApiUrl();
        String  getEmployeeUrl = baseApiUrl + "/persons/" + id;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Person> response = restTemplate.exchange(
                getEmployeeUrl,
                HttpMethod.GET,
                null,
                Person.class
        );

        log.debug("Get person call " + response.getStatusCode().toString());

        return response.getBody();
    }

}
