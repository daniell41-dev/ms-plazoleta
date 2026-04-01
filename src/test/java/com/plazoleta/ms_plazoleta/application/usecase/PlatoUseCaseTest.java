package com.plazoleta.ms_plazoleta.application.usecase;

import com.plazoleta.ms_plazoleta.domain.constants.PlatoConstantes;
import com.plazoleta.ms_plazoleta.domain.exception.NoPropietarioDelRestauranteException;
import com.plazoleta.ms_plazoleta.domain.exception.PlatoNoEncontradoException;
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

    private static final Long PLATO_ID = 1L;
    private static final Long RESTAURANTE_ID = 10L;
    private static final Long PROPIETARIO_ID = 5L;
    private static final Long OTRO_PROPIETARIO_ID = 99L;

    private Plato plato;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        plato = new Plato(
                PLATO_ID,
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

    // ═══════════════════════════════════════════════════════════════════════
    // guardarPlato
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    void guardarPlato_cuandoTodosLosDatosSonValidos_guardaCorrectamente() {
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.of(restaurante));

        platoUseCase.guardarPlato(plato, PROPIETARIO_ID);

        verify(platoPersistencePort, times(1)).guardarPlato(plato);
    }

    @Test
    void guardarPlato_cuandoRestauranteNoExiste_lanzaRestauranteNoEncontradoException() {
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.empty());

        assertThrows(RestauranteNoEncontradoException.class,
                () -> platoUseCase.guardarPlato(plato, PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void guardarPlato_cuandoPropietarioNoEsDelRestaurante_lanzaNoPropietarioDelRestauranteException() {
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID))
                .thenReturn(Optional.of(restaurante));

        assertThrows(NoPropietarioDelRestauranteException.class,
                () -> platoUseCase.guardarPlato(plato, OTRO_PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

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

    // ═══════════════════════════════════════════════════════════════════════
    // actualizarPlato
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    void actualizarPlato_cuandoPlatoNoExiste_lanzaPlatoNoEncontradoException() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.empty());

        assertThrows(PlatoNoEncontradoException.class,
                () -> platoUseCase.actualizarPlato(PLATO_ID, 30000, "Nueva desc", PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void actualizarPlato_cuandoRestauranteNoExiste_lanzaRestauranteNoEncontradoException() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.empty());

        assertThrows(RestauranteNoEncontradoException.class,
                () -> platoUseCase.actualizarPlato(PLATO_ID, 30000, "Nueva desc", PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void actualizarPlato_cuandoPropietarioNoEsDelRestaurante_lanzaNoPropietarioDelRestauranteException() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.of(restaurante));

        assertThrows(NoPropietarioDelRestauranteException.class,
                () -> platoUseCase.actualizarPlato(PLATO_ID, 30000, "Nueva desc", OTRO_PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void actualizarPlato_cuandoPrecioEsCero_lanzaIllegalArgumentException() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.of(restaurante));

        assertThrows(IllegalArgumentException.class,
                () -> platoUseCase.actualizarPlato(PLATO_ID, PlatoConstantes.PRECIO_MINIMO, null, PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void actualizarPlato_cuandoPrecioEsNegativo_lanzaIllegalArgumentException() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.of(restaurante));

        assertThrows(IllegalArgumentException.class,
                () -> platoUseCase.actualizarPlato(PLATO_ID, -1, null, PROPIETARIO_ID));

        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void actualizarPlato_cuandoSoloActualizaPrecio_guardaConNuevoPrecio() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.of(restaurante));

        platoUseCase.actualizarPlato(PLATO_ID, 30000, null, PROPIETARIO_ID);

        assertEquals(30000, plato.getPrecio());
        verify(platoPersistencePort, times(1)).guardarPlato(plato);
    }

    @Test
    void actualizarPlato_cuandoSoloActualizaDescripcion_guardaConNuevaDescripcion() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.of(restaurante));

        platoUseCase.actualizarPlato(PLATO_ID, null, "Nueva descripción", PROPIETARIO_ID);

        assertEquals("Nueva descripción", plato.getDescripcion());
        verify(platoPersistencePort, times(1)).guardarPlato(plato);
    }

    @Test
    void actualizarPlato_cuandoActualizaPrecioYDescripcion_guardaAmbosCambios() {
        when(platoPersistencePort.buscarPlatoPorId(PLATO_ID)).thenReturn(Optional.of(plato));
        when(restaurantePersistencePort.buscarRestaurantePorId(RESTAURANTE_ID)).thenReturn(Optional.of(restaurante));

        platoUseCase.actualizarPlato(PLATO_ID, 35000, "Nueva descripción", PROPIETARIO_ID);

        assertEquals(35000, plato.getPrecio());
        assertEquals("Nueva descripción", plato.getDescripcion());
        verify(platoPersistencePort, times(1)).guardarPlato(plato);
    }
}
