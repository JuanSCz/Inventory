package com.interonda.Inventory.entityDTO;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class StockDTO {

    private Long id;

    @NotNull(message = "{stockDTO.cantidad.notNull}")
    @PositiveOrZero(message = "{stockDTO.cantidad.positiveOrZero}")
    private Integer cantidad;

    @FutureOrPresent(message = "{stockDTO.fechaActualizacion.futureOrPresent}")
    private LocalDateTime fechaActualizacion;

    @NotBlank(message = "{stockDTO.operacion.notBlank}")
    @Size(max = 50, message = "{stockDTO.operacion.size}")
    private String operacion;

    @NotNull(message = "{stockDTO.producto.notNull}")
    private Long productoId;

    @NotNull(message = "{stockDTO.deposito.notNull}")
    private Long depositoId;

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
}