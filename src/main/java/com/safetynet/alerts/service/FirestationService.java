package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.FirestationRepositories;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Data
@Service
public class FirestationService {

    @Autowired
    private FirestationRepositories firestationRepositories;

    public FirestationService(FirestationRepositories firestationRepositories) {
        this.firestationRepositories = firestationRepositories;
    }

    public Iterable<Firestation> list(){
        return firestationRepositories.findAll();
    }

    public Optional<Firestation> get(Long id) {
        return firestationRepositories.findById(id);
    }

    public Firestation save(Firestation firestation){
        return firestationRepositories.save(firestation);
    }

    public Iterable<Firestation> save(List<Firestation> firestations) {
        return firestationRepositories.saveAll(firestations);
    }



    public List<Object[]> getFireStationByAddress(String address){

        return firestationRepositories.findByAddressForFire(address);
    }

    public List<Object[]> getAddressByFireStationNumber(String firestationNumber){

        return firestationRepositories.findByFireStationNumber(firestationNumber);

    }
}
