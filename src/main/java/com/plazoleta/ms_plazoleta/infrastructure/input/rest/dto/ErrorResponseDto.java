package com.plazoleta.ms_plazoleta.infrastructure.input.rest.dto;

public class ErrorResponseDto {

    private int status;
    private String mensaje;

    public ErrorResponseDto(int status, String mensaje) {
        this.status = status;
        this.mensaje = mensaje;
    }

    public int getStatus() {
        return status;
    }

    public String getMensaje() {
        return mensaje;
    }
}
