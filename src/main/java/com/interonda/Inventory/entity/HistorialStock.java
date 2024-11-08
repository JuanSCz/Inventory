package com.interonda.Inventory.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_stock")
public class HistorialStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int cantidadAnterior;

    @Column(nullable = false)
    private int cantidadNueva;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private String motivo;

    @Column(nullable = false)
    private String tipoMovimiento;

    @Column(nullable = true)
    private String observacion;

    // Relaciones

    // Relación muchos-a-uno con Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // Relación muchos-a-uno con Deposito
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

    // Constructor con parámetros
    public HistorialStock(int cantidadAnterior, int cantidadNueva, LocalDateTime fecha,
                          String motivo, String tipoMovimiento, String observacion,
                          Producto producto, Deposito deposito, Usuario usuario, Stock stock) {
        if (cantidadAnterior < 0 || cantidadNueva < 0) {
            throw new IllegalArgumentException("Las cantidades no pueden ser negativas.");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }
        this.cantidadAnterior = cantidadAnterior;
        this.cantidadNueva = cantidadNueva;
        this.fecha = fecha;
        this.motivo = motivo;
        this.tipoMovimiento = tipoMovimiento;
        this.observacion = observacion;
        this.producto = producto;
        this.deposito = deposito;
        this.usuario = usuario;
        this.stock = stock;
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
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
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

