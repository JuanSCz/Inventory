package com.interonda.inventory.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoriaDTO {

    private Long id;

    @NotBlank(message = "El nombre de la categoria no puede estar vacío")
    @Size(max = 50, message = "El nombre de la categoria no puede tener más de 50 caracteres")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "La descripcion de la categoria no puede estar vacía")
    @Size(max = 75, message = "La descripción de la categoria no puede tener más de 75 caracteres")
    @Column(length = 75)
    private String descripcion;

    public CategoriaDTO() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

