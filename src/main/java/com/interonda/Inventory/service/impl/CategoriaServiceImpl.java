package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Categoria;
import com.interonda.Inventory.entityDTO.CategoriaDTO;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.CategoriaMapper;
import com.interonda.Inventory.repository.CategoriaRepository;
import com.interonda.Inventory.service.CategoriaService;
import com.interonda.Inventory.exceptions.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaServiceImpl.class);

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    @Override
    public CategoriaDTO convertToDto(Categoria categoria) {
        return categoriaMapper.toDto(categoria);
    }

    @Override
    public Categoria convertToEntity(CategoriaDTO categoriaDTO) {
        return categoriaMapper.toEntity(categoriaDTO);
    }

    @Override
    @Transactional
    public CategoriaDTO createCategoria(CategoriaDTO categoriaDTO) {
        try {
            logger.info("Creando nueva Categoria");
            Categoria categoria = convertToEntity(categoriaDTO);
            Categoria savedCategoria = categoriaRepository.save(categoria);
            logger.info("Categoria creada exitosamente con id: {}", savedCategoria.getId());
            return convertToDto(savedCategoria);
        } catch (Exception e) {
            logger.error("Error creando Categoria", e);
            throw new DataAccessException("Error creando Categoria", e);
        }
    }

    @Override
    @Transactional
    public CategoriaDTO updateCategoria(Long id, CategoriaDTO categoriaDTO) {
        try {
            logger.info("Actualizando Categoria con id: {}", id);
            Categoria categoria = categoriaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con el id: " + id));
            categoria.setNombre(categoriaDTO.getNombre());
            categoria.setDescripcion(categoriaDTO.getDescripcion());
            Categoria updatedCategoria = categoriaRepository.save(categoria);
            logger.info("Categoria actualizada exitosamente con id: {}", updatedCategoria.getId());
            return convertToDto(updatedCategoria);
        } catch (ResourceNotFoundException e) {
            logger.warn("Categoria no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error actualizando Categoria por id: " + id, e);
            throw new DataAccessException("Error actualizando Categoria por id: " + id, e);
        }
    }

    @Override
    @Transactional
    public void deleteCategoria(Long id) {
        try {
            logger.info("Eliminando Categoria con id: {}", id);
            if (!categoriaRepository.existsById(id)) {
                throw new ResourceNotFoundException("Categoria no encontrada con el id: " + id);
            }
            categoriaRepository.deleteById(id);
            logger.info("Categoria eliminada exitosamente con id: {}", id);
        } catch (ResourceNotFoundException e) {
            logger.warn("Categoria no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error eliminando Categoria por id: " + id, e);
            throw new DataAccessException("Error eliminando Categoria por id: " + id, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> getAllCategorias() {
        try {
            logger.info("Obteniendo todas las Categorias");
            return categoriaRepository.findAll().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error obteniendo todas las Categorias", e);
            throw new DataAccessException("Error obteniendo todas las Categorias", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaDTO getCategoriaById(Long id) {
        try {
            logger.info("Obteniendo Categoria con id: {}", id);
            Categoria categoria = categoriaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con el id: " + id));
            return convertToDto(categoria);
        } catch (ResourceNotFoundException e) {
            logger.warn("Categoria no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo Categoria por id: " + id, e);
            throw new DataAccessException("Error obteniendo Categoria por id: " + id, e);
        }
    }
}