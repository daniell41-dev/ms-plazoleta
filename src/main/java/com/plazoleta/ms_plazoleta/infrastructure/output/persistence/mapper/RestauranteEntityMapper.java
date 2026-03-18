package com.plazoleta.ms_plazoleta.infrastructure.output.persistence.mapper;

import com.plazoleta.ms_plazoleta.domain.model.Restaurante;
import com.plazoleta.ms_plazoleta.infrastructure.output.persistence.entity.RestauranteEntity;
import org.springframework.stereotype.Component;

@Component
public class RestauranteEntityMapper implements IRestauranteEntityMapper {

    @Override
    public RestauranteEntity toEntity(Restaurante restaurante) {
        return new RestauranteEntity(
                restaurante.getId(),
                restaurante.getNombre(),
                restaurante.getNit(),
                restaurante.getDireccion(),
                restaurante.getTelefono(),
                restaurante.getUrlLogo(),
                restaurante.getPropietarioId()
        );
    }

    @Override
    public Restaurante toRestaurante(RestauranteEntity restauranteEntity) {
        return new Restaurante(
                restauranteEntity.getId(),
                restauranteEntity.getNombre(),
                restauranteEntity.getNit(),
                restauranteEntity.getDireccion(),
                restauranteEntity.getTelefono(),
                restauranteEntity.getUrlLogo(),
                restauranteEntity.getPropietarioId()
        );
    }
}
