package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.exception.ResourceNotFoundException;
import fr.esgi.cleancode.model.DrivingLicence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DrivingLicenceRemovePointsServiceTest {

    @InjectMocks
    private DrivingLicenceRemovePointsService service;

    @Mock
    private InMemoryDatabase database;

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 })
    void should_remove_points(int pointsToRemove) {
        final var id = UUID.randomUUID();
        final var given = DrivingLicence.builder().id(id).build();
        when(database.findById(id)).thenReturn(Optional.of(given));

        final var actual = service.removePoints(id, pointsToRemove);

        assertThat(actual.getAvailablePoints()).isEqualTo(12 - pointsToRemove);
    }

    @ParameterizedTest
    @ValueSource(ints = { 12, 13, 14, 15 })
    void should_put_points_to_0(int pointsToRemove) {
        final var id = UUID.randomUUID();
        final var given = DrivingLicence.builder().id(id).build();
        when(database.findById(id)).thenReturn(Optional.of(given));

        final var actual = service.removePoints(id, pointsToRemove);

        assertThat(actual.getAvailablePoints()).isEqualTo(0);
    }

    @Test
    void should_not_remove_points() {
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> service.removePoints(UUID.randomUUID(), 0));
    }

}