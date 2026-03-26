package com.plazoleta.ms_plazoleta.infrastructure.input.rest.mapper;

import com.plazoleta.ms_plazoleta.domain.model.Restaurante;
import com.plazoleta.ms_plazoleta.infrastructure.input.rest.dto.RestauranteResponseDto;
import org.springframework.stereotype.Component;

@Component
public class RestauranteResponseMapper {

    public RestauranteResponseDto toDto(Restaurante restaurante) {
        return new RestauranteResponseDto(
                restaurante.getNombre(),
                restaurante.getUrlLogo()
        );
    }
}
