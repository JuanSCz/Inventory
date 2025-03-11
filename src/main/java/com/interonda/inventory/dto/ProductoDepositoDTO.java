package com.interonda.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class ProductoDepositoDTO {

    private Long id;

    @Size(max = 13, message = "El código de barras debe tener un máximo de 13 caracteres")
    private String codigoBarras;

    @Size(max = 50, message = "El número de serie debe tener un máximo de 50 caracteres")
    private String numeroDeSerie;

    @Size(max = 17, message = "La dirección MAC debe tener un máximo de 17 caracteres")
    private String macAddress;

    @NotNull(message = "El depósito no puede estar vacío")
    private Long depositoId;

    private List<StockDTO> stocks;

    public ProductoDepositoDTO() {
        this.stocks = new ArrayList<>();
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getNumeroDeSerie() {
        return numeroDeSerie;
    }

    public void setNumeroDeSerie(String numeroDeSerie) {
        this.numeroDeSerie = numeroDeSerie;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Long getDepositoId() {
        return depositoId;
    }

    public void setDepositoId(Long depositoId) {
        this.depositoId = depositoId;
    }

    public List<StockDTO> getStocks() {
        return stocks;
    }

    public void setStocks(List<StockDTO> stocks) {
        this.stocks = stocks;
    }
}