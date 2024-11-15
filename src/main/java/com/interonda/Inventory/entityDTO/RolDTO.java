package com.interonda.Inventory.entityDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RolDTO {

    private Long id;

    @Size(max = 50, message = "{rolDTO.nombre.size}")
    @NotBlank(message = "{rolDTO.nombre.notBlank}")
    @Column(nullable = false, length = 50)
    private String nombre;

    public RolDTO() {
    }

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
}