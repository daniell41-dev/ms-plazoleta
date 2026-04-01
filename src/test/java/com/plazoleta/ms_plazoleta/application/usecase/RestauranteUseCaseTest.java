package com.plazoleta.ms_plazoleta.application.usecase;

import com.plazoleta.ms_plazoleta.domain.exception.NoPropietarioException;
import com.plazoleta.ms_plazoleta.domain.model.Restaurante;
import com.plazoleta.ms_plazoleta.domain.ports.out.IRestaurantePersistencePort;
import com.plazoleta.ms_plazoleta.domain.ports.out.IUsuarioServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

    @InjectMocks
    private RestauranteUseCase restauranteUseCase;

    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante(1L, "Mi Restaurante", "123456789", "Calle 1", "+571234567", "logo.jpg", 10L);
    }

    @Test
    void guardarRestaurante_exitoso() {
        when(usuarioServicePort.obtenerRolUsuario(10L)).thenReturn("PROPIETARIO");

        restauranteUseCase.guardarRestaurante(restaurante);

        verify(restaurantePersistencePort).guardarRestaurante(restaurante);
    }

    @Test
    void guardarRestaurante_noPropietario_lanzaExcepcion() {
        when(usuarioServicePort.obtenerRolUsuario(10L)).thenReturn("ADMINISTRADOR");

        assertThrows(NoPropietarioException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_nitConLetras_lanzaExcepcion() {
        Restaurante conNitInvalido = new Restaurante(1L, "Mi Restaurante", "NIT-123", "Calle 1", "+571234567", "logo.jpg", 10L);
        when(usuarioServicePort.obtenerRolUsuario(10L)).thenReturn("PROPIETARIO");

        assertThrows(IllegalArgumentException.class,
                () -> restauranteUseCase.guardarRestaurante(conNitInvalido));

        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_telefonoInvalido_lanzaExcepcion() {
        Restaurante conTelefonoInvalido = new Restaurante(1L, "Mi Restaurante", "123456789", "Calle 1", "abc123", "logo.jpg", 10L);
        when(usuarioServicePort.obtenerRolUsuario(10L)).thenReturn("PROPIETARIO");

        assertThrows(IllegalArgumentException.class,
                () -> restauranteUseCase.guardarRestaurante(conTelefonoInvalido));

        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_telefonoDemasiadoLargo_lanzaExcepcion() {
        Restaurante conTelefonoLargo = new Restaurante(1L, "Mi Restaurante", "123456789", "Calle 1", "99999999999999", "logo.jpg", 10L);
        when(usuarioServicePort.obtenerRolUsuario(10L)).thenReturn("PROPIETARIO");

        assertThrows(IllegalArgumentException.class,
                () -> restauranteUseCase.guardarRestaurante(conTelefonoLargo));

        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_nombreSoloNumeros_lanzaExcepcion() {
        Restaurante conNombreNumerico = new Restaurante(1L, "12345", "123456789", "Calle 1", "+571234567", "logo.jpg", 10L);
        when(usuarioServicePort.obtenerRolUsuario(10L)).thenReturn("PROPIETARIO");

        assertThrows(IllegalArgumentException.class,
                () -> restauranteUseCase.guardarRestaurante(conNombreNumerico));

        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }
}
