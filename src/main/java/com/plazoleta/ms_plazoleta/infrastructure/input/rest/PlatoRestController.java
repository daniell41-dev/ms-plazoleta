package com.plazoleta.ms_plazoleta.infrastructure.input.rest;

import com.plazoleta.ms_plazoleta.domain.ports.in.IPlatoServicePort;
import com.plazoleta.ms_plazoleta.infrastructure.input.rest.dto.PlatoRequestDto;
import com.plazoleta.ms_plazoleta.infrastructure.input.rest.mapper.PlatoRequestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurantes")
public class PlatoRestController {

    private final IPlatoServicePort platoServicePort;
    private final PlatoRequestMapper platoRequestMapper;

    public PlatoRestController(IPlatoServicePort platoServicePort, PlatoRequestMapper platoRequestMapper) {
        this.platoServicePort = platoServicePort;
        this.platoRequestMapper = platoRequestMapper;
    }

    @PostMapping("/{restauranteId}/platos")
    public ResponseEntity<Void> crearPlato(@PathVariable Long restauranteId,
                                           @Valid @RequestBody PlatoRequestDto platoRequestDto) {
        platoServicePort.guardarPlato(
                platoRequestMapper.toDomain(platoRequestDto, restauranteId),
                platoRequestDto.getPropietarioId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
