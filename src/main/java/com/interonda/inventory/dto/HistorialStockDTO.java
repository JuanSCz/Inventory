package com.interonda.inventory.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class HistorialStockDTO {

    private Long id;

    @NotNull(message = "La cantidad anterior no puede estar vacía")
    @PositiveOrZero(message = "La cantidad anterior debe ser un número positivo o cero")
    private Integer cantidadAnterior;

    @NotNull(message = "La cantidad nueva no puede estar vacía")
    @PositiveOrZero(message = "La cantidad nueva debe ser un número positivo o cero")
    private Integer cantidadNueva;

    @NotNull(message = "La fecha de actualización no puede estar vacía")
    @FutureOrPresent(message = "La fecha de actualización debe ser en el presente o futuro")
    private LocalDateTime fechaActualizacion;

    @NotBlank(message = "El motivo no puede estar vacío")
    @Size(min = 3, max = 50, message = "El motivo debe tener entre 3 y 50 caracteres")
    private String motivo;

    @NotBlank(message = "El tipo de movimiento no puede estar vacío")
    @Size(min = 3, max = 50, message = "El tipo de movimiento debe tener entre 3 y 50 caracteres")
    private String tipoMovimiento;

    @NotNull(message = "El producto no puede estar vacío")
    private Long productoId;

    @NotNull(message = "El depósito no puede estar vacío")
    private Long depositoId;

    @NotNull(message = "El usuario no puede estar vacío")
    private Long usuarioId;

    @NotNull(message = "El stock no puede estar vacío")
    private Long stockId;

    public HistorialStockDTO() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidadAnterior() {
        return cantidadAnterior;
    }

    public void setCantidadAnterior(Integer cantidadAnterior) {
        this.cantidadAnterior = cantidadAnterior;
    }

    public Integer getCantidadNueva() {
        return cantidadNueva;
    }

    public void setCantidadNueva(Integer cantidadNueva) {
        this.cantidadNueva = cantidadNueva;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
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

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }
}