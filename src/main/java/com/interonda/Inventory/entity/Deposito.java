package com.interonda.Inventory.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "depositos")
public class Deposito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String direccion;

    // Relación uno-a-muchos con Stock
    @OneToMany(mappedBy = "deposito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stock> stocks;

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
}
