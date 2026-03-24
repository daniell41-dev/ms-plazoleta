package com.plazoleta.ms_plazoleta.infrastructure.input.rest.dto;

import jakarta.validation.constraints.NotNull;

public class PlatoHabilitarRequestDto {

    @NotNull(message = "El campo activa es obligatorio")
    private Boolean activa;

    public Boolean getActiva() {
        return activa;
    }
}
