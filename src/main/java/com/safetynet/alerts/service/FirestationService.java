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


    private FirestationRepositories firestationRepositories;

    public FirestationService(FirestationRepositories firestationRepositories) {
        this.firestationRepositories = firestationRepositories;
    }

    public Iterable<Firestation> list(){
        return firestationRepositories.getFirestations();
    }

    public List<Firestation> addFirestation(List<Firestation> firestation) {
        firestationRepositories.setFirestations(firestation);
        return firestation;
    }

    public List<Firestation> deleteFirestation(String station) {
        List<Firestation> firestations = firestationRepositories.getFirestations();
        firestations.removeIf(firestation -> firestation.getStation().equals(station));
        firestationRepositories.setFirestations(firestations);
        return firestations;
    }

    public List<Firestation> deleteFirestationByAddress(String address) {
        List<Firestation> firestations = firestationRepositories.getFirestations();
        firestations.removeIf(firestation -> firestation.getStation().equals(address));
        firestationRepositories.setFirestations(firestations);
        return firestations;
    }

    public String getFireStationByAddress(String address){
        List<Firestation> fireStations = firestationRepositories.getFirestations();
        String stationFromAddress = fireStations.stream()
                .filter(f -> f.getAddress().equalsIgnoreCase(address))
                .map(Firestation::getStation)
                .findAny()
                .orElse(null);

        return stationFromAddress;
    }

    public List<String> getAddressByFireStationNumber(String firestationNumber){
        List<Firestation> fireStations = firestationRepositories.getFirestations();
        List<String> addressFromStation = fireStations.stream()
                .filter(f -> f.getStation().equals(firestationNumber))
                .map(Firestation::getAddress)
                .distinct()
                .toList();

        return addressFromStation;
    }
}
