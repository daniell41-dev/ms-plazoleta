package com.plazoleta.ms_plazoleta.infrastructure.output.restclient;

import com.plazoleta.ms_plazoleta.domain.ports.out.IUsuarioServicePort;
import com.plazoleta.ms_plazoleta.infrastructure.output.restclient.dto.UsuarioRolResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UsuarioRestClientAdapter implements IUsuarioServicePort {

    private final RestClient restClient;

    public UsuarioRestClientAdapter() {
        this.restClient = RestClient.create("http://localhost:8080");
    }

    @Override
    public String obtenerRolUsuario(Long id) {
        UsuarioRolResponseDto response = restClient.get()
                .uri("/usuarios/{id}/rol", id)
                .retrieve()
                .body(UsuarioRolResponseDto.class);
        return response.getRol();
    }
}
