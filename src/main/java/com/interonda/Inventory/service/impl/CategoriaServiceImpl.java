package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Categoria;
import com.interonda.Inventory.exception.ResourceNotFoundException;
import com.interonda.Inventory.repository.CategoriaRepository;
import com.interonda.Inventory.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServiceImpl implements CategoriaService {


    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    @Override
    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById(id)
                .or(() -> {
                    throw new ResourceNotFoundException("Categoria no encontrada con id: " + id);
                });
    }

    @Override
    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public void deleteById(Long id) {
        categoriaRepository.deleteById(id);
    }
}
