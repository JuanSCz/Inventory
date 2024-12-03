package com.interonda.inventory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public class ProveedorDTO {

    private Long id;

    @NotBlank(message = "{proveedorDTO.nombre.notBlank}")
    @Size(max = 50, message = "{proveedorDTO.nombre.size}")
    private String nombre;

    @Size(max = 15, message = "{proveedorDTO.contactoProveedor.size}")
    private String contactoProveedor;

    @Size(max = 50, message = "{proveedorDTO.direccion.size}")
    private String direccion;

    @Length(max = 254, message = "{proveedorDTO.pais.length}")
    private String pais;

    @Size(max = 254, message = "{proveedorDTO.emailProveedor.size}")
    @Email(message = "{proveedorDTO.emailProveedor.email}")
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