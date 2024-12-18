package com.interonda.inventory.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductoDTO {

    private Long id;

    @NotBlank(message = "{productoDTO.nombre.notBlank}")
    @Size(min = 3, max = 50, message = "{productoDTO.nombre.size}")
    private String nombre;

    @Size(max = 75, message = "{productoDTO.descripcion.size}")
    private String descripcion;

    @NotNull(message = "{productoDTO.precio.notNull}")
    @Digits(integer = 10, fraction = 2, message = "{productoDTO.precio.digits}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{productoDTO.precio.decimalMin}")
    private BigDecimal precio;

    @NotNull(message = "{productoDTO.costo.notNull}")
    @Digits(integer = 10, fraction = 2, message = "{productoDTO.costo.digits}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{productoDTO.costo.decimalMin}")
    private BigDecimal costo;

    @Size(min = 12, max = 13, message = "{productoDTO.codigoBarras.size}")
    @Positive(message = "{productoDTO.codigoBarras.positive}")
    private String codigoBarras;

    @Size(min = 1, max = 50, message = "{productoDTO.numeroDeSerie.size}")
    @Positive(message = "{productoDTO.numeroDeSerie.positive}")
    private String numeroDeSerie;

    @NotNull(message = "{productoDTO.stockActual.notNull}")
    @PositiveOrZero(message = "{productoDTO.stockActual.positiveOrZero}")
    private Integer stockActual;

    @NotNull(message = "{productoDTO.stockMinimo.notNull}")
    @PositiveOrZero(message = "{productoDTO.stockMinimo.positiveOrZero}")
    private Integer stockMinimo;

    @Size(min = 17, max = 17, message = "{productoDTO.macAddress.size}")
    @Positive(message = "{productoDTO.macAddress.positive}")
    private String macAddress;

    @NotNull(message = "{productoDTO.categoriaId.notNull}")
    private Long categoriaId;

    @NotNull(message = "{productoDTO.categoria.notNull}")
    private String categoriaNombre;

    public ProductoDTO() {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
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

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String rolNombre) {
        this.categoriaNombre = rolNombre;
    }
}