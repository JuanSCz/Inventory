package com.interonda.Inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{usuario.nombre.notBlank}")
    @Size(max = 50, message = "{usuario.nombre.size}")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "{usuario.contrasenia.notBlank}")
    @Size(min = 8, message = "{usuario.contrasenia.size}")
    @Column(name = "contraseña", nullable = false)
    private String contrasenia;

    @Column(name = "foto_usuario")
    private byte[] imagenUsuario;

    @NotBlank(message = "{usuario.contacto.notBlank}")
    @Size(max = 15, message = "{usuario.contacto.size}")
    @Column(length = 15, nullable = false)
    private String contacto;

    // Relación uno-a-muchos con HistorialStock (un usuario puede tener muchos historiales de stock)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialStock> historialStock = new ArrayList<>();

    //Relación muchos-a-uno con Rol (muchos usuarios pueden tener un rol)
    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    public Usuario() {}

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

    public byte[] getImagenUsuario() {
        return imagenUsuario;
    }

    public void setImagenUsuario(byte[] imagenUsuario) {
        this.imagenUsuario = imagenUsuario;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
}


