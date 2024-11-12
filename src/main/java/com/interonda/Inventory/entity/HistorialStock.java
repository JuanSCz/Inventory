package com.interonda.Inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_stock")
public class HistorialStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La cantidad anterior es requerida")
    @PositiveOrZero(message = "La cantidad anterior debe ser mayor o igual a cero")
    @Column(name = "cantidad_anterior", nullable = false)
    private Integer cantidadAnterior;

    @NotNull(message = "La cantidad nueva es requerida")
    @PositiveOrZero(message = "La cantidad nueva debe ser mayor o igual a cero")
    @Column(name = "cantidad_actual", nullable = false)
    private Integer cantidadNueva;

    @NotNull(message = "La fecha es requerida")
    @FutureOrPresent(message = "La fecha debe ser actual o futura")
    @Column(name = "actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @NotBlank(message = "El motivo es requerido")
    @Size(min = 3, max = 50, message = "El motivo debe tener entre 3 y 50 caracteres")
    @Column(nullable = false)
    private String motivo;

    @NotBlank(message = "El tipo de movimiento es requerido")
    @Size(min = 3, max = 50, message = "El tipo de movimiento debe tener entre 3 y 50 caracteres")
    @Column(name = "tipo_movimiento", length = 50, nullable = false)
    private String tipoMovimiento;

    @Size(max = 200, message = "La observación debe tener como máximo 200 caracteres")
    @Column(length = 200)
    private String observacion;

    // Relaciones

    // Relación muchos-a-uno con Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // Relación muchos-a-uno con Depósito
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposito_id", nullable = false)
    private Deposito deposito;

    // Relación muchos-a-uno con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock; // Esta es la propiedad que faltaba

    // Constructor vacío requerido por JPA
    public HistorialStock() {
        // Constructor vacío
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCantidadAnterior() {
        return cantidadAnterior;
    }

    public void setCantidadAnterior(int cantidadAnterior) {
        this.cantidadAnterior = cantidadAnterior;
    }

    public int getCantidadNueva() {
        return cantidadNueva;
    }

    public void setCantidadNueva(int cantidadNueva) {
        this.cantidadNueva = cantidadNueva;
    }

    public LocalDateTime getFecha() {
        return fechaActualizacion;
    }

    public void setFecha(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}

