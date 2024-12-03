package com.interonda.inventory.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class HistorialStockDTO {

    private Long id;

    @NotNull(message = "{historialStockDTO.cantidadAnterior.notNull}")
    @PositiveOrZero(message = "{historialStockDTO.cantidadAnterior.positiveOrZero}")
    private Integer cantidadAnterior;

    @NotNull(message = "{historialStockDTO.cantidadNueva.notNull}")
    @PositiveOrZero(message = "{historialStockDTO.cantidadNueva.positiveOrZero}")
    private Integer cantidadNueva;

    @NotNull(message = "{historialStockDTO.fechaActualizacion.notNull}")
    @FutureOrPresent(message = "{historialStockDTO.fechaActualizacion.futureOrPresent}")
    private LocalDateTime fechaActualizacion;

    @NotBlank(message = "{historialStockDTO.motivo.notBlank}")
    @Size(min = 3, max = 50, message = "{historialStockDTO.motivo.size}")
    private String motivo;

    @NotBlank(message = "{historialStockDTO.tipoMovimiento.notBlank}")
    @Size(min = 3, max = 50, message = "{historialStockDTO.tipoMovimiento.size}")
    private String tipoMovimiento;

    @Size(max = 200, message = "{historialStockDTO.observacion.size}")
    private String observacion;

    @NotNull(message = "{historialStockDTO.producto.notNull}")
    private Long productoId;

    @NotNull(message = "{historialStockDTO.deposito.notNull}")
    private Long depositoId;

    @NotNull(message = "{historialStockDTO.usuario.notNull}")
    private Long usuarioId;

    @NotNull(message = "{historialStockDTO.stock.notNull}")
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

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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