package com.interonda.Inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoriaDTO {
    private Long id;

    @NotBlank(message = "{categoriaDTO.nombre.notBlank}")
    @Size(max = 50, message = "{categoriaDTO.nombre.size}")
    private String nombre;

    @Size(max = 75, message = "{categoriaDTO.descripcion.size}")
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

