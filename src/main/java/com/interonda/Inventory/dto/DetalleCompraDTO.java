package com.interonda.Inventory.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class DetalleCompraDTO {

    private Long id;

    @NotNull(message = "{detalleCompraDTO.cantidad.notNull}")
    @Positive(message = "{detalleCompraDTO.cantidad.positive}")
    private Integer cantidad;

    @NotNull(message = "{detalleCompraDTO.precioUnitario.notNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{detalleCompraDTO.precioUnitario.decimalMin}")
    @Digits(integer = 10, fraction = 2, message = "{detalleCompraDTO.precioUnitario.digits}")
    private BigDecimal precioUnitario;

    @NotNull(message = "{detalleCompraDTO.compra.notNull}")
    private Long compraId;

    @NotNull(message = "{detalleCompraDTO.producto.notNull}")
    private Long productoId;

    public DetalleCompraDTO() {
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

    public Long getCompraId() {
        return compraId;
    }

    public void setCompraId(Long compraId) {
        this.compraId = compraId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
}