package com.interonda.Inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La cantidad es obligatoria")
    @PositiveOrZero(message = "La cantidad no puede ser negativa")
    @Column(nullable = false)
    private Integer cantidad;

    @FutureOrPresent(message = "La fecha de actualización debe ser actual o futura")
    @Column(name = "actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @NotBlank(message = "La operación es obligatoria")
    @Size(max = 50, message = "La operación no puede tener más de 50 caracteres")
    @Column(length = 50, nullable = false)
    private String operacion;

    // Relaciones

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "deposito_id", nullable = false)
    private Deposito deposito;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialStock> historialStocks;

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

