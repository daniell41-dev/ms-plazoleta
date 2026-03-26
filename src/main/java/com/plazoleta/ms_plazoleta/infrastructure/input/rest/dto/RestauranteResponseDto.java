package com.plazoleta.ms_plazoleta.infrastructure.input.rest.dto;

public class RestauranteResponseDto {

    private String nombre;
    private String urlLogo;

    public RestauranteResponseDto(String nombre, String urlLogo) {
        this.nombre = nombre;
        this.urlLogo = urlLogo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrlLogo() {
        return urlLogo;
    }
}
