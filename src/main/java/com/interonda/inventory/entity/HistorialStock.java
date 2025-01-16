package com.interonda.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_stock")
public class HistorialStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La cantidad anterior no puede estar vacía")
    @PositiveOrZero(message = "La cantidad anterior debe ser un número positivo o cero")
    @Column(name = "cantidad_anterior", nullable = false)
    private Integer cantidadAnterior;

    @NotNull(message = "La cantidad nueva no puede estar vacía")
    @PositiveOrZero(message = "La cantidad nueva debe ser un número positivo o cero")
    @Column(name = "cantidad_actual", nullable = false)
    private Integer cantidadNueva;

    @NotNull(message = "La fecha de actualización no puede ser nula")
    @Column(name = "actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @NotBlank(message = "El motivo no puede estar vacío")
    @Size(min = 3, max = 50, message = "El motivo debe tener entre 3 y 50 caracteres")
    @Column(nullable = false)
    private String motivo;

    @NotBlank(message = "El tipo de movimiento no puede estar vacío")
    @Size(min = 3, max = 50, message = "El tipo de movimiento debe tener entre 3 y 50 caracteres")
    @Column(name = "tipo_movimiento", length = 50, nullable = false)
    private String tipoMovimiento;

    // Relaciones

    // Relación muchos-a-uno con Producto (un producto puede tener varios historiales de stock)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // Relación muchos-a-uno con Depósito (un depósito puede tener varios historiales de stock)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposito_id", nullable = false)
    private Deposito deposito;

    // Relación muchos-a-uno con Usuario (un usuario puede tener varios historiales de stock)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    // Relación muchos-a-uno con Stock (un stock puede tener varios historiales de stock)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock; // Esta es la propiedad que faltaba

    // Constructor vacío requerido por JPA
    public HistorialStock() {
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

    public void setCantidadAnterior(Integer cantidadAnterior) {
        this.cantidadAnterior = cantidadAnterior;
    }

    public void setCantidadNueva(Integer cantidadNueva) {
        this.cantidadNueva = cantidadNueva;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}

