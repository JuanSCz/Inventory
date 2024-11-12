package com.interonda.Inventory.util;

import com.interonda.Inventory.exceptions.BadRequestException;

public class ValidationUtil {
    public static void validarCantidadPositiva(Integer cantidad) {
        if (cantidad == null || cantidad < 0) {
            throw new BadRequestException("La cantidad debe ser un número positivo.");
        }
    }
}