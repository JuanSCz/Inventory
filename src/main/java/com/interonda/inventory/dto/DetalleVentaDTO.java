package com.interonda.inventory.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class DetalleVentaDTO {

    private Long id;

    @NotNull(message = "La cantidad no puede estar vacía")
    @Positive(message = "La cantidad debe ser un número positivo")
    private Integer cantidad;

    @NotNull(message = "El precio unitario no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor que 0")
    @Digits(integer = 10, fraction = 2, message = "El precio unitario debe tener un máximo de 10 dígitos enteros y 2 decimales")
    private BigDecimal precioUnitario;

    @NotNull(message = "La venta no puede estar vacía")
    private Long ventaId;

    @NotNull(message = "El producto no puede estar vacío")
    private Long productoId;

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String productoNombre;

    @NotBlank(message = "El nombre del proveedor no puede estar vacío")
    private String proveedorNombre;

    @NotNull(message = "El subtotal no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = false, message = "El subtotal debe ser mayor que 0")
    private BigDecimal subtotal;

    private String precioUnitarioFormatted;

    private String subtotalFormatted;

    private String totalFormatted;

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

    public String getPrecioUnitarioFormatted() {
        return precioUnitarioFormatted;
    }

    public void setPrecioUnitarioFormatted(String precioUnitarioFormatted) {
        this.precioUnitarioFormatted = precioUnitarioFormatted;
    }

    public String getTotalFormatted() {
        return totalFormatted;
    }

    public void setTotalFormatted(String totalFormatted) {
        this.totalFormatted = totalFormatted;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getSubtotalFormatted() {
        return subtotalFormatted;
    }

    public void setSubtotalFormatted(String subtotalFormatted) {
        this.subtotalFormatted = subtotalFormatted;
    }
}