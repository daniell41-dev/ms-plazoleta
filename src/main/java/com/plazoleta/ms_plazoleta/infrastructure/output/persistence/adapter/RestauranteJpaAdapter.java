package com.plazoleta.ms_plazoleta.infrastructure.output.persistence.adapter;

import lombok.RequiredArgsConstructor;
import com.plazoleta.ms_plazoleta.domain.model.Restaurante;
import com.plazoleta.ms_plazoleta.domain.ports.out.IRestaurantePersistencePort;
import com.plazoleta.ms_plazoleta.infrastructure.output.persistence.mapper.IRestauranteEntityMapper;
import com.plazoleta.ms_plazoleta.infrastructure.output.persistence.repository.IRestauranteRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestauranteJpaAdapter implements IRestaurantePersistencePort {

    private final IRestauranteRepository restauranteRepository;
    private final IRestauranteEntityMapper restauranteEntityMapper;

    @Override
    public void guardarRestaurante(Restaurante restaurante) {
        restauranteRepository.save(restauranteEntityMapper.toEntity(restaurante));
    }
}
