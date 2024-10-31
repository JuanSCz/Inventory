package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<Categoria> findAll();

    Optional<Categoria> findById(Long id);

    Categoria save(Categoria categoria);

    void deleteById(Long id);
}
