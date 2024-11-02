package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Categoria;
import com.interonda.Inventory.exception.ResourceNotFoundException;
import com.interonda.Inventory.repository.CategoriaRepository;
import com.interonda.Inventory.service.CategoriaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La categoría N " + id + " no fue encontrada!"));
    }

    @Override
    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public void deleteById(Long id) {
        categoriaRepository.deleteById(id);
    }

    @Override
    public void editarNombreCategoria(Long idCategoria, String nuevoNombre) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con el ID: " + idCategoria));

        categoria.setNombre(nuevoNombre);
        categoriaRepository.save(categoria);
    }
}

