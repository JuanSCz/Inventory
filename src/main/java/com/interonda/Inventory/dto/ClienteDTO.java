package com.interonda.Inventory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClienteDTO {

    private Long id;

    @NotBlank(message = "{clienteDTO.nombre.notBlank}")
    @Size(max = 50, message = "{clienteDTO.nombre.size}")
    private String nombre;

    @NotBlank(message = "{clienteDTO.pais.notBlank}")
    @Size(max = 30, message = "{clienteDTO.pais.size}")
    private String pais;

    @NotBlank(message = "{clienteDTO.ciudad.notBlank}")
    @Size(max = 30, message = "{clienteDTO.ciudad.size}")
    private String ciudad;

    @NotBlank(message = "{clienteDTO.direccion.notBlank}")
    @Size(max = 50, message = "{clienteDTO.direccion.size}")
    private String direccion;

    @NotBlank(message = "{clienteDTO.contactoCliente.notBlank}")
    @Size(max = 15, message = "{clienteDTO.contactoCliente.size}")
    private String contactoCliente;

    @Email(message = "{clienteDTO.eMailCliente.email}")
    @Size(max = 254, message = "{clienteDTO.eMailCliente.size}")
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
