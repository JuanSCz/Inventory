// CategoriaServiceImpl.java
package com.interonda.inventory.service.impl;

import com.interonda.inventory.dto.CategoriaDTO;
import com.interonda.inventory.entity.Categoria;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.CategoriaMapper;
import com.interonda.inventory.repository.CategoriaRepository;
import com.interonda.inventory.service.CategoriaService;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.utils.ValidatorUtils;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaServiceImpl.class);

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    private final Validator validator;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper, Validator validator) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
        this.validator = validator;
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaDTO getCategoria(Long id) {
        try {
            logger.info("Obteniendo Categoria con id: {}", id);
            Categoria categoria = categoriaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con el id: " + id));
            return categoriaMapper.toDto(categoria);
        } catch (ResourceNotFoundException e) {
            logger.warn("Categoria no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo Categoria", e);
            throw new DataAccessException("Error obteniendo Categoria", e);
        }
    }

    @Override
    @Transactional
    public CategoriaDTO createCategoria(CategoriaDTO categoriaDTO) {
        ValidatorUtils.validateEntity(categoriaDTO, validator);
        try {
            logger.info("Creando nueva Categoria");
            Categoria categoria = categoriaMapper.toEntity(categoriaDTO);
            Categoria savedCategoria = categoriaRepository.save(categoria);
            logger.info("Categoria creada exitosamente con id: {}", savedCategoria.getId());
            return categoriaMapper.toDto(savedCategoria);
        } catch (Exception e) {
            logger.error("Error guardando Categoria", e);
            throw new DataAccessException("Error guardando Categoria", e);
        }
    }

    @Override
    @Transactional
    public CategoriaDTO updateCategoria(CategoriaDTO categoriaDTO) {
        ValidatorUtils.validateEntity(categoriaDTO, validator);
        try {
            logger.info("Actualizando Categoria con id: {}", categoriaDTO.getId());
            Categoria categoria = categoriaRepository.findById(categoriaDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con el id: " + categoriaDTO.getId()));
            categoria = categoriaMapper.toEntity(categoriaDTO);
            Categoria updatedCategoria = categoriaRepository.save(categoria);
            logger.info("Categoria actualizada exitosamente con id: {}", updatedCategoria.getId());
            return categoriaMapper.toDto(updatedCategoria);
        } catch (ResourceNotFoundException e) {
            logger.warn("Categoria no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error actualizando Categoria", e);
            throw new DataAccessException("Error actualizando Categoria", e);
        }
    }

    @Override
    @Transactional
    public boolean deleteCategoria(Long id) {
        try {
            logger.info("Eliminando Categoria con id: {}", id);
            if (!categoriaRepository.existsById(id)) {
                throw new ResourceNotFoundException("Categoria no encontrada con el id: " + id);
            }
            categoriaRepository.deleteById(id);
            logger.info("Categoria eliminada exitosamente con id: {}", id);
            return true;
        } catch (ResourceNotFoundException e) {
            logger.warn("Categoria no encontrada: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error eliminando Categoria", e);
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaDTO> getAllCategorias(Pageable pageable) {
        try {
            logger.info("Obteniendo todas las Categorias con paginación");
            Page<Categoria> categorias = categoriaRepository.findAll(pageable);
            return categorias.map(categoriaMapper::toDto);
        } catch (Exception e) {
            logger.error("Error obteniendo todas las Categorias con paginación", e);
            throw new DataAccessException("Error obteniendo todas las Categorias con paginación", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countCategorias() {
        try {
            long total = categoriaRepository.count();
            logger.info("Total de categorias: {}", total);
            return total;
        } catch (Exception e) {
            logger.error("Error contando todas las Categorias", e);
            throw new DataAccessException("Error contando todas las Categorias", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaDTO> searchCategoriasByName(String nombre, Pageable pageable) {
        try {
            logger.info("Buscando Categorias por nombre: {}", nombre);
            Page<Categoria> categorias = categoriaRepository.findByNombreContainingIgnoreCase(nombre, pageable);
            return categorias.map(categoriaMapper::toDto);
        } catch (Exception e) {
            logger.error("Error buscando Categorias por nombre", e);
            throw new DataAccessException("Error buscando Categorias por nombre", e);
        }
    }
}