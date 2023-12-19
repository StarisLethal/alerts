package com.safetynet.alerts.repositories;

import com.safetynet.alerts.model.Person;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepositories{
    @Getter
    @Setter
    List<Person> persons;
}

