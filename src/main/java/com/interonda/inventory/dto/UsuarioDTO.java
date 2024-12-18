package com.interonda.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "{usuarioDTO.nombre.notBlank}")
    @Size(max = 50, message = "{usuarioDTO.nombre.size}")
    private String nombre;

    @NotBlank(message = "{usuarioDTO.contrasenia.notBlank}")
    @Size(min = 8, max = 20, message = "{usuarioDTO.contrasenia.size}")
    private String contrasenia;

    @NotBlank(message = "{usuarioDTO.contacto.notBlank}")
    private String contacto;

    private Long rolId;

    private String rolNombre;

    public UsuarioDTO() {
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

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }

    public String getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }
}