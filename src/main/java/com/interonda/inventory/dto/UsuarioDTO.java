package com.interonda.inventory.dto;

import com.interonda.inventory.entity.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre debe tener un máximo de 50 caracteres")
    private String nombre;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String contrasenia;

    @NotBlank(message = "El contacto no puede estar vacío")
    @Size(max = 15, message = "El contacto debe tener un máximo de 15 caracteres")
    private String contacto;

    @NotNull(message = "El ID del rol no puede ser nulo")
    private Long rolId;

    private String rolNombre;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.contrasenia = usuario.getContrasenia();
        this.contacto = usuario.getContacto();
        this.rolId = usuario.getRol().getId();
        this.rolNombre = usuario.getRol().getNombre();
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