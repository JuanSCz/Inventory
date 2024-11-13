package com.interonda.Inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{compra.fecha.notNull}")
    @FutureOrPresent(message = "{compra.fecha.futureOrPresent}")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull(message = "{compra.total.notNull}")
    @DecimalMin(value = "0.0 ", message = "{compra.total.decimalMin}")
    @Digits(integer = 10, fraction = 2, message = "{compra.total.digits}")
    @Column(nullable = false)
    private BigDecimal total;

    @NotBlank(message = "{compra.metodoPago.notBlank}")
    @Column(name = "metodo_de_pago", nullable = false, length = 30)
    private String metodoPago;

    @NotBlank(message = "{compra.estado.notBlank}")
    @Column(nullable = false, length = 30)
    private String estado;

    @NotNull(message = "{compra.impuestos.notNull}")
    @DecimalMin(value = "0.0", message = "{compra.impuestos.decimalMin}")
    @Digits(integer = 10, fraction = 2, message = "{compra.impuestos.digits}")
    @Column(nullable = false)
    private BigDecimal impuestos;

    // Relaciones

    // Relación muchos-a-uno con Proveedor(una compra pertenece a un proveedor)
    @ManyToOne
    @JoinColumn(name = "proveedor_id", nullable = false)
    @NotNull(message = "{compra.proveedor.notNull}")
    private Proveedor proveedor;

    // Relación uno-a-muchos con DetalleCompra(una compra puede tener muchos detalles de compra)
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCompra> detallesCompra = new ArrayList<>();

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

