package com.interonda.Inventory.entityDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class VentaDTO {

    private Long id;
    private LocalDate fecha;
    private BigDecimal total;
    private String metodoPago;
    private String estado;
    private BigDecimal impuestos;
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