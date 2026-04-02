package com.plazoleta.ms_plazoleta.infrastructure.input.rest;

import com.plazoleta.ms_plazoleta.domain.ports.in.IPlatoServicePort;
import com.plazoleta.ms_plazoleta.infrastructure.input.rest.dto.PlatoActualizarRequestDto;
import com.plazoleta.ms_plazoleta.infrastructure.input.rest.dto.PlatoHabilitarRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
                                                @Valid @RequestBody PlatoActualizarRequestDto platoActualizarDto) {
        Long propietarioId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        platoServicePort.actualizarPlato(id, platoActualizarDto.getPrecio(), platoActualizarDto.getDescripcion(), propietarioId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> habilitarDeshabilitarPlato(@PathVariable Long id,
                                                           @Valid @RequestBody PlatoHabilitarRequestDto platoHabilitarDto) {
        Long propietarioId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        platoServicePort.habilitarDeshabilitarPlato(id, platoHabilitarDto.getActiva(), propietarioId);
        return ResponseEntity.ok().build();
    }

}
