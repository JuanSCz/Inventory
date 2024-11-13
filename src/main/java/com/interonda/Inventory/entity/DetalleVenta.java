package com.interonda.Inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{detalleVenta.cantidad.notNull}")
    @Positive(message = "{detalleVenta.cantidad.positive}")
    @Column(nullable = false)
    private Integer cantidad;

    @NotNull(message = "{detalleVenta.precioUnitario.notNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{detalleVenta.precioUnitario.decimalMin}")
    @Digits(integer = 10, fraction = 2, message = "{detalleVenta.precioUnitario.digits}")
    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

    // Relaciones

    // Relación muchos-a-uno con Venta
    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = false)
    @NotNull(message = "{detalleVenta.venta.notNull}")
    private Venta venta;

    // Relación muchos-a-uno con Producto
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    @NotNull(message = "{detalleVenta.producto.notNull}")
    private Producto producto;

    // Constructor vacío requerido por JPA
    public DetalleVenta() {
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

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}

