package com.plazoleta.ms_plazoleta.domain.ports.in;

import com.plazoleta.ms_plazoleta.domain.model.Plato;

public interface IPlatoServicePort {
    void guardarPlato(Plato plato, Long propietarioId);
    void actualizarPlato(Long id,Integer precio, String descripcion, Long propietarioId);
    void habilitarDeshabilitarPlato(Long id, Boolean activa, Long propietarioId);

}
