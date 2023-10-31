package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repositories.FirestationRepositories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirestationService {
    private FirestationRepositories firestationRepositories;

    public FirestationService(FirestationRepositories firestationRepositories) {
        this.firestationRepositories = firestationRepositories;
    }

    public Iterable<Firestation> list(){
        return firestationRepositories.findAll();
    }

    public Firestation save(Firestation firestation){
        return firestationRepositories.save(firestation);
    }

    public Iterable<Firestation> save(List<Firestation> firestations) {
        return firestationRepositories.saveAll(firestations);
    }
}
