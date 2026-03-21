package com.plazoleta.ms_plazoleta.domain.model;

public class Plato {
    private Long id;
    private String nombre;
    private Integer precio;
    private String descripcion;
    private String urlImagen;
    private String categoria;
    private Boolean activa = true;
    private Long restauranteId;

    public Plato(Long id, String nombre, Integer precio, String descripcion, String urlImagen, String categoria, Long restauranteId) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
        this.categoria = categoria;
        this.restauranteId = restauranteId;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getPrecio() {
        return precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public Boolean getActiva() {
        return activa;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
