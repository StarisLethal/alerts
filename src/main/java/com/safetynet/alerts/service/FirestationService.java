package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repositories.FirestationRepositories;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Firestation> addFirestation(Firestation newFirestation) {
        List<Firestation> fireStations = firestationRepositories.getFirestations();
        List<Firestation> updatedFireStations = fireStations.stream()
                .collect(Collectors.toList());

        fireStations.add(newFirestation);

        return fireStations;
    }

    public Optional<Firestation> editFirestationNumber(String currentAddress, String newStationNumber) {
        List<Firestation> firestations = firestationRepositories.getFirestations();

        Optional<Firestation> updatedStation = firestations.stream()
                .filter(station -> station.getAddress().equals(currentAddress))
                .findFirst();

        if (updatedStation.isPresent()) {
            updatedStation.get().setStation(newStationNumber);
            return updatedStation;
        }

        return Optional.empty();
    }

    public List<Firestation> deleteFirestationByStation(String firestationNumber) {
        List<Firestation> fireStations = firestationRepositories.getFirestations();

        List<Firestation> updatedFireStations = fireStations.stream()
                .filter(f -> !f.getStation().equals(firestationNumber))
                .collect(Collectors.toList());

        firestationRepositories.setFirestations(updatedFireStations);

        return updatedFireStations;
    }

    public List<Firestation> deleteFirestationByAddress(String address) {
        List<Firestation> fireStations = firestationRepositories.getFirestations();

        List<Firestation> updatedFireStations = fireStations.stream()
                .filter(f -> !f.getAddress().equals(address))
                .collect(Collectors.toList());

        firestationRepositories.setFirestations(updatedFireStations);

        return updatedFireStations;
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
