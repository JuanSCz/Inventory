package com.interonda.inventory.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class DetalleCompraDTO {

    private Long id;

    @NotNull(message = "La cantidad no puede estar vacía")
    @Positive(message = "La cantidad debe ser un número positivo")
    private Integer cantidad;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor que 0")
    @NotNull(message = "El precio unitario no puede estar vacío")
    @Digits(integer = 10, fraction = 3, message = "El precio unitario debe tener un máximo de 10 dígitos enteros y 3 decimales")
    private BigDecimal precioUnitario;

    @NotNull(message = "La compra no puede estar vacía")
    private Long compraId;

    @NotNull(message = "El producto no puede estar vacío")
    private Long productoId;

    @NotNull(message = "El producto no puede estar vacío")
    private String productoNombre;

    @NotNull(message = "El proveedor no puede estar vacío")
    private String proveedorNombre;

    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }

    private String precioUnitarioFormatted;

    private String totalFormatted;

    public DetalleCompraDTO() {
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
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
}