package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Categoria;
import com.interonda.Inventory.entityDTO.CategoriaDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.mapper.CategoriaMapper;
import com.interonda.Inventory.repository.CategoriaRepository;
import com.interonda.Inventory.service.impl.CategoriaServiceImpl;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoriaServiceImplTest {

    private static final String NOMBRE_NO_PUEDE_ESTAR_VACIO = "must not be blank";
    private static final String NOMBRE_LONGITUD_MAXIMA = "size must be between 0 and 50";
    private static final String NOMBRE_DUPLICADO = "El nombre de la categoria ya existe";
    private static final String ERROR_CREANDO_CATEGORIA = "Error creando Categoria";

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CategoriaMapper categoriaMapper;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private CategoriaDTO createCategoriaDTO(String nombre, String descripcion) {
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setNombre(nombre);
        categoriaDTO.setDescripcion(descripcion);
        return categoriaDTO;
    }

    private CategoriaDTO createCategoriaDTO(Long id, String nombre, String descripcion) {
        CategoriaDTO categoriaDTO = createCategoriaDTO(nombre, descripcion);
        categoriaDTO.setId(id);
        return categoriaDTO;
    }

    private Categoria createCategoriaEntity(Long id, String nombre, String descripcion) {
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);
        return categoria;
    }

    @Test
    void createCategoria_Success() {
        CategoriaDTO categoriaDTO = createCategoriaDTO("Electronics", "Description");
        Categoria categoria = createCategoriaEntity(1L, "Electronics", "Description");
        CategoriaDTO expectedCategoriaDTO = createCategoriaDTO(1L, "Electronics", "Description");

        when(categoriaMapper.toEntity(categoriaDTO)).thenReturn(categoria);
        when(categoriaMapper.toDto(categoria)).thenReturn(expectedCategoriaDTO);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        CategoriaDTO createdCategoriaDTO = categoriaService.createCategoria(categoriaDTO);

        assertAll("Categoria creada correctamente",
                () -> assertNotNull(createdCategoriaDTO),
                () -> assertEquals("Electronics", createdCategoriaDTO.getNombre()),
                () -> assertEquals("Description", createdCategoriaDTO.getDescripcion()),
                () -> assertEquals(1L, createdCategoriaDTO.getId())
        );

        verify(categoriaMapper).toEntity(categoriaDTO);
        verify(categoriaMapper).toDto(categoria);
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void createCategoria_NullCategoriaDTO() {
        var exception = assertThrows(IllegalArgumentException.class, () ->
                categoriaService.createCategoria(null)
        );
        assertEquals("El objeto CategoriaDTO no puede ser nulo", exception.getMessage());
    }

    @Test
    void createCategoria_NullNombre() {
        CategoriaDTO categoriaDTO = createCategoriaDTO(null, "Description");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                categoriaService.createCategoria(categoriaDTO)
        );

        assertEquals("El nombre de la categoria no puede ser nulo o estar vacío", exception.getMessage());
    }

    @Test
    void createCategoria_EmptyNombre() {
        CategoriaDTO categoriaDTO = createCategoriaDTO("", "Description");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                categoriaService.createCategoria(categoriaDTO)
        );

        assertEquals("El nombre de la categoria no puede ser nulo o estar vacío", exception.getMessage());
    }

    @Test
    void createCategoria_LongNombre() {
        CategoriaDTO categoriaDTO = createCategoriaDTO("A".repeat(51), "Description");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                categoriaService.createCategoria(categoriaDTO)
        );

        assertEquals("El nombre de la categoria no puede exceder 50 caracteres", exception.getMessage());
    }

    @Test
    void createCategoria_DuplicateNombre() {
        CategoriaDTO categoriaDTO = createCategoriaDTO("Electronics", "Description");

        when(categoriaRepository.existsByNombre("Electronics")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                categoriaService.createCategoria(categoriaDTO)
        );

        assertEquals("El nombre de la categoria ya existe", exception.getMessage());
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void createCategoria_DataAccessException() {
        CategoriaDTO categoriaDTO = createCategoriaDTO("Electronics", "Description");

        when(categoriaMapper.toEntity(categoriaDTO)).thenThrow(new RuntimeException("Database error"));

        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                categoriaService.createCategoria(categoriaDTO)
        );

        assertEquals("Error creando Categoria", exception.getMessage());
        verify(categoriaMapper).toEntity(categoriaDTO);
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void createCategoria_LongDescripcion() {
        CategoriaDTO categoriaDTO = createCategoriaDTO("Electronics", "A".repeat(76));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                categoriaService.createCategoria(categoriaDTO)
        );

        assertEquals("La descripción de la categoria no puede exceder 75 caracteres", exception.getMessage());
    }
}