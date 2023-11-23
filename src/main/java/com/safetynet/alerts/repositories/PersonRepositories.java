package com.safetynet.alerts.repositories;

import com.safetynet.alerts.model.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepositories extends CrudRepository<Person, Long> {

    @Query("SELECT p.id FROM Person p WHERE p.firstName = :firstName AND p.lastName = :lastName")
    Long findIdByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT p.firstName, p.lastName, p.address, p.email FROM Person p WHERE p.firstName = :firstName AND p.lastName = :lastName")
    List<Object[]> findPersonInfoByCompleteName(@Param("firstName") String firstName,@Param("lastName") String lastName);

    @Query("SELECT p.firstName, p.lastName, p.phone FROM Person p WHERE p.address = :address")
    List<Object[]> findByAddressForFire(@Param("address") String address);
}

