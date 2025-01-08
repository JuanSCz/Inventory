package com.interonda.inventory.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class ProductoDTO {

    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre debe tener un máximo de 50 caracteres")
    private String nombre;

    @NotBlank(message = "La descripcion no puede estar vacía")
    @Size(max = 100, message = "La descripción debe tener un máximo de 100 caracteres")
    private String descripcion;

    @NotNull(message = "El precio no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
    @Digits(integer = 10, fraction = 3, message = "El precio debe tener un máximo de 10 dígitos enteros y 3 decimales")
    private BigDecimal precio;

    @NotNull(message = "El costo no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = false, message = "El costo debe ser mayor que 0")
    @Digits(integer = 10, fraction = 3, message = "El costo debe tener un máximo de 10 dígitos enteros y 3 decimales")
    private BigDecimal costo;

    @Size(max = 13, message = "El código de barras debe tener un máximo de 13 caracteres")
    private String codigoBarras;

    @Size(max = 50, message = "El número de serie debe tener un máximo de 50 caracteres")
    private String numeroDeSerie;

    @NotNull(message = "El stock actual no puede estar vacío")
    @PositiveOrZero(message = "El stock actual debe ser un número positivo o cero")
    private Integer stockActual;

    @NotNull(message = "El stock mínimo no puede estar vacío")
    @PositiveOrZero(message = "El stock mínimo debe ser un número positivo o cero")
    private Integer stockMinimo;

    @Size(max = 17, message = "La dirección MAC debe tener un máximo de 17 caracteres")
    private String macAddress;

    @NotNull(message = "La categoría no puede estar vacía")
    private Long categoriaId;

    private long depositoId;

    private String categoriaNombre;

    private List<StockDTO> stocks;

    @NotBlank(message = "El total no puede estar vacío")
    @Pattern(regexp = "\\d{1,3}(\\.\\d{3})*(,\\d{1,2})?", message = "El precio debe tener un formato válido (Ej: 1.000.000)")
    private String precioString;

    @NotBlank(message = "El total no puede estar vacío")
    @Pattern(regexp = "\\d{1,3}(\\.\\d{3})*(,\\d{1,2})?", message = "El costo debe tener un formato válido (Ej: 1.000.000)")
    private String costoString;

    public ProductoDTO() {
        this.stocks = new ArrayList<>();
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

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public List<StockDTO> getStocks() {
        return stocks;
    }

    public void setStocks(List<StockDTO> stocks) {
        this.stocks = stocks;
    }

    public long getDepositoId() {
        return depositoId;
    }

    public void setDepositoId(long depositoId) {
        this.depositoId = depositoId;
    }

    public String getPrecioString() {
        if (this.precio != null) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            DecimalFormat formatter = new DecimalFormat("#,###,###.##", symbols);
            return formatter.format(this.precio);
        }
        return this.precioString;
    }

    public void setPrecioString(String precioString) {
        this.precioString = precioString;
        if (precioString != null && !precioString.isEmpty()) {
            this.precio = new BigDecimal(precioString.replace(".", "").replace(",", "."));
        }
    }

    public String getCostoString() {
        if (this.costo != null) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            DecimalFormat formatter = new DecimalFormat("#,###,###.##", symbols);
            return formatter.format(this.costo);
        }
        return this.costoString;
    }

    public void setCostoString(String costoString) {
        this.costoString = costoString;
        if (costoString != null && !costoString.isEmpty()) {
            this.costo = new BigDecimal(costoString.replace(".", "").replace(",", "."));
        }
    }
}