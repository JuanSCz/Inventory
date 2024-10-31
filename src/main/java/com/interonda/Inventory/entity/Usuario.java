package com.interonda.Inventory.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private String rol;

    @Column(nullable = false)
    private String contraseña;

    @Column(nullable = true)
    private Integer imagenUsuario;

    // Relación uno-a-muchos con HistorialStock
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialStock> historialStock;

    // Constructor vacío requerido por JPA
    public Usuario() {
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public List<HistorialStock> getHistorialStock() {
        return historialStock;
    }

    public void setHistorialStock(List<HistorialStock> historialStock) {
        this.historialStock = historialStock;
    }

    public Integer getImagenUsuario() {
        return imagenUsuario;
    }

    public void setImagenUsuario(Integer imagenUsuario) {
        this.imagenUsuario = imagenUsuario;
    }
}

