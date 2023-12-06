package com.safetynet.alerts.repositories;

import com.safetynet.alerts.model.Firestation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FirestationRepositories extends CrudRepository<Firestation, Long> {

    @Query("SELECT f.station FROM Firestation f WHERE f.address = :address")
    List<Object[]> findByAddressForFire(@Param("address") String address);

    @Query("SELECT f.address FROM Firestation f WHERE f.station = :station")
    List<Object[]> findByFireStationNumber(@Param("station") String station);

    @Query("SELECT f.id FROM Firestation f WHERE f.address = :address")
    Long deleteByAddress(@Param("address") String address);

    @Query("SELECT f.id FROM Firestation f WHERE f.station = :station")
    Long deleteByStation(@Param("station") String station);


}
