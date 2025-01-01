package com.interonda.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Entity
@Table(name = "detalles_compra")
public class DetalleCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La cantidad no puede estar vacía")
    @Positive(message = "La cantidad debe ser un número positivo")
    @Column(nullable = false)
    private Integer cantidad;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor que 0")
    @NotNull(message = "El precio unitario no puede estar vacío")
    @Digits(integer = 10, fraction = 3, message = "El precio unitario debe tener un máximo de 10 dígitos enteros y 3 decimales")
    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

    // Relaciones

    // Relación muchos-a-uno con Compra(una compra puede tener varios detalles)
    @NotNull(message = "La compra no puede ser nula")
    @ManyToOne
    @JoinColumn(name = "compra_id", nullable = false)
    private Compra compra;

    // Relación muchos-a-uno con Producto (un producto puede estar en varios detalles)
    @NotNull(message = "El producto no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
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

