package com.interonda.Inventory.entityDTO;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class VentaDTO {

    private Long id;

    @NotNull(message = "{ventaDTO.fecha.notNull}")
    @FutureOrPresent(message = "{venta.fecha.futureOrPresent}")
    private LocalDate fecha;

    @NotNull(message = "{ventaDTO.total.notNull}")
    @DecimalMin(value = "0.0", message = "{ventaDTO.total.decimalMin}")
    @Digits(integer = 10, fraction = 2, message = "{ventaDTO.total.digits}")
    private BigDecimal total;

    @NotBlank(message = "{ventaDTO.metodoPago.notBlank}")
    private String metodoPago;

    @NotBlank(message = "{ventaDTO.estado.notBlank}")
    private String estado;

    @NotNull(message = "{ventaDTO.impuestos.notNull}")
    @DecimalMin(value = "0.0", message = "{ventaDTO.impuestos.decimalMin}")
    @Digits(integer = 10, fraction = 2, message = "{ventaDTO.impuestos.digits}")
    private BigDecimal impuestos;

    @NotNull(message = "{ventaDTO.cliente.notNull}")
    private Long clienteId;

    private List<Long> detallesVentaIds;

    public VentaDTO() {
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

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<Long> getDetallesVentaIds() {
        return detallesVentaIds;
    }

    public void setDetallesVentaIds(List<Long> detallesVentaIds) {
        this.detallesVentaIds = detallesVentaIds;
    }
}