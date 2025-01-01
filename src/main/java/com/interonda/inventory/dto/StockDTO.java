package com.interonda.inventory.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class StockDTO {
    private Long id;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;

    @NotNull(message = "La fecha de actualización no puede ser nula")
    private LocalDateTime fechaActualizacion;

    @NotBlank(message = "La operación no puede estar vacía")
    @Size(max = 50, message = "La operación debe tener un máximo de 50 caracteres")
    private String operacion;

    @NotNull(message = "El ID del producto no puede ser nulo")
    private Long productoId;

    @NotNull(message = "El ID del depósito no puede ser nulo")
    private Long depositoId;

    @NotBlank(message = "El nombre del depósito no puede estar vacío")
    private String depositoNombre;

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