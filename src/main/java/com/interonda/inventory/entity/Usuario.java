package com.interonda.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre debe tener un máximo de 50 caracteres")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Column(name = "contraseña", nullable = false)
    private String contrasenia;

    @NotBlank(message = "El contacto no puede estar vacío")
    @Size(max = 15, message = "El contacto debe tener un máximo de 15 caracteres")
    @Column(length = 15, nullable = false)
    private String contacto;

    // Relación uno-a-muchos con HistorialStock (un usuario puede tener muchos historiales de stock)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialStock> historialStock = new ArrayList<>();

    //Relación muchos-a-uno con Rol (muchos usuarios pueden tener un rol)
    @NotNull(message = "El rol no puede ser nulo")
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


