package com.plazoleta.ms_plazoleta.infrastructure.input.rest.mapper;

import com.plazoleta.ms_plazoleta.domain.model.Plato;
import com.plazoleta.ms_plazoleta.infrastructure.input.rest.dto.PlatoRequestDto;
import org.springframework.stereotype.Component;

@Component
public class PlatoRequestMapper {

    public Plato toDomain(PlatoRequestDto platoRequestDto, Long restauranteId) {
        return new Plato(
                null,
                platoRequestDto.getNombre(),
                platoRequestDto.getPrecio(),
                platoRequestDto.getDescripcion(),
                platoRequestDto.getUrlImagen(),
                platoRequestDto.getCategoria(),
                restauranteId
        );
    }

}
