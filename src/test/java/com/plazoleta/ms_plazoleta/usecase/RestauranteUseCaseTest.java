package com.plazoleta.ms_plazoleta.usecase;

import com.plazoleta.ms_plazoleta.application.usecase.RestauranteUseCase;
import com.plazoleta.ms_plazoleta.domain.exception.NoPropietarioException;
import com.plazoleta.ms_plazoleta.domain.model.Restaurante;
import com.plazoleta.ms_plazoleta.domain.ports.out.IRestaurantePersistencePort;
import com.plazoleta.ms_plazoleta.domain.ports.out.IUsuarioServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestauranteUseCaseTest {

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @Mock
    private IUsuarioServicePort usuarioServicePort;

    private RestauranteUseCase restauranteUseCase;

    @BeforeEach
    void setUp() {
        restauranteUseCase = new RestauranteUseCase(restaurantePersistencePort, usuarioServicePort);
    }

    private Restaurante restauranteValido() {
        return new Restaurante(null, "El Buen Sabor", "123456789", "Calle 10", "+573001234567", "http://logo.com", 1L);
    }

    @Test
    void guardarRestaurante_exitoso() {
        Restaurante restaurante = restauranteValido();
        when(usuarioServicePort.obtenerRolUsuario(1L)).thenReturn("PROPIETARIO");

        assertDoesNotThrow(() -> restauranteUseCase.guardarRestaurante(restaurante));
        verify(restaurantePersistencePort).guardarRestaurante(restaurante);
    }

    @Test
    void guardarRestaurante_usuarioNoEsPropietario_lanzaExcepcion() {
        Restaurante restaurante = restauranteValido();
        when(usuarioServicePort.obtenerRolUsuario(1L)).thenReturn("CLIENTE");

        assertThrows(NoPropietarioException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));
        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_nitConLetras_lanzaExcepcion() {
        Restaurante restaurante = new Restaurante(null, "El Buen Sabor", "123ABC", "Calle 10", "+573001234567", "http://logo.com", 1L);
        when(usuarioServicePort.obtenerRolUsuario(1L)).thenReturn("PROPIETARIO");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));
        assertEquals("El NIT debe ser únicamente numérico", ex.getMessage());
    }

    @Test
    void guardarRestaurante_telefonoConCaracteresInvalidos_lanzaExcepcion() {
        Restaurante restaurante = new Restaurante(null, "El Buen Sabor", "123456789", "Calle 10", "abc-123", "http://logo.com", 1L);
        when(usuarioServicePort.obtenerRolUsuario(1L)).thenReturn("PROPIETARIO");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));
        assertEquals("El teléfono debe ser únicamente numérico", ex.getMessage());
    }

    @Test
    void guardarRestaurante_telefonoMuyLargo_lanzaExcepcion() {
        Restaurante restaurante = new Restaurante(null, "El Buen Sabor", "123456789", "Calle 10", "12345678901234", "http://logo.com", 1L);
        when(usuarioServicePort.obtenerRolUsuario(1L)).thenReturn("PROPIETARIO");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));
        assertEquals("El teléfono no puede tener más de 13 caracteres", ex.getMessage());
    }

    @Test
    void guardarRestaurante_nombreSoloNumeros_lanzaExcepcion() {
        Restaurante restaurante = new Restaurante(null, "12345", "123456789", "Calle 10", "+573001234567", "http://logo.com", 1L);
        when(usuarioServicePort.obtenerRolUsuario(1L)).thenReturn("PROPIETARIO");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));
        assertEquals("El nombre no puede ser únicamente numérico", ex.getMessage());
    }
}
