package com.interonda.Inventory.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String nombre;

    @Column(nullable = false, length = 15)
    private String pais;

    @Column(nullable = false, length = 15)
    private String ciudad;

    @Column(nullable = false, length = 20)
    private String direccion;

    @Column(nullable = false, length = 20)
    private String contactoCliente;

    @Column(nullable = true, length = 20)
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

