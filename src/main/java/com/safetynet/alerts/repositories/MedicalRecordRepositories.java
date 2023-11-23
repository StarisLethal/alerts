package com.safetynet.alerts.repositories;

import com.safetynet.alerts.model.MedicalRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicalRecordRepositories extends CrudRepository<MedicalRecord, Long> {

    @Query("SELECT m.medications, m.allergies FROM MedicalRecord m WHERE m.firstName = :firstName AND m.lastName = :lastName")
    List<Object[]> findMedicalRecordByCompleteName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT m.birthdate FROM MedicalRecord m WHERE m.firstName = :firstName AND m.lastName = :lastName")
    String findBirthDayByCompleteName(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
