package com.interonda.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    @Column(nullable = false)
    private Integer cantidad;

    @NotNull(message = "La fecha de actualización no puede ser nula")
    @Column(name = "actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @NotBlank(message = "La operación no puede estar vacía")
    @Size(max = 50, message = "La operación debe tener un máximo de 50 caracteres")
    @Column(length = 50, nullable = false)
    private String operacion;

    // Relaciones

    // Relación muchos-a-uno con Producto (un stock pertenece a un producto)
    @NotNull(message = "El producto no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // Relación muchos-a-uno con Deposito (un stock pertenece a un depósito)
    @NotNull(message = "El depósito no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "deposito_id", nullable = false)
    private Deposito deposito;

    // Relación uno-a-muchos con HistorialStock (un stock puede tener varios historiales de stock)
    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialStock> historialStocks = new ArrayList<>();

    // Constructor vacío requerido por JPA
    public Stock() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    public List<HistorialStock> getHistorialStocks() {
        return historialStocks;
    }

    public void setHistorialStocks(List<HistorialStock> historialStocks) {
        this.historialStocks = historialStocks;
    }
}

