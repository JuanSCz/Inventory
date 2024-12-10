/*
package com.interonda.inventory.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.interonda.inventory.entity.Categoria;
import com.interonda.inventory.dto.CategoriaDTO;
import com.interonda.inventory.exceptions.ConflictException;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.CategoriaMapper;
import com.interonda.inventory.repository.CategoriaRepository;
import com.interonda.inventory.service.impl.CategoriaServiceImpl;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private Validator validator;

    @Mock
    private CategoriaMapper categoriaMapper;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    private CategoriaDTO categoriaDTO;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoriaDTO = new CategoriaDTO();
        categoriaDTO.setNombre("Categoria Test");
        categoriaDTO.setId(1L);

        categoria = new Categoria();
        categoria.setNombre("Categoria Test");
        categoria.setId(1L);
    }

    @Test
    void createCategoria_ShouldCreateCategoriaSuccessfully() {
        // Arrange
        when(categoriaRepository.existsByNombre(categoriaDTO.getNombre())).thenReturn(false);
        when(categoriaMapper.toEntity(categoriaDTO)).thenReturn(categoria);
        when(categoriaRepository.save(categoria)).thenReturn(categoria);
        when(categoriaMapper.toDto(categoria)).thenReturn(categoriaDTO);

        // Act
        CategoriaDTO result = categoriaService.createCategoria(categoriaDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Categoria Test", result.getNombre());
        verify(validator).validate(categoriaDTO);
        verify(categoriaRepository).existsByNombre(categoriaDTO.getNombre());
        verify(categoriaRepository).save(categoria);
        verify(categoriaMapper).toDto(categoria);
    }

    @Test
    void createCategoria_ShouldThrowConflictException_WhenCategoriaExists() {
        // Arrange
        when(categoriaRepository.existsByNombre(categoriaDTO.getNombre())).thenReturn(true);

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> categoriaService.createCategoria(categoriaDTO));

        // Verify
        assertEquals("El nombre de la categoria ya existe", exception.getMessage());
        verify(validator).validate(categoriaDTO);
        verify(categoriaRepository).existsByNombre(categoriaDTO.getNombre());
        verifyNoMoreInteractions(categoriaRepository);
    }

    @Test
    void createCategoria_ShouldThrowDataAccessException_OnUnexpectedError() {
        // Arrange
        when(categoriaRepository.existsByNombre(categoriaDTO.getNombre())).thenReturn(false);
        when(categoriaMapper.toEntity(categoriaDTO)).thenReturn(categoria);
        when(categoriaRepository.save(categoria)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> categoriaService.createCategoria(categoriaDTO));

        // Verify
        assertTrue(exception.getMessage().contains("Error creando Categoria"));
        verify(validator).validate(categoriaDTO);
        verify(categoriaRepository).existsByNombre(categoriaDTO.getNombre());
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void createCategoria_ShouldThrowValidationException_OnValidationFailure() {
        // Arrange
        doThrow(new ValidationException("Invalid data")).when(validator).validate(categoriaDTO);

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> categoriaService.createCategoria(categoriaDTO));

        // Verify
        assertEquals("Invalid data", exception.getMessage());
        verify(validator).validate(categoriaDTO);
        verifyNoInteractions(categoriaRepository);
    }

    @Test
    void updateCategoria_ShouldUpdateCategoriaSuccessfully() {
        // Arrange
        when(categoriaRepository.existsByNombreAndIdNot(categoriaDTO.getNombre(), categoriaDTO.getId())).thenReturn(false);
        when(categoriaRepository.findById(categoriaDTO.getId())).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(categoria)).thenReturn(categoria);
        when(categoriaMapper.toDto(categoria)).thenReturn(categoriaDTO);

        // Act
        CategoriaDTO result = categoriaService.updateCategoria(categoriaDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Categoria Test", result.getNombre());
        verify(validator).validate(categoriaDTO);
        verify(categoriaRepository).existsByNombreAndIdNot(categoriaDTO.getNombre(), categoriaDTO.getId());
        verify(categoriaRepository).findById(categoriaDTO.getId());
        verify(categoriaRepository).save(categoria);
        verify(categoriaMapper).toDto(categoria);
    }

    @Test
    void updateCategoria_ShouldThrowConflictException_WhenCategoriaExists() {
        // Arrange
        when(categoriaRepository.existsByNombreAndIdNot(categoriaDTO.getNombre(), categoriaDTO.getId())).thenReturn(true);

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> categoriaService.updateCategoria(categoriaDTO));

        // Verify
        assertEquals("El nombre de la categoria ya existe", exception.getMessage());
        verify(validator).validate(categoriaDTO);
        verify(categoriaRepository).existsByNombreAndIdNot(categoriaDTO.getNombre(), categoriaDTO.getId());
        verifyNoMoreInteractions(categoriaRepository);
    }

    @Test
    void updateCategoria_ShouldThrowResourceNotFoundException_WhenCategoriaNotFound() {
        // Arrange
        when(categoriaRepository.existsByNombreAndIdNot(categoriaDTO.getNombre(), categoriaDTO.getId())).thenReturn(false);
        when(categoriaRepository.findById(categoriaDTO.getId())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> categoriaService.updateCategoria(categoriaDTO));

        // Verify
        assertEquals("Categoria no encontrada con el id: " + categoriaDTO.getId(), exception.getMessage());
        verify(validator).validate(categoriaDTO);
        verify(categoriaRepository).existsByNombreAndIdNot(categoriaDTO.getNombre(), categoriaDTO.getId());
        verify(categoriaRepository).findById(categoriaDTO.getId());
        verifyNoMoreInteractions(categoriaRepository);
    }

    @Test
    void updateCategoria_ShouldThrowDataAccessException_OnUnexpectedError() {
        // Arrange
        when(categoriaRepository.existsByNombreAndIdNot(categoriaDTO.getNombre(), categoriaDTO.getId())).thenReturn(false);
        when(categoriaRepository.findById(categoriaDTO.getId())).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(categoria)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> categoriaService.updateCategoria(categoriaDTO));

        // Verify
        assertTrue(exception.getMessage().contains("Error actualizando Categoria"));
        verify(validator).validate(categoriaDTO);
        verify(categoriaRepository).existsByNombreAndIdNot(categoriaDTO.getNombre(), categoriaDTO.getId());
        verify(categoriaRepository).findById(categoriaDTO.getId());
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void deleteCategoria_ShouldDeleteCategoriaSuccessfully() {
        // Arrange
        when(categoriaRepository.existsById(categoriaDTO.getId())).thenReturn(true);

        // Act
        categoriaService.deleteCategoria(categoriaDTO.getId());

        // Verify
        verify(categoriaRepository).existsById(categoriaDTO.getId());
        verify(categoriaRepository).deleteById(categoriaDTO.getId());
    }

    @Test
    void deleteCategoria_ShouldThrowResourceNotFoundException_WhenCategoriaNotFound() {
        // Arrange
        when(categoriaRepository.existsById(categoriaDTO.getId())).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> categoriaService.deleteCategoria(categoriaDTO.getId()));

        // Verify
        assertEquals("Categoria no encontrada con el id: " + categoriaDTO.getId(), exception.getMessage());
        verify(categoriaRepository).existsById(categoriaDTO.getId());
        verifyNoMoreInteractions(categoriaRepository);
    }

    @Test
    void deleteCategoria_ShouldThrowDataAccessException_OnUnexpectedError() {
        // Arrange
        when(categoriaRepository.existsById(categoriaDTO.getId())).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(categoriaRepository).deleteById(categoriaDTO.getId());

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> categoriaService.deleteCategoria(categoriaDTO.getId()));

        // Verify
        assertTrue(exception.getMessage().contains("Error eliminando Categoria"));
        verify(categoriaRepository).existsById(categoriaDTO.getId());
        verify(categoriaRepository).deleteById(categoriaDTO.getId());
    }

    @Test
    void getAllCategorias_ShouldReturnAllCategorias() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Categoria> categoriaPage = new PageImpl<>(Collections.singletonList(categoria), pageable, 1);
        when(categoriaRepository.findAll(pageable)).thenReturn(categoriaPage);
        when(categoriaMapper.toDto(categoria)).thenReturn(categoriaDTO);

        // Act
        Page<CategoriaDTO> result = categoriaService.getAllCategorias(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(categoriaRepository).findAll(pageable);
        verify(categoriaMapper).toDto(categoria);
    }

    @Test
    void getCategoriaById_ShouldReturnCategoriaById() {
        // Arrange
        when(categoriaRepository.findById(categoriaDTO.getId())).thenReturn(Optional.of(categoria));
        when(categoriaMapper.toDto(categoria)).thenReturn(categoriaDTO);

        // Act
        CategoriaDTO result = categoriaService.getCategoria(categoriaDTO.getId());

        // Assert
        assertNotNull(result);
        assertEquals("Categoria Test", result.getNombre());
        verify(categoriaRepository).findById(categoriaDTO.getId());
        verify(categoriaMapper).toDto(categoria);
    }

    @Test
    void getCategoriaById_ShouldThrowResourceNotFoundException_WhenCategoriaNotFound() {
        // Arrange
        when(categoriaRepository.findById(categoriaDTO.getId())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> categoriaService.getCategoria(categoriaDTO.getId()));

        // Verify
        assertEquals("Categoria no encontrada con el id: " + categoriaDTO.getId(), exception.getMessage());
        verify(categoriaRepository).findById(categoriaDTO.getId());
    }
} */
