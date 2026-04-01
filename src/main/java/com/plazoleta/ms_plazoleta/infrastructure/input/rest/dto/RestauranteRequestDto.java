package com.plazoleta.ms_plazoleta.infrastructure.input.rest.dto;

import com.plazoleta.ms_plazoleta.domain.constants.RestauranteConstantes;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El NIT es obligatorio")
    @Pattern(regexp = RestauranteConstantes.PATRON_SOLO_DIGITOS, message = "El NIT debe ser únicamente numérico")
    private String nit;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = RestauranteConstantes.LONGITUD_MAXIMA_TELEFONO, message = "El teléfono debe tener máximo 13 caracteres")
    @Pattern(regexp = RestauranteConstantes.PATRON_TELEFONO, message = "El teléfono debe ser numérico y puede iniciar con +")
    private String telefono;

    @NotBlank(message = "La URL del logo es obligatoria")
    private String urlLogo;

    @NotNull(message = "El ID del propietario es obligatorio")
    private Long propietarioId;
}
