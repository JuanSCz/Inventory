package com.interonda.Inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha no puede ser nula")
    @FutureOrPresent(message = "La fecha debe ser presente o futura")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull(message = "El total no puede ser nulo")
    @DecimalMin(value = "0.0 ", message = "El total debe ser mayor o igual a 0")
    @Digits(integer = 10, fraction = 2, message = "El total debe tener como máximo 10 dígitos enteros y 2 decimales")
    @Column(nullable = false)
    private BigDecimal total;

    @NotBlank(message = "El método de pago no puede estar vacío")
    @Column(name = "metodo_de_pago", nullable = false, length = 30)
    private String metodoPago;

    @NotBlank(message = "El estado no puede estar vacío")
    @Column(nullable = false, length = 30)
    private String estado;

    @NotNull(message = "Los impuestos no pueden ser nulos")
    @DecimalMin(value = "0.0", message = "Los impuestos deben ser mayores o iguales a 0")
    @Digits(integer = 10, fraction = 2, message = "Los impuestos deben tener como máximo 10 dígitos enteros y 2 decimales")
    @Column(nullable = false)
    private BigDecimal impuestos;

    // Relaciones

    // Relación muchos-a-uno con Proveedor
    @ManyToOne
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    // Relación uno-a-muchos con DetalleCompra
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCompra> detallesCompra;

    // Constructor vacío requerido por JPA
    public Compra() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<DetalleCompra> getDetallesCompra() {
        return detallesCompra;
    }

    public void setDetallesCompra(List<DetalleCompra> detallesCompra) {
        this.detallesCompra = detallesCompra;
    }
}

