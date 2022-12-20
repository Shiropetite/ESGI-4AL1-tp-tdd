package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DrivingLicenceSaveService {

    private final InMemoryDatabase database;

    private final DrivingLicenceIdGenerationService generationService;

    public DrivingLicence save(String socialSecurityNumber) throws InvalidDriverSocialSecurityNumberException {
        if(!isValidSocialNumber(socialSecurityNumber)) throw  new InvalidDriverSocialSecurityNumberException("Social security number is invalid");
        DrivingLicence drivingLicence =  DrivingLicence.builder()
                .id(generationService.generateNewDrivingLicenceId())
                .driverSocialSecurityNumber(socialSecurityNumber)
                .build();
        return  database.save(drivingLicence.getId(), drivingLicence);
    }

    private boolean isValidSocialNumber(String socialSecurityNumber){
        if (socialSecurityNumber == null) return false;
        if (socialSecurityNumber.length() != 15) return false;
        return socialSecurityNumber.matches("^\\d*$");
    }
}
