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
                                                @Valid @RequestBody PlatoActualizarRequestDto dto) {
        Long propietarioId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        platoServicePort.actualizarPlato(id, dto.getPrecio(), dto.getDescripcion(), propietarioId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> habilitarDeshabilitarPlato(@PathVariable Long id,
                                                           @Valid @RequestBody PlatoHabilitarRequestDto dto) {
        Long propietarioId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        platoServicePort.habilitarDeshabilitarPlato(id, dto.getActiva(), propietarioId);
        return ResponseEntity.ok().build();
    }

}
