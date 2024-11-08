package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Categoria;
import com.interonda.Inventory.exceptions.ConflictException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.repository.CategoriaRepository;
import com.interonda.Inventory.service.CategoriaService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CategoriaServiceImpl.class);

    @Autowired
    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("La categoría no fue encontrada!"));
    }

    @Transactional
    @Override
    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        categoriaRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Categoria crearCategoria(Categoria categoria) {
        // Comprobar si ya existe una categoría con el mismo nombre
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new ConflictException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        // Guardar la nueva categoría en la base de datos
        return categoriaRepository.save(categoria);
    }

    @Transactional
    @Override
    public void editarNombreCategoria(Long idCategoria, String nuevoNombre) {
        // Comprobar si ya existe una categoría con el mismo nombre
        if (categoriaRepository.existsByNombre(nuevoNombre)) {
            throw new ConflictException("Ya existe una categoría con el nombre: " + nuevoNombre);
        }
        // Llamar a la consulta JPQL para actualizar el nombre de la categoría
        categoriaRepository.updateNombre(idCategoria, nuevoNombre);
    }


}

