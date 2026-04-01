package com.plazoleta.ms_plazoleta.domain.constants;

public final class RestauranteConstantes {

    private RestauranteConstantes() {}

    public static final String ROL_PROPIETARIO = "PROPIETARIO";

    /**
     * Uno o más dígitos (0-9). Sin letras, sin espacios, sin símbolos.
     * Usado para validar NIT y nombres que no deben ser solo numéricos.
     */
    public static final String PATRON_SOLO_DIGITOS = "\\d+";

    /**
     * Número de teléfono: permite un + opcional al inicio, seguido de dígitos.
     * Ejemplos válidos: +573001234567, 3001234567
     */
    public static final String PATRON_TELEFONO = "^\\+?\\d+$";

    /**
     * Longitud máxima de un número de teléfono (con el + incluido).
     * +573001234567 = 13 caracteres.
     */
    public static final int LONGITUD_MAXIMA_TELEFONO = 13;
}
