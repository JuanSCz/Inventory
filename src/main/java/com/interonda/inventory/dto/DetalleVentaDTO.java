package com.interonda.inventory.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class DetalleVentaDTO {

    private Long id;

    @NotNull(message = "{detalleVentaDTO.cantidad.notNull}")
    @Positive(message = "{detalleVentaDTO.cantidad.positive}")
    private Integer cantidad;

    @NotNull(message = "{detalleVentaDTO.precioUnitario.notNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{detalleVentaDTO.precioUnitario.decimalMin}")
    @Digits(integer = 10, fraction = 2, message = "{detalleVentaDTO.precioUnitario.digits}")
    private BigDecimal precioUnitario;

    @NotNull(message = "{detalleVentaDTO.venta.notNull}")
    private Long ventaId;

    @NotNull(message = "{detalleVentaDTO.producto.notNull}")
    private Long productoId;

    public DetalleVentaDTO() {
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

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
}