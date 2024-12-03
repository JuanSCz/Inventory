package com.interonda.inventory.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CompraDTO {

    private Long id;

    @NotNull(message = "{compraDTO.fecha.notNull}")
    @FutureOrPresent(message = "{compraDTO.fecha.futureOrPresent}")
    private LocalDate fecha;

    @NotNull(message = "{compraDTO.total.notNull}")
    @DecimalMin(value = "0.0", message = "{compraDTO.total.decimalMin}")
    @Digits(integer = 10, fraction = 2, message = "{compraDTO.total.digits}")
    private BigDecimal total;

    @NotBlank(message = "{compraDTO.metodoPago.notBlank}")
    private String metodoPago;

    @NotBlank(message = "{compraDTO.estado.notBlank}")
    private String estado;

    @NotNull(message = "{compraDTO.impuestos.notNull}")
    @DecimalMin(value = "0.0", message = "{compraDTO.impuestos.decimalMin}")
    @Digits(integer = 10, fraction = 2, message = "{compraDTO.impuestos.digits}")
    private BigDecimal impuestos;

    @NotNull(message = "{compraDTO.proveedor.notNull}")
    private Long proveedorId;

    private List<DetalleCompraDTO> detallesCompra = new ArrayList<>();

    public CompraDTO() {
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

    public Long getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }

    public List<DetalleCompraDTO> getDetallesCompra() {
        return detallesCompra;
    }

    public void setDetallesCompra(List<DetalleCompraDTO> detallesCompra) {
        this.detallesCompra = detallesCompra;
    }
}
