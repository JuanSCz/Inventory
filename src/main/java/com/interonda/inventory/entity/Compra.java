package com.interonda.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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

    @NotNull(message = "La fecha no puede estar vacía")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull(message = "El total no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor que 0")
    @Column(nullable = false)
    private BigDecimal total;

    @NotBlank(message = "El método de pago no puede estar vacío")
    @Size(max = 30, message = "El método de pago no puede tener más de 30 caracteres")
    @Column(name = "metodo_de_pago", nullable = false, length = 30)
    private String metodoPago;

    @NotBlank(message = "El estado no puede estar vacío")
    @Size(max = 30, message = "El estado no puede tener más de 30 caracteres")
    @Column(nullable = false, length = 30)
    private String estado;

    @NotBlank(message = "Los impuestos no pueden estar vacíos")
    @Size(max = 30, message = "Los impuestos no pueden tener más de 30 caracteres")
    @Column(nullable = false, length = 30)
    private String impuestos;

// Relaciones

    // Relación muchos-a-uno con Proveedor(una compra pertenece a un proveedor)

    @NotNull(message = "El proveedor no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "proveedor_id", nullable = false)
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

    public String getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(String impuestos) {
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

