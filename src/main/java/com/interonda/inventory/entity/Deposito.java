package com.interonda.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "depositos")
public class Deposito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "La provincia no puede estar vacía")
    @Size(max = 20, message = "La provincia no puede tener más de 20 caracteres")
    @Column(nullable = false, length = 20)
    private String provincia;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(max = 50, message = "La dirección no puede tener más de 50 caracteres")
    @Column(nullable = false, length = 50)
    private String direccion;

    @NotBlank(message = "El contacto no puede estar vacío")
    @Size(max = 20, message = "El contacto no puede tener más de 20 caracteres")
    @Column(name = "contacto", nullable = false, length = 20)
    private String contactoDeposito;

    // Relaciones

    // Relación uno-a-muchos con Stock (un depósito puede tener muchos stocks)
    @OneToMany(mappedBy = "deposito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stock> stocks;

    // Constructor vacío requerido por JPA
    public Deposito() {
        this.stocks = new ArrayList<>();
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

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public String getContactoDeposito() {
        return contactoDeposito;
    }

    public void setContactoDeposito(String contactoDeposito) {
        this.contactoDeposito = contactoDeposito;
    }
}
