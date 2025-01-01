package com.interonda.inventory.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaDTO {

    private Long id;

    @NotNull(message = "La fecha no puede ser nula")
    private LocalDate fecha;

    @NotNull(message = "El total no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor que 0")
    private BigDecimal total;

    @NotBlank(message = "El método de pago no puede estar vacío")
    private String metodoPago;

    @NotBlank(message = "El estado no puede estar vacío")
    private String estado;

    @NotBlank(message = "Los impuestos no pueden estar vacíos")
    private String impuestos;

    @NotNull(message = "El ID del cliente no puede ser nulo")
    private Long clienteId;

    private String clienteNombre;

    private BigDecimal subtotal;

    private ClienteDTO cliente;

    private List<DetalleVentaDTO> detallesVenta = new ArrayList<>();

    private List<Long> detallesVentaIds;

    private String totalFormatted;

    private String subtotalFormatted;

    @NotBlank(message = "El total no puede estar vacío")
    @Pattern(regexp = "\\d{1,3}(\\.\\d{3})*(,\\d{1,2})?", message = "El total debe tener un formato válido (e.g., 1.000.000,00)")
    private String totalString;

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

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public List<DetalleVentaDTO> getDetallesVenta() {
        return detallesVenta;
    }

    public void setDetallesVenta(List<DetalleVentaDTO> detallesVenta) {
        this.detallesVenta = detallesVenta;
    }

    public String getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(String impuestos) {
        this.impuestos = impuestos;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotalFormatted() {
        return totalFormatted;
    }

    public void setTotalFormatted(String totalFormatted) {
        this.totalFormatted = totalFormatted;
    }

    public String getSubtotalFormatted() {
        return subtotalFormatted;
    }

    public void setSubtotalFormatted(String subtotalFormatted) {
        this.subtotalFormatted = subtotalFormatted;
    }

    public void setTotalString(String totalString) {
        this.totalString = totalString;
        if (totalString != null && !totalString.isEmpty()) {
            this.total = new BigDecimal(totalString.replace(".", "").replace(",", "."));
        }
    }

    public String getTotalString() {
        return this.totalString;
    }
}