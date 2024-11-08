package com.interonda.Inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del proveedor no puede estar vacío") // El campo no puede ser nulo ni vacío
    @Size(max = 100, message = "El nombre del proveedor no puede exceder los 100 caracteres") // Longitud máxima
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El contacto del proveedor no puede estar vacío") // Este campo también debe ser obligatorio
    @Size(max = 50, message = "El contacto del proveedor no puede exceder los 50 caracteres")
    @Column(length = 50)
    private String contacto;

    @Size(max = 255, message = "La dirección no puede exceder los 255 caracteres") // Opcional, pero con longitud máxima
    @Column(length = 255)
    private String direccion;

    @Size(max = 50, message = "El país no puede exceder los 50 caracteres") // Opcional, pero con longitud máxima
    @Column(length = 50)
    private String pais;

    // Relaciones

    // Relación muchos-a-muchos con Producto
    @ManyToMany
    @JoinTable(
            name = "proveedor_producto",
            joinColumns = @JoinColumn(name = "proveedor_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private List<Producto> productos;

    // Relación uno-a-muchos con Compra
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Compra> compras;

    // Constructor vacío requerido por JPA
    public Proveedor() {
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

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }
}

