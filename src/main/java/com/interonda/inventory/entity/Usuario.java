package com.interonda.inventory.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(name = "contraseña", nullable = false)
    private String contrasenia;

    @Column(length = 15, nullable = false)
    private String contacto;

    // Relación uno-a-muchos con HistorialStock (un usuario puede tener muchos historiales de stock)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialStock> historialStock = new ArrayList<>();

    //Relación muchos-a-uno con Rol (muchos usuarios pueden tener un rol)
    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    public Usuario() {
    }

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

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public List<HistorialStock> getHistorialStock() {
        return historialStock;
    }

    public void setHistorialStock(List<HistorialStock> historialStock) {
        this.historialStock = historialStock;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
}


