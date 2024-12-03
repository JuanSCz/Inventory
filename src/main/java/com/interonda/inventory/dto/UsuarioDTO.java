package com.interonda.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "{usuarioDTO.nombre.notBlank}")
    @Size(max = 50, message = "{usuarioDTO.nombre.size}")
    private String nombre;

    @NotBlank(message = "{usuarioDTO.contrasenia.notBlank}")
    @Size(min = 8, max = 50, message = "{usuarioDTO.contrasenia.size}")
    private String contrasenia;

    private byte[] imagenUsuario;
    private String contacto;
    private Long rolId;

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

    public byte[] getImagenUsuario() {
        return imagenUsuario;
    }

    public void setImagenUsuario(byte[] imagenUsuario) {
        this.imagenUsuario = imagenUsuario;
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
}