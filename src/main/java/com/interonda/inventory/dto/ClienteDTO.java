package com.interonda.inventory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClienteDTO {

    private Long id;


    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;

    @NotBlank(message = "El país no puede estar vacío")
    @Size(max = 30, message = "El país no puede tener más de 30 caracteres")
    private String pais;

    @NotBlank(message = "La ciudad no puede estar vacía")
    @Size(max = 30, message = "La ciudad no puede tener más de 30 caracteres")
    private String ciudad;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(max = 50, message = "La dirección no puede tener más de 50 caracteres")
    private String direccion;

    @NotBlank(message = "El contacto no puede estar vacío")
    @Size(max = 15, message = "El contacto no puede tener más de 15 caracteres")
    private String contactoCliente;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El correo electrónico debe ser válido")
    @Size(max = 254, message = "El correo electrónico no puede tener más de 254 caracteres")
    private String eMailCliente;

    public ClienteDTO() {
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

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContactoCliente() {
        return contactoCliente;
    }

    public void setContactoCliente(String contactoCliente) {
        this.contactoCliente = contactoCliente;
    }

    public String geteMailCliente() {
        return eMailCliente;
    }

    public void seteMailCliente(String eMailCliente) {
        this.eMailCliente = eMailCliente;
    }
}
