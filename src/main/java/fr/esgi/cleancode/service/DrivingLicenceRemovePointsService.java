package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.ResourceNotFoundException;
import fr.esgi.cleancode.model.DrivingLicence;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DrivingLicenceRemovePointsService {

    private final InMemoryDatabase database;

    public DrivingLicence removePoints(UUID drivingLicenceId, int pointToRemove) throws ResourceNotFoundException {
        Optional<DrivingLicence> optionalDrivingLicence = database.findById(drivingLicenceId);
        if(optionalDrivingLicence.isEmpty()) {
            throw new ResourceNotFoundException("The driving licence doesn't exist");
        }

        DrivingLicence currentDrivingLicence = optionalDrivingLicence.get();
        if (currentDrivingLicence.getAvailablePoints() - pointToRemove < 0) {
            currentDrivingLicence = currentDrivingLicence.withAvailablePoints(0);
        }
        else {
            currentDrivingLicence = currentDrivingLicence.withAvailablePoints(currentDrivingLicence.getAvailablePoints() - pointToRemove);
        }
        database.save(drivingLicenceId, currentDrivingLicence);

        return currentDrivingLicence;
    }
}
