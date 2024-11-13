package com.interonda.Inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{stock.cantidad.notNull}")
    @PositiveOrZero(message = "{stock.cantidad.positiveOrZero}")
    @Column(nullable = false)
    private Integer cantidad;

    @FutureOrPresent(message = "{stock.fechaActualizacion.futureOrPresent}")
    @Column(name = "actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @NotBlank(message = "{stock.operacion.notBlank}")
    @Size(max = 50, message = "{stock.operacion.size}")
    @Column(length = 50, nullable = false)
    private String operacion;

    // Relaciones

    // Relación muchos-a-uno con Producto (un stock pertenece a un producto)
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    @NotNull(message = "{stock.producto.notNull}")
    private Producto producto;

    // Relación muchos-a-uno con Deposito (un stock pertenece a un depósito)
    @ManyToOne
    @JoinColumn(name = "deposito_id", nullable = false)
    @NotNull(message = "{stock.deposito.notNull}")
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

