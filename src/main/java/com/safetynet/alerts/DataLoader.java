package com.safetynet.alerts;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repositories.FirestationRepositories;
import com.safetynet.alerts.repositories.MedicalRecordRepositories;
import com.safetynet.alerts.repositories.PersonRepositories;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final PersonRepositories personRepositories;
    private final FirestationRepositories firestationRepositories;
    private final MedicalRecordRepositories medicalRecordRepositories;


    public DataLoader(PersonRepositories personRepositories, FirestationRepositories firestationRepositories, MedicalRecordRepositories medicalRecordRepositories) {
        this.personRepositories = personRepositories;
        this.firestationRepositories = firestationRepositories;
        this.medicalRecordRepositories = medicalRecordRepositories;
    }


    @Override
    public void run(String... args) throws Exception {
        List<Person> persons = new ArrayList<>();
        List<Firestation> firestations = new ArrayList<>();
        List<MedicalRecord> medicalRecords = new ArrayList<>();


Gson gson = new Gson();

        try (FileReader fileReader = new FileReader("C:\\Users\\letha\\IdeaProjects\\alerts\\src\\main\\resources\\json\\data.json")) {
            JsonElement jsonElement = gson.fromJson(fileReader, JsonElement.class);

            if (jsonElement.isJsonObject()) {
                JsonElement personsJson = jsonElement.getAsJsonObject().getAsJsonArray("persons");
                JsonElement firestationsJson = jsonElement.getAsJsonObject().getAsJsonArray("firestations");
                JsonElement medicalrecordsJson = jsonElement.getAsJsonObject().getAsJsonArray("medicalrecords");

                JsonArray jsonArrayPersons = personsJson.getAsJsonArray();
                JsonArray jsonArrayStations = firestationsJson.getAsJsonArray();
                JsonArray jsonArrayRecords = medicalrecordsJson.getAsJsonArray();

                for (JsonElement element : jsonArrayPersons) {
                    if (element.isJsonObject()) {
                        JsonObject jsonObject = element.getAsJsonObject();
                        persons.add(gson.fromJson(jsonObject, Person.class));
                    }
                }

                for (JsonElement element : jsonArrayStations) {
                    if (element.isJsonObject()) {
                        JsonObject jsonObject = element.getAsJsonObject();
                        firestations.add(gson.fromJson(jsonObject, Firestation.class));

                    }
                }

                for (JsonElement element : jsonArrayRecords) {
                    if (element.isJsonObject()) {
                        JsonObject jsonObject = element.getAsJsonObject();
                        medicalRecords.add(gson.fromJson(jsonObject, MedicalRecord.class));
                    }
                }
            }
        }
        if (personRepositories.existsById(1L)) {
            System.out.println("base de donnée déja remplis");
        }   else{
            personRepositories.saveAll(persons);
            firestationRepositories.saveAll(firestations);
            medicalRecordRepositories.saveAll(medicalRecords);
        }


    }
}
