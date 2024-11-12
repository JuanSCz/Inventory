package com.interonda.Inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    @Column(nullable = false, length = 50)
    private String nombre;

    @Size(max = 15, message = "El contacto no puede tener más de 15 caracteres")
    @Column(name = "contacto", length = 15)
    private String contactoProveedor;

    @Size(max = 50, message = "La dirección no puede tener más de 50 caracteres")
    @Column(length = 50)
    private String direccion;

    @Length(max = 254, message = "El país no puede tener más de 254 caracteres")
    @Column(length = 254)
    private String pais;

    @Size(max = 254, message = "El e-mail no puede tener más de 254 caracteres")
    @Email(message = "El e-mail debe ser válido")
    @Column(name = "email", length = 254)
    private String eEmailProveedor;

    // Relaciones

    // Relación muchos-a-muchos con Producto
    @ManyToMany
    @JoinTable(name = "proveedor_producto", joinColumns = @JoinColumn(name = "proveedor_id"), inverseJoinColumns = @JoinColumn(name = "producto_id"))
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

    public String geteEmailProveedor() {
        return eEmailProveedor;
    }

    public void seteEmailProveedor(String eEmailProveedor) {
        this.eEmailProveedor = eEmailProveedor;
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

