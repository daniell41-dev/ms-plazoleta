package com.plazoleta.ms_plazoleta.application.usecase;

import com.plazoleta.ms_plazoleta.domain.constants.PlatoConstantes;
import com.plazoleta.ms_plazoleta.domain.exception.NoPropietarioDelRestauranteException;
import com.plazoleta.ms_plazoleta.domain.exception.RestauranteNoEncontradoException;
import com.plazoleta.ms_plazoleta.domain.model.Plato;
import com.plazoleta.ms_plazoleta.domain.model.Restaurante;
import com.plazoleta.ms_plazoleta.domain.ports.out.IPlatoPersistencePort;
import com.plazoleta.ms_plazoleta.domain.ports.out.IRestaurantePersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatoUseCaseTest {

    @Mock
    private IPlatoPersistencePort platoPersistencePort;

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @InjectMocks
    private PlatoUseCase platoUseCase;

    private static final Long PROPIETARIO_ID = 1L;
    private static final Long RESTAURANTE_ID = 10L;
    private static final Long OTRO_PROPIETARIO_ID = 99L;

    private Plato platoValido;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        platoValido = new Plato(
                null,
                "Bandeja Paisa",
                25000,
                "Plato típico colombiano",
                "http://img.com/bandeja.png",
                "Típicos",
                RESTAURANTE_ID
        );

        restaurante = new Restaurante(
                RESTAURANTE_ID,
                "El Rincón",
                "900123456",
                "Calle 10 #5-20",
                "+573001234567",
                "http://logo.com/img.png",
                PROPIETARIO_ID
        );
    }

    // ─── Camino feliz ────────────────────────────────────────────────────────

    @Test
    void guardarPlato_cuandoTodosLosDatosSonValidos_guardaCorrectamente() {
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.of(restaurante));

        platoUseCase.guardarPlato(platoValido, PROPIETARIO_ID);

        verify(platoPersistencePort, times(1)).guardarPlato(platoValido);
    }

    // ─── Validación de restaurante ───────────────────────────────────────────

    @Test
    void guardarPlato_cuandoRestauranteNoExiste_lanzaRestauranteNoEncontradoException() {
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.empty());

        assertThrows(RestauranteNoEncontradoException.class,
                () -> platoUseCase.guardarPlato(platoValido, PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    // ─── Validación de propietario ───────────────────────────────────────────

    @Test
    void guardarPlato_cuandoPropietarioNoEsDelRestaurante_lanzaNoPropietarioDelRestauranteException() {
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.of(restaurante));

        assertThrows(NoPropietarioDelRestauranteException.class,
                () -> platoUseCase.guardarPlato(platoValido, OTRO_PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    // ─── Validación de precio ────────────────────────────────────────────────

    @Test
    void guardarPlato_cuandoPrecioEsCero_lanzaIllegalArgumentException() {
        Plato platoConPrecioCero = new Plato(
                null, "Bandeja Paisa", PlatoConstantes.PRECIO_MINIMO,
                "Descripción", "http://img.com/img.png", "Típicos", RESTAURANTE_ID
        );
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.of(restaurante));

        assertThrows(IllegalArgumentException.class,
                () -> platoUseCase.guardarPlato(platoConPrecioCero, PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void guardarPlato_cuandoPrecioEsNegativo_lanzaIllegalArgumentException() {
        Plato platoConPrecioNegativo = new Plato(
                null, "Bandeja Paisa", -1,
                "Descripción", "http://img.com/img.png", "Típicos", RESTAURANTE_ID
        );
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.of(restaurante));

        assertThrows(IllegalArgumentException.class,
                () -> platoUseCase.guardarPlato(platoConPrecioNegativo, PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }
}
