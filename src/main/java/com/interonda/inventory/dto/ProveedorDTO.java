package com.interonda.inventory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public class ProveedorDTO {

    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre debe tener un máximo de 50 caracteres")
    private String nombre;

    @NotBlank(message = "El contacto no puede estar vacío")
    @Size(max = 15, message = "El contacto debe tener un máximo de 15 caracteres")
    private String contactoProveedor;

    @NotBlank(message = "El dirección no puede estar vacío")
    @Size(max = 50, message = "La dirección debe tener un máximo de 50 caracteres")
    private String direccion;

    @NotBlank(message = "El país no puede estar vacío")
    @Size(max = 254, message = "El país debe tener un máximo de 254 caracteres")
    private String pais;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    @Size(max = 254, message = "El email debe tener un máximo de 254 caracteres")
    private String emailProveedor;

    private List<Long> productosIds;

    private List<Long> comprasIds;

    public ProveedorDTO() {
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

    public String getContactoProveedor() {
        return contactoProveedor;
    }

    public void setContactoProveedor(String contactoProveedor) {
        this.contactoProveedor = contactoProveedor;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getEmailProveedor() {
        return emailProveedor;
    }

    public void setEmailProveedor(String emailProveedor) {
        this.emailProveedor = emailProveedor;
    }

    public List<Long> getProductosIds() {
        return productosIds;
    }

    public void setProductosIds(List<Long> productosIds) {
        this.productosIds = productosIds;
    }

    public List<Long> getComprasIds() {
        return comprasIds;
    }

    public void setComprasIds(List<Long> comprasIds) {
        this.comprasIds = comprasIds;
    }
}