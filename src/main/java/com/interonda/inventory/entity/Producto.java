package com.interonda.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre debe tener un máximo de 50 caracteres")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "La descripcion no puede estar vacía")
    @Size(max = 100, message = "La descripción debe tener un máximo de 100 caracteres")
    @Column(length = 100)
    private String descripcion;

    @NotNull(message = "El precio no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
    @Digits(integer = 10, fraction = 3, message = "El precio debe tener un máximo de 10 dígitos enteros y 3 decimales")
    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal precio;

    @NotNull(message = "El costo no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = false, message = "El costo debe ser mayor que 0")
    @Digits(integer = 10, fraction = 3, message = "El costo debe tener un máximo de 10 dígitos enteros y 3 decimales")
    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal costo;

    @Size(max = 13, message = "El código de barras debe tener un máximo de 13 caracteres")
    @Column(name = "codigo_barra", length = 13, unique = true)
    private String codigoBarras;

    @Size(max = 50, message = "El número de serie debe tener un máximo de 50 caracteres")
    @Column(name = "numero_serie", length = 50, unique = true)
    private String numeroDeSerie;

    @NotNull(message = "El stock actual no puede estar vacío")
    @PositiveOrZero(message = "El stock actual debe ser un número positivo o cero")
    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual;

    @NotNull(message = "El stock mínimo no puede estar vacío")
    @PositiveOrZero(message = "El stock mínimo debe ser un número positivo o cero")
    @Column(nullable = false)
    private Integer stockMinimo;

    @Size(max = 17, message = "La dirección MAC debe tener un máximo de 17 caracteres")
    @Column(name = "mac_address", length = 17, unique = true)
    private String macAddress;

    // Relaciones

    // Relación muchos a uno con la entidad Categoria (un producto pertenece a una categoría)
    @NotNull(message = "La categoría no puede ser nula")
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // Relación uno a muchos con la entidad Stock (un producto tiene muchos stocks)
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stock> stocks = new ArrayList<>();

    // Relación muchos a muchos con la entidad Proveedor (un producto tiene muchos proveedores)
    @ManyToMany
    @JoinTable(name = "producto_proveedor", joinColumns = @JoinColumn(name = "producto_id"), inverseJoinColumns = @JoinColumn(name = "proveedor_id"))
    private List<Proveedor> proveedores = new ArrayList<>();

    // Relación uno a muchos con la entidad DetalleCompra (un producto tiene muchos detalles de compra)
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCompra> detallesCompra = new ArrayList<>();

    // Relación uno a muchos con la entidad DetalleVenta (un producto tiene muchos detalles
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detallesVenta = new ArrayList<>();

    // Relación uno a muchos con la entidad HistorialStock (un producto tiene muchos historiales de stock)
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialStock> historialStocks = new ArrayList<>();

    // Constructor vacío requerido por JPA
    public Producto() {
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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public List<Proveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }

    public List<DetalleCompra> getDetallesCompra() {
        return detallesCompra;
    }

    public void setDetallesCompra(List<DetalleCompra> detallesCompra) {
        this.detallesCompra = detallesCompra;
    }

    public List<DetalleVenta> getDetallesVenta() {
        return detallesVenta;
    }

    public void setDetallesVenta(List<DetalleVenta> detallesVenta) {
        this.detallesVenta = detallesVenta;
    }

    public List<HistorialStock> getHistorialStocks() {
        return historialStocks;
    }

    public void setHistorialStocks(List<HistorialStock> historialStocks) {
        this.historialStocks = historialStocks;
    }
}