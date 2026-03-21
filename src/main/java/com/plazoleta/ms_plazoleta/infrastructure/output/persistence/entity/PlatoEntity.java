package com.plazoleta.ms_plazoleta.infrastructure.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "platos")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlatoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Integer precio;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String urlImagen;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private Boolean activa;

    @Column(name= "restaurante_id",nullable = false)
    private Long restauranteId;
}
