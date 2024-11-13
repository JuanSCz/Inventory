package com.interonda.Inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{cliente.nombre.notBlank}")
    @Size(max = 50, message = "{cliente.nombre.size}")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "{cliente.pais.notBlank}")
    @Size(max = 30, message = "{cliente.pais.size}")
    @Column(nullable = false, length = 30)
    private String pais;

    @NotBlank(message = "{cliente.ciudad.notBlank}")
    @Size(max = 30, message = "{cliente.ciudad.size}")
    @Column(nullable = false, length = 30)
    private String ciudad;

    @NotBlank(message = "{cliente.direccion.notBlank}")
    @Size(max = 50, message = "{cliente.direccion.size}")
    @Column(nullable = false, length = 50)
    private String direccion;

    @NotBlank(message = "{cliente.contactoCliente.notBlank}")
    @Size(max = 15, message = "{cliente.contactoCliente.size}")
    @Column(name = "contacto", nullable = false, length = 15)
    private String contactoCliente;

    @Email(message = "{cliente.eMailCliente.email}")
    @Size(max = 254, message = "{cliente.eMailCliente.size}")
    @Column(name = "email", length = 254)
    private String eMailCliente;

    // Relaciones

    // Relación uno-a-muchos con Venta (un cliente puede tener muchas ventas)
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Venta> ventas = new ArrayList<>();

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

