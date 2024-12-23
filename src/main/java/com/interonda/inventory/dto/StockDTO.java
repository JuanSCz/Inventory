package com.interonda.inventory.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class StockDTO {

    private Long id;

    @NotNull(message = "La cantidad no puede ser nula")
    private Integer cantidad;

    private LocalDateTime fechaActualizacion;

    private String operacion;

    @NotNull(message = "El ID del producto no puede ser nulo")
    private Long productoId;

    @NotNull(message = "El ID del depósito no puede ser nulo")
    private Long depositoId;

    private String depositoNombre; // Nuevo campo

    public StockDTO() {
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

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
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