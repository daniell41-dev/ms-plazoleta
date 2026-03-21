package com.plazoleta.ms_plazoleta.infrastructure.input.rest;

import com.plazoleta.ms_plazoleta.domain.ports.in.IPlatoServicePort;
import com.plazoleta.ms_plazoleta.infrastructure.input.rest.dto.PlatoActualizarRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/platos")
public class PlatoOperacionesRestController {

    private final IPlatoServicePort platoServicePort;

    public PlatoOperacionesRestController(IPlatoServicePort platoServicePort) {
        this.platoServicePort = platoServicePort;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> actualizarPlato(@PathVariable Long id,
                                                @Valid @RequestBody PlatoActualizarRequestDto dto) {
        platoServicePort.actualizarPlato(id, dto.getPrecio(), dto.getDescripcion(), dto.getPropietarioId());
        return ResponseEntity.ok().build();
    }

}
