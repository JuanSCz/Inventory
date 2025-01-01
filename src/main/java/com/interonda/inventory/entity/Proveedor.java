package com.interonda.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre debe tener un máximo de 50 caracteres")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "El contacto no puede estar vacío")
    @Size(max = 15, message = "El contacto debe tener un máximo de 15 caracteres")
    @Column(name = "contacto", length = 15)
    private String contactoProveedor;

    @NotBlank(message = "La direccion no puede estar vacía")
    @Size(max = 50, message = "La dirección debe tener un máximo de 50 caracteres")
    @Column(length = 50)
    private String direccion;

    @NotBlank(message = "El pais no puede estar vacío")
    @Size(max = 254, message = "El país debe tener un máximo de 254 caracteres")
    @Column(length = 254)
    private String pais;

    @NotBlank(message = "El pais no puede estar vacío")
    @Email(message = "El email debe ser válido")
    @Size(max = 254, message = "El email debe tener un máximo de 254 caracteres")
    @Column(name = "email", length = 254)
    private String emailProveedor;

    // Relaciones

    // Relación muchos-a-muchos con Producto (un proveedor puede suministrar varios productos)
    @ManyToMany
    @JoinTable(name = "proveedor_producto", joinColumns = @JoinColumn(name = "proveedor_id"), inverseJoinColumns = @JoinColumn(name = "producto_id"))
    private List<Producto> productos = new ArrayList<>();

    // Relación uno-a-muchos con Compra (un proveedor puede tener varias compras)
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Compra> compras = new ArrayList<>();

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

    public String getContactoProveedor() {
        return contactoProveedor;
    }

    public void setContactoProveedor(String contactoProveedor) {
        this.contactoProveedor = contactoProveedor;
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

    public String getEmailProveedor() {
        return emailProveedor;
    }

    public void setEmailProveedor(String emailProveedor) {
        this.emailProveedor = emailProveedor;
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

