package com.plazoleta.ms_plazoleta.infrastructure.input.rest.mapper;

import com.plazoleta.ms_plazoleta.domain.model.Restaurante;
import com.plazoleta.ms_plazoleta.infrastructure.input.rest.dto.RestauranteRequestDto;
import org.springframework.stereotype.Component;

@Component
public class RestauranteRequestMapper {

    public Restaurante toDomain(RestauranteRequestDto dto) {
        return new Restaurante(
                null,
                dto.getNombre(),
                dto.getNit(),
                dto.getDireccion(),
                dto.getTelefono(),
                dto.getUrlLogo(),
                dto.getPropietarioId()
        );
    }
}
