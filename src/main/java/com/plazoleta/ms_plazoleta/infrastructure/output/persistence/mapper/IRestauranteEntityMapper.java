package com.plazoleta.ms_plazoleta.infrastructure.output.persistence.mapper;

import com.plazoleta.ms_plazoleta.domain.model.Restaurante;
import com.plazoleta.ms_plazoleta.infrastructure.output.persistence.entity.RestauranteEntity;

public interface IRestauranteEntityMapper {

    RestauranteEntity toEntity(Restaurante restaurante);

    Restaurante toRestaurante(RestauranteEntity restauranteEntity);
}
