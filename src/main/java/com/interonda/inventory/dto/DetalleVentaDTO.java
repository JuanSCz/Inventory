package com.interonda.inventory.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

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

    @Pattern(regexp = "\\d{1,3}(\\.\\d{3})*(,\\d{1,2})?", message = "El total debe tener un formato válido (e.g., 1.000.000,00)")
    private String precioUnitarioString;

    private String totalDetalleFormatted;

    private String subtotalFormatted;

    private String macAddress;

    private String codigoBarras;

    private String numeroSerie;

    private Long depositoId;

    private String depositoNombre;

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
        if (cantidad != null && precioUnitario != null) {
            subtotal = precioUnitario.multiply(new BigDecimal(cantidad));
        }
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public void setPrecioUnitarioString(String precioUnitarioString) {
        this.precioUnitarioString = precioUnitarioString;
        if (precioUnitarioString != null && !precioUnitarioString.isEmpty()) {
            this.precioUnitario = new BigDecimal(precioUnitarioString.replace(".", "").replace(",", "."));
        }
    }

    public String getPrecioUnitarioString() {
        if (this.precioUnitario != null) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            DecimalFormat formatter = new DecimalFormat("#,###,###.##", symbols);
            return formatter.format(this.precioUnitario);
        }
        return this.precioUnitarioString;
    }

    public void setSubtotalFormatted(String subtotalFormatted) {
        this.subtotalFormatted = subtotalFormatted;
        if (subtotalFormatted != null && !subtotalFormatted.isEmpty()) {
            this.subtotal = new BigDecimal(subtotalFormatted.replace(".", "").replace(",", "."));
        }
    }

    public String getSubtotalFormatted() {
        if (subtotal != null) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');

            DecimalFormat formatter = new DecimalFormat("#,###,###", symbols);
            formatter.setRoundingMode(RoundingMode.DOWN);
            subtotalFormatted = formatter.format(subtotal);
        }
        return subtotalFormatted;
    }

    public String getTotalDetalleFormatted() {
        if (cantidad != null && precioUnitario != null) {
            BigDecimal totalDetalle = precioUnitario.multiply(new BigDecimal(cantidad));
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            DecimalFormat formatter = new DecimalFormat("#,###,###.##", symbols);
            return formatter.format(totalDetalle);
        }
        return totalDetalleFormatted;
    }

    public void setTotalDetalleFormatted(String totalDetalleFormatted) {
        this.totalDetalleFormatted = totalDetalleFormatted;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public Long getDepositoId() {
        return depositoId;
    }

    public void setDepositoId(Long depositoId) {
        this.depositoId = depositoId;
    }

    public String getDepositoNombre() {
        return depositoNombre;
    }

    public void setDepositoNombre(String depositoNombre) {
        this.depositoNombre = depositoNombre;
    }
}