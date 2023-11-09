package com.safetynet.alerts;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.FirestationRepositories;
import com.safetynet.alerts.repositories.MedicalRecordRepositories;
import com.safetynet.alerts.repositories.PersonRepositories;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final PersonRepositories personRepositories;
    private final FirestationRepositories firestationRepositories;
    private final MedicalRecordRepositories medicalRecordRepositories;


    public DataLoader(ObjectMapper objectMapper, PersonRepositories personRepositories, FirestationRepositories firestationRepositories, MedicalRecordRepositories medicalRecordRepositories) {
        this.objectMapper = objectMapper;
        this.personRepositories = personRepositories;
        this.firestationRepositories = firestationRepositories;
        this.medicalRecordRepositories = medicalRecordRepositories;
    }


    @Override
    public void run(String... args) throws Exception {
        List<Person> persons = new ArrayList<>();
        List<Firestation> firestations = new ArrayList<>();
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        JsonNode json;

        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/json/data.json")) {
            json = objectMapper.readValue(inputStream, JsonNode.class);

            JsonNode personsNode = json.get("persons");

            if (personsNode != null && personsNode.isArray()){
                for (JsonNode personNode : personsNode){
                    Person person = objectMapper.treeToValue(personNode, Person.class);
                    persons.add(person);
                }
            }else {
                throw new IllegalArgumentException("Invalid Json format for Person");
            }

            JsonNode firestationsNode = json.get("firestations");

            if (firestationsNode != null && firestationsNode.isArray()) {
                for (JsonNode firestationNode : firestationsNode) {
                    Firestation firestation = objectMapper.treeToValue(firestationNode, Firestation.class);
                    firestations.add(firestation);
                }
            } else {
                throw new IllegalArgumentException("Invalid Json format for Firestations");
            }

            JsonNode medicalRecordsNode = json.get("medicalrecords");

            if (medicalRecordsNode != null && medicalRecordsNode.isArray()) {
                for (JsonNode medicalRecordNode : medicalRecordsNode) {
                    MedicalRecord medicalRecord = objectMapper.treeToValue(medicalRecordNode, MedicalRecord.class);
                    medicalRecords.add(medicalRecord);
                }
            } else {
                throw new IllegalArgumentException("Invalid Json format for MedicalRecords");
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Json Data", e);
        }

            personRepositories.saveAll(persons);
            firestationRepositories.saveAll(firestations);
            medicalRecordRepositories.saveAll(medicalRecords);


    }
}
