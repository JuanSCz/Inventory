package com.interonda.inventory.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CompraDTO {

    private Long id;

    @NotNull(message = "La fecha no puede estar vacía")
    private LocalDate fecha;

    @NotNull(message = "El total no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor que 0")
    private BigDecimal total;

    @NotBlank(message = "El método de pago no puede estar vacío")
    @Size(max = 30, message = "El método de pago no puede tener más de 30 caracteres")
    private String metodoPago;

    @NotBlank(message = "El estado no puede estar vacío")
    @Size(max = 30, message = "El estado no puede tener más de 30 caracteres")
    private String estado;

    @NotBlank(message = "Los impuestos no pueden estar vacíos")
    @Size(max = 30, message = "Los impuestos no pueden tener más de 30 caracteres")
    private String impuestos;

    @NotNull(message = "El ID del proveedor no puede ser nulo")
    private Long proveedorId;

    private String proveedorNombre;

    private List<DetalleCompraDTO> detallesCompra = new ArrayList<>();

    @NotBlank(message = "El total no puede estar vacío")
    @Pattern(regexp = "\\d{1,3}(\\.\\d{3})*(,\\d{1,2})?", message = "El total debe tener un formato válido (Ej: 1.000.000)")
    private String totalString;

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

    public String getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(String impuestos) {
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

    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }

    public void setTotalString(String totalString) {
        this.totalString = totalString;
        if (totalString != null && !totalString.isEmpty()) {
            this.total = new BigDecimal(totalString.replace(".", "").replace(",", "."));
        }
    }

    public String getTotalString() {
        if (this.total != null) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            DecimalFormat formatter = new DecimalFormat("#,###,###.##", symbols);
            return formatter.format(this.total);
        }
        return this.totalString;
    }
}
