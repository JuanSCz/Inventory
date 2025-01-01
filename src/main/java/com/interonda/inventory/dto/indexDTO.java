package com.interonda.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class indexDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotEmpty(message = "La contraseña no puede estar vacía")
    private String contrasenia;

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
