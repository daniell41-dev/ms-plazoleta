package com.plazoleta.ms_plazoleta.infrastructure.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.plazoleta.ms_plazoleta.domain.ports.in.IRestauranteServicePort;
import com.plazoleta.ms_plazoleta.infrastructure.input.rest.dto.RestauranteRequestDto;
import com.plazoleta.ms_plazoleta.infrastructure.input.rest.mapper.RestauranteRequestMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurantes")
@RequiredArgsConstructor
public class RestauranteRestController {

    private final IRestauranteServicePort restauranteServicePort;
    private final RestauranteRequestMapper restauranteRequestMapper;

    @PostMapping
    public ResponseEntity<Void> guardarRestaurante(@Valid @RequestBody RestauranteRequestDto restauranteRequestDto) {
        restauranteServicePort.guardarRestaurante(
                restauranteRequestMapper.toDomain(restauranteRequestDto)
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
