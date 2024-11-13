package com.interonda.Inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "depositos")
public class Deposito {

    // Atributos y anotaciones de validación
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{deposito.nombre.notBlank}")
    @Size(max = 50, message = "{deposito.nombre.size}")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "{deposito.provincia.notBlank}")
    @Size(max = 20, message = "{deposito.provincia.size}")
    @Column(nullable = false, length = 20)
    private String provincia;

    @NotBlank(message = "{deposito.direccion.notBlank}")
    @Size(max = 20, message = "{deposito.direccion.size}")
    @Column(nullable = false, length = 20)
    private String direccion;

    @NotBlank(message = "{deposito.contactoDeposito.notBlank}")
    @Size(max = 15, message = "{deposito.contactoDeposito.size}")
    @Column(name = "contacto", length = 15, nullable = false)
    private String contactoDeposito;

    // Relaciones

    // Relación uno-a-muchos con Stock (un depósito puede tener muchos stocks)
    @OneToMany(mappedBy = "deposito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stock> stocks = new ArrayList<>();

    // Constructor vacío requerido por JPA
    public Deposito() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCiudad() {
        return nombre;
    }

    public void setCiudad(String nombre) {
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
