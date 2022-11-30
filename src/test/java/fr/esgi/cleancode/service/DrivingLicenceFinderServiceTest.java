package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.ResourceNotFoundException;
import fr.esgi.cleancode.model.DrivingLicence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DrivingLicenceFinderServiceTest {

    @InjectMocks
    private DrivingLicenceFinderService service;

    @Mock
    private InMemoryDatabase database;
    @Mock
    private DrivingLicenceIdGenerationService generationService;

    @Test
    void should_find() {
        final var given = DrivingLicence.builder().id(generationService.generateNewDrivingLicenceId()).build();
        when(database.findById(given.getId())).thenReturn(Optional.of(given));

        final var actual = service.findById(given.getId()).orElse(null);
        assertThat(actual).isEqualTo(given);
    }

    @Test
    void should_not_find() {
        assertThat(service.findById(generationService.generateNewDrivingLicenceId()).isPresent()).isEqualTo(false);
    }
}