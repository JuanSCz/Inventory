package com.interonda.Inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalles_compra")
public class DetalleCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{detalleCompra.cantidad.notNull}")
    @Positive(message = "{detalleCompra.cantidad.positive}")
    @Column(nullable = false)
    private Integer cantidad;

    @NotNull(message = "{detalleCompra.precioUnitario.notNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{detalleCompra.precioUnitario.decimalMin}")
    @Digits(integer = 10, fraction = 2, message = "{detalleCompra.precioUnitario.digits}")
    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

    // Relaciones

    // Relación muchos-a-uno con Compra(una compra puede tener varios detalles)
    @ManyToOne
    @JoinColumn(name = "compra_id", nullable = false)
    @NotNull(message = "{detalleCompra.compra.notNull}")
    private Compra compra;

    // Relación muchos-a-uno con Producto(un producto puede estar en varios detalles)
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    @NotNull(message = "{detalleCompra.producto.notNull}")
    private Producto producto;

    // Constructor vacío requerido por JPA
    public DetalleCompra() {
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

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}

