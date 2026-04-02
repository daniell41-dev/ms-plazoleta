package com.plazoleta.ms_plazoleta.adapter;

import com.plazoleta.ms_plazoleta.domain.exception.NitDuplicadoException;
import com.plazoleta.ms_plazoleta.domain.model.Restaurante;
import com.plazoleta.ms_plazoleta.infrastructure.output.persistence.adapter.RestauranteJpaAdapter;
import com.plazoleta.ms_plazoleta.infrastructure.output.persistence.entity.RestauranteEntity;
import com.plazoleta.ms_plazoleta.infrastructure.output.persistence.mapper.IRestauranteEntityMapper;
import com.plazoleta.ms_plazoleta.infrastructure.output.persistence.repository.IRestauranteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestauranteJpaAdapterTest {

    @Mock
    private IRestauranteRepository restauranteRepository;

    @Mock
    private IRestauranteEntityMapper restauranteEntityMapper;

    private RestauranteJpaAdapter restauranteJpaAdapter;

    @BeforeEach
    void setUp() {
        restauranteJpaAdapter = new RestauranteJpaAdapter(restauranteRepository, restauranteEntityMapper);
    }

    private Restaurante restauranteValido() {
        return new Restaurante(null, "El Buen Sabor", "123456789", "Calle 10", "+573001234567", "http://logo.com", 1L);
    }

    @Test
    void guardarRestaurante_nitDuplicado_lanzaNitDuplicadoException() {
        Restaurante restaurante = restauranteValido();
        RestauranteEntity entidad = new RestauranteEntity();
        when(restauranteEntityMapper.toEntity(restaurante)).thenReturn(entidad);
        when(restauranteRepository.save(entidad)).thenThrow(new DataIntegrityViolationException("constraint violation"));

        assertThrows(NitDuplicadoException.class,
                () -> restauranteJpaAdapter.guardarRestaurante(restaurante));
    }
}
