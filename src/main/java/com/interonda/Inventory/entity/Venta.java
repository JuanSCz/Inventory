package com.interonda.Inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{venta.fecha.notNull}")
    @FutureOrPresent(message = "{venta.fecha.futureOrPresent}")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull(message = "{venta.total.notNull}")
    @DecimalMin(value = "0.0", message = "{venta.total.decimalMin}")
    @Digits(integer = 10, fraction = 2, message = "{venta.total.digits}")
    @Column(nullable = false)
    private BigDecimal total;

    @NotBlank(message = "{venta.metodoPago.notBlank}")
    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;

    @NotBlank(message = "{venta.estado.notBlank}")
    @Column(nullable = false)
    private String estado;

    @NotNull(message = "{venta.impuestos.notNull}")
    @DecimalMin(value = "0.0", message = "{venta.impuestos.decimalMin}")
    @Digits(integer = 10, fraction = 2, message = "{venta.impuestos.digits}")
    @Column(nullable = false)
    private BigDecimal impuestos;

    // Relación muchos-a-uno con Cliente (muchas ventas pueden tener un cliente)
    @NotNull(message = "{venta.cliente.notNull}")
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Relación uno-a-muchos con DetalleVenta (una venta puede tener muchos detalles de venta)
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVenta> detallesVenta = new ArrayList<>();

    // Constructor vacío requerido por JPA
    public Venta() {
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<DetalleVenta> getDetallesVenta() {
        return detallesVenta;
    }

    public void setDetallesVenta(List<DetalleVenta> detallesVenta) {
        this.detallesVenta = detallesVenta;
    }
}

