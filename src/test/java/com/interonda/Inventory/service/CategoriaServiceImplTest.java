package com.interonda.Inventory.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.interonda.Inventory.entity.Categoria;
import com.interonda.Inventory.entityDTO.CategoriaDTO;
import com.interonda.Inventory.exceptions.ConflictException;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.CategoriaMapper;
import com.interonda.Inventory.repository.CategoriaRepository;
import com.interonda.Inventory.service.impl.CategoriaServiceImpl;
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
        when(categoriaRepository.existsByNombre(categoriaDTO.getNombre())).thenReturn(false);
        when(categoriaMapper.toEntity(categoriaDTO)).thenReturn(categoria);
        when(categoriaRepository.save(categoria)).thenReturn(categoria);
        when(categoriaMapper.toDto(categoria)).thenReturn(categoriaDTO);

        CategoriaDTO result = categoriaService.createCategoria(categoriaDTO);

        assertNotNull(result);
        assertEquals("Categoria Test", result.getNombre());
        verify(validator).validate(categoriaDTO);
        verify(categoriaRepository).existsByNombre(categoriaDTO.getNombre());
        verify(categoriaRepository).save(categoria);
        verify(categoriaMapper).toDto(categoria);
    }

    @Test
    void createCategoria_ShouldThrowConflictException_WhenCategoriaExists() {
        when(categoriaRepository.existsByNombre(categoriaDTO.getNombre())).thenReturn(true);

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> categoriaService.createCategoria(categoriaDTO)
        );

        assertEquals("El nombre de la categoria ya existe", exception.getMessage());
        verify(validator).validate(categoriaDTO);
        verify(categoriaRepository).existsByNombre(categoriaDTO.getNombre());
        verifyNoMoreInteractions(categoriaRepository);
    }

    @Test
    void createCategoria_ShouldThrowDataAccessException_OnUnexpectedError() {
        when(categoriaRepository.existsByNombre(categoriaDTO.getNombre())).thenReturn(false);
        when(categoriaMapper.toEntity(categoriaDTO)).thenReturn(categoria);
        when(categoriaRepository.save(categoria)).thenThrow(new RuntimeException("Database error"));

        DataAccessException exception = assertThrows(
                DataAccessException.class,
                () -> categoriaService.createCategoria(categoriaDTO)
        );

        assertTrue(exception.getMessage().contains("Error creando Categoria"));
        verify(validator).validate(categoriaDTO);
        verify(categoriaRepository).existsByNombre(categoriaDTO.getNombre());
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void createCategoria_ShouldThrowValidationException_OnValidationFailure() {
        doThrow(new ValidationException("Invalid data"))
                .when(validator).validate(categoriaDTO);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> categoriaService.createCategoria(categoriaDTO)
        );

        assertEquals("Invalid data", exception.getMessage());
        verify(validator).validate(categoriaDTO);
        verifyNoInteractions(categoriaRepository);
    }

    @Test
    void updateCategoria_ShouldUpdateCategoriaSuccessfully() {
        when(categoriaRepository.existsByNombreAndIdNot(categoriaDTO.getNombre(), categoriaDTO.getId())).thenReturn(false);
        when(categoriaRepository.findById(categoriaDTO.getId())).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(categoria)).thenReturn(categoria);
        when(categoriaMapper.toDto(categoria)).thenReturn(categoriaDTO);

        CategoriaDTO result = categoriaService.updateCategoria(categoriaDTO.getId(), categoriaDTO);

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
        when(categoriaRepository.existsByNombreAndIdNot(categoriaDTO.getNombre(), categoriaDTO.getId())).thenReturn(true);

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> categoriaService.updateCategoria(categoriaDTO.getId(), categoriaDTO)
        );

        assertEquals("El nombre de la categoria ya existe", exception.getMessage());
        verify(validator).validate(categoriaDTO);
        verify(categoriaRepository).existsByNombreAndIdNot(categoriaDTO.getNombre(), categoriaDTO.getId());
        verifyNoMoreInteractions(categoriaRepository);
    }

    @Test
    void updateCategoria_ShouldThrowResourceNotFoundException_WhenCategoriaNotFound() {
        when(categoriaRepository.existsByNombreAndIdNot(categoriaDTO.getNombre(), categoriaDTO.getId())).thenReturn(false);
        when(categoriaRepository.findById(categoriaDTO.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoriaService.updateCategoria(categoriaDTO.getId(), categoriaDTO)
        );

        assertEquals("Categoria no encontrada con el id: " + categoriaDTO.getId(), exception.getMessage());
        verify(validator).validate(categoriaDTO);
        verify(categoriaRepository).existsByNombreAndIdNot(categoriaDTO.getNombre(), categoriaDTO.getId());
        verify(categoriaRepository).findById(categoriaDTO.getId());
        verifyNoMoreInteractions(categoriaRepository);
    }

    @Test
    void updateCategoria_ShouldThrowDataAccessException_OnUnexpectedError() {
        when(categoriaRepository.existsByNombreAndIdNot(categoriaDTO.getNombre(), categoriaDTO.getId())).thenReturn(false);
        when(categoriaRepository.findById(categoriaDTO.getId())).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(categoria)).thenThrow(new RuntimeException("Database error"));

        DataAccessException exception = assertThrows(
                DataAccessException.class,
                () -> categoriaService.updateCategoria(categoriaDTO.getId(), categoriaDTO)
        );

        assertTrue(exception.getMessage().contains("Error actualizando Categoria"));
        verify(validator).validate(categoriaDTO);
        verify(categoriaRepository).existsByNombreAndIdNot(categoriaDTO.getNombre(), categoriaDTO.getId());
        verify(categoriaRepository).findById(categoriaDTO.getId());
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void deleteCategoria_ShouldDeleteCategoriaSuccessfully() {
        when(categoriaRepository.existsById(categoriaDTO.getId())).thenReturn(true);

        categoriaService.deleteCategoria(categoriaDTO.getId());

        verify(categoriaRepository).existsById(categoriaDTO.getId());
        verify(categoriaRepository).deleteById(categoriaDTO.getId());
    }

    @Test
    void deleteCategoria_ShouldThrowResourceNotFoundException_WhenCategoriaNotFound() {
        when(categoriaRepository.existsById(categoriaDTO.getId())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoriaService.deleteCategoria(categoriaDTO.getId())
        );

        assertEquals("Categoria no encontrada con el id: " + categoriaDTO.getId(), exception.getMessage());
        verify(categoriaRepository).existsById(categoriaDTO.getId());
        verifyNoMoreInteractions(categoriaRepository);
    }

    @Test
    void deleteCategoria_ShouldThrowDataAccessException_OnUnexpectedError() {
        when(categoriaRepository.existsById(categoriaDTO.getId())).thenReturn(true);
        doThrow(new RuntimeException("Database error"))
                .when(categoriaRepository).deleteById(categoriaDTO.getId());

        DataAccessException exception = assertThrows(
                DataAccessException.class,
                () -> categoriaService.deleteCategoria(categoriaDTO.getId())
        );

        assertTrue(exception.getMessage().contains("Error eliminando Categoria"));
        verify(categoriaRepository).existsById(categoriaDTO.getId());
        verify(categoriaRepository).deleteById(categoriaDTO.getId());
    }

    @Test
    void getAllCategorias_ShouldReturnAllCategorias() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Categoria> categoriaPage = new PageImpl<>(Collections.singletonList(categoria), pageable, 1);
        when(categoriaRepository.findAll(pageable)).thenReturn(categoriaPage);
        when(categoriaMapper.toDto(categoria)).thenReturn(categoriaDTO);

        Page<CategoriaDTO> result = categoriaService.getAllCategorias(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(categoriaRepository).findAll(pageable);
        verify(categoriaMapper).toDto(categoria);
    }

    @Test
    void getCategoriaById_ShouldReturnCategoriaById() {
        when(categoriaRepository.findById(categoriaDTO.getId())).thenReturn(Optional.of(categoria));
        when(categoriaMapper.toDto(categoria)).thenReturn(categoriaDTO);

        CategoriaDTO result = categoriaService.getCategoriaById(categoriaDTO.getId());

        assertNotNull(result);
        assertEquals("Categoria Test", result.getNombre());
        verify(categoriaRepository).findById(categoriaDTO.getId());
        verify(categoriaMapper).toDto(categoria);
    }

    @Test
    void getCategoriaById_ShouldThrowResourceNotFoundException_WhenCategoriaNotFound() {
        when(categoriaRepository.findById(categoriaDTO.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoriaService.getCategoriaById(categoriaDTO.getId())
        );

        assertEquals("Categoria no encontrada con el id: " + categoriaDTO.getId(), exception.getMessage());
        verify(categoriaRepository).findById(categoriaDTO.getId());
    }
}
