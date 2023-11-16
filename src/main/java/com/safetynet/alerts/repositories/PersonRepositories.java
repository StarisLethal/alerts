package com.safetynet.alerts.repositories;

import com.safetynet.alerts.model.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonRepositories extends CrudRepository<Person, Long> {

    @Query("SELECT p.id FROM Person p WHERE p.firstName = :firstName AND p.lastName = :lastName")
    Long findByFirstNameAndLastName(@Param("firstName") String firstName,@Param("lastName") String lastName);

}

