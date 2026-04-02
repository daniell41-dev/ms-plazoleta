package com.plazoleta.ms_plazoleta.infrastructure.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.plazoleta.ms_plazoleta.domain.ports.in.IRestauranteServicePort;
import com.plazoleta.ms_plazoleta.infrastructure.input.rest.constants.PaginacionConstants;
import com.plazoleta.ms_plazoleta.infrastructure.input.rest.dto.RestauranteRequestDto;
import com.plazoleta.ms_plazoleta.infrastructure.input.rest.dto.RestauranteResponseDto;
import com.plazoleta.ms_plazoleta.infrastructure.input.rest.mapper.RestauranteRequestMapper;
import com.plazoleta.ms_plazoleta.infrastructure.input.rest.mapper.RestauranteResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/restaurantes")
@RequiredArgsConstructor
public class RestauranteRestController {

    private final IRestauranteServicePort restauranteServicePort;
    private final RestauranteRequestMapper restauranteRequestMapper;
    private final RestauranteResponseMapper restauranteResponseMapper;

    @PostMapping
    public ResponseEntity<Void> guardarRestaurante(@Valid @RequestBody RestauranteRequestDto restauranteRequestDto) {
        restauranteServicePort.guardarRestaurante(
                restauranteRequestMapper.toDomain(restauranteRequestDto)
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<RestauranteResponseDto>> listarRestaurantes(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = PaginacionConstants.TAMANO_PAGINA_DEFAULT) int tamano) {
        if (pagina < 0) {
            throw new IllegalArgumentException("El número de página no puede ser negativo");
        }
        if (tamano < PaginacionConstants.TAMANO_PAGINA_MINIMO || tamano > PaginacionConstants.TAMANO_PAGINA_MAXIMO) {
            throw new IllegalArgumentException("El tamaño de página debe estar entre 1 y 50");
        }
        List<RestauranteResponseDto> respuesta = restauranteServicePort
                .listarRestaurantes(pagina, tamano)
                .stream()
                .map(restauranteResponseMapper::toDto)
                .toList();
        return ResponseEntity.ok(respuesta);
    }
}
