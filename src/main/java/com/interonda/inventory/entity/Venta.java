package com.interonda.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

    @NotNull(message = "La fecha no puede ser nula")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull(message = "El total no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor que 0")
    @Column(nullable = false)
    private BigDecimal total;

    @NotBlank(message = "El método de pago no puede estar vacío")
    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;

    @NotBlank(message = "El estado no puede estar vacío")
    @Column(nullable = false)
    private String estado;

    @NotBlank(message = "Los impuestos no pueden estar vacíos")
    @Column(nullable = false, length = 30)
    private String impuestos;

    @NotNull(message = "El cliente no puede ser nulo")
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

    public String getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(String impuestos) {
        this.impuestos = impuestos;
    }
}

