package com.interonda.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class DepositoDTO {

    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;

    @NotBlank(message = "La provincia no puede estar vacía")
    @Size(max = 20, message = "La provincia no puede tener más de 20 caracteres")
    private String provincia;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(max = 50, message = "La dirección no puede tener más de 50 caracteres")
    private String direccion;

    @NotBlank(message = "El contacto no puede estar vacío")
    @Size(max = 20, message = "El contacto no puede tener más de 20 caracteres")
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