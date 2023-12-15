package com.safetynet.alerts.repositories;

import com.safetynet.alerts.model.Firestation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FirestationRepositories  {

    @Getter
    @Setter
    List<Firestation> firestations;
}
