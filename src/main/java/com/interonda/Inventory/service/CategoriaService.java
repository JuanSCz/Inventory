package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Categoria;

import java.util.List;

public interface CategoriaService {
    List<Categoria> findAll();

    Categoria findById(Long id);

    Categoria save(Categoria categoria);

    void deleteById(Long id);

    void editarNombreCategoria(Long idCategoria, String nuevoNombre);

    Categoria crearCategoria(Categoria categoria);
}
