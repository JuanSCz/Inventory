package com.interonda.Inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "El país no puede estar vacío")
    @Size(max = 30, message = "El país no puede tener más de 30 caracteres")
    @Column(nullable = false, length = 30)
    private String pais;

    @NotBlank(message = "La ciudad no puede estar vacía")
    @Size(max = 30, message = "La ciudad no puede tener más de 30 caracteres")
    @Column(nullable = false, length = 30)
    private String ciudad;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(max = 50, message = "La dirección no puede tener más de 50 caracteres")
    @Column(nullable = false, length = 50)
    private String direccion;

    @NotBlank(message = "El contacto no puede estar vacío")
    @Size(max = 15, message = "El contacto no puede tener más de 15 caracteres")
    @Column(name = "contacto", nullable = false, length = 15)
    private String contactoCliente;

    @Email(message = "El e-mail debe ser válido")
    @Size(max = 254, message = "El e-mail no puede tener más de 254 caracteres")
    @Column(name = "email", length = 254)
    private String eMailCliente;

    // Relaciones

    // Relación uno-a-muchos con Venta
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Venta> ventas;

    // Constructor vacío requerido por JPA
    public Cliente() {
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

    public List<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(List<Venta> ventas) {
        this.ventas = ventas;
    }
}

