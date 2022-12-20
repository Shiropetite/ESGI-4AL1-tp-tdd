package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DrivingLicenceSaveServiceTest {

    @InjectMocks
    private DrivingLicenceSaveService service;

    @Mock
    private InMemoryDatabase database;
    @Mock
    private DrivingLicenceIdGenerationService generationService;

    @ParameterizedTest
    @ValueSource(strings = {"012345678901234", "000000000000000", "444445555566666"})
    void should_save(String validDriverSocialSecurityNumber) {
        final var mockId = UUID.randomUUID();
        final var mockDrivingLicence = DrivingLicence.builder()
                .id(mockId)
                .driverSocialSecurityNumber(validDriverSocialSecurityNumber)
                .build();

        when(generationService.generateNewDrivingLicenceId()).thenReturn(mockId);
        when(database.save(eq(mockId), any(DrivingLicence.class))).thenReturn(mockDrivingLicence);

        final var actual = service.save(validDriverSocialSecurityNumber);

        assertThat(actual.getAvailablePoints()).isEqualTo(12);
        assertThat(actual.getDriverSocialSecurityNumber()).isEqualTo(validDriverSocialSecurityNumber);
        verify(generationService).generateNewDrivingLicenceId();
        verify(database).save(actual.getId(), actual);
        verifyNoMoreInteractions(database);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", "A1234567890123", "ABCDEFGHIJKLMNO", "0123", "0", "A", "444445555566666666666666666"})
    void should_not_save(String invalidDriverSocialSecurityNumber) {
        assertThatExceptionOfType(InvalidDriverSocialSecurityNumberException.class)
                .isThrownBy(() -> service.save(invalidDriverSocialSecurityNumber));
    }

}