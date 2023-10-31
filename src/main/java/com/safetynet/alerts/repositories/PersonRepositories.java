package com.safetynet.alerts.repositories;

import com.safetynet.alerts.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepositories extends CrudRepository<Person, Long> {
}
