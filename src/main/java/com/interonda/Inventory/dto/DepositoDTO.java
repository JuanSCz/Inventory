package com.interonda.Inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class DepositoDTO {

    private Long id;

    @NotBlank(message = "{depositoDTO.nombre.notBlank}")
    @Size(max = 50, message = "{depositoDTO.nombre.size}")
    private String nombre;

    @NotBlank(message = "{depositoDTO.provincia.notBlank}")
    @Size(max = 20, message = "{depositoDTO.provincia.size}")
    private String provincia;

    @NotBlank(message = "{depositoDTO.direccion.notBlank}")
    @Size(max = 20, message = "{depositoDTO.direccion.size}")
    private String direccion;

    @NotBlank(message = "{depositoDTO.contactoDeposito.notBlank}")
    @Size(max = 15, message = "{depositoDTO.contactoDeposito.size}")
    private String contactoDeposito;

    private List<StockDTO> stocks;

    public DepositoDTO() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContactoDeposito() {
        return contactoDeposito;
    }

    public void setContactoDeposito(String contactoDeposito) {
        this.contactoDeposito = contactoDeposito;
    }

    public List<StockDTO> getStocks() {
        return stocks;
    }

    public void setStocks(List<StockDTO> stocks) {
        this.stocks = stocks;
    }
}
