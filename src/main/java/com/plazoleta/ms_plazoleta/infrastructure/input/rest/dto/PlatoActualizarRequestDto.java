package com.plazoleta.ms_plazoleta.infrastructure.input.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlatoActualizarRequestDto {

    @Positive(message = "El precio debe ser mayor a 0")
    private Integer precio;

    private String descripcion;

    @NotNull(message = "El propietario es obligatorio")
    private Long propietarioId;
}
