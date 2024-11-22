package com.interonda.Inventory.serviceImplTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.interonda.Inventory.entity.Proveedor;
import com.interonda.Inventory.dto.ProveedorDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.ProveedorMapper;
import com.interonda.Inventory.repository.ProveedorRepository;
import com.interonda.Inventory.serviceImplTest.impl.ProveedorServiceImpl;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

class ProveedorServiceImplTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @Mock
    private ProveedorMapper proveedorMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private ProveedorServiceImpl proveedorService;

    private ProveedorDTO proveedorDTO;
    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        proveedorDTO = new ProveedorDTO();
        proveedorDTO.setNombre("Proveedor Test");
        proveedor = new Proveedor();
        proveedor.setNombre("Proveedor Test");
    }

    @Test
    void createProveedor_Success() {
        // Arrange
        when(proveedorMapper.toEntity(any(ProveedorDTO.class))).thenReturn(proveedor);
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);
        when(proveedorMapper.toDto(any(Proveedor.class))).thenReturn(proveedorDTO);

        // Act
        ProveedorDTO result = proveedorService.createProveedor(proveedorDTO);

        // Assert
        assertNotNull(result, "El resultado no debería ser nulo");
        assertEquals("Proveedor Test", result.getNombre(), "El nombre del proveedor debería ser 'Proveedor Test'");
        verify(proveedorRepository, times(1)).save(any(Proveedor.class));
        verify(proveedorMapper, times(1)).toEntity(any(ProveedorDTO.class));
        verify(proveedorMapper, times(1)).toDto(any(Proveedor.class));
    }

    @Test
    void createProveedor_DataAccessException() {
        // Arrange
        when(proveedorMapper.toEntity(any(ProveedorDTO.class))).thenReturn(proveedor);
        when(proveedorRepository.save(any(Proveedor.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> proveedorService.createProveedor(proveedorDTO));
        assertEquals("Error guardando Proveedor", exception.getMessage(), "El mensaje de la excepción debería ser 'Error guardando Proveedor'");
        verify(proveedorRepository, times(1)).save(any(Proveedor.class));
    }

    @Test
    void createProveedor_InvalidDTO() {
        // Arrange
        doThrow(new RuntimeException("Validation error")).when(validator).validate(any(ProveedorDTO.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> proveedorService.createProveedor(proveedorDTO));
        assertEquals("Validation error", exception.getMessage(), "The exception message should be 'Validation error'");
        verify(proveedorRepository, never()).save(any(Proveedor.class));
    }

    @Test
    void updateProveedor_Success() {
        // Arrange
        proveedorDTO.setId(1L); // Assign a valid ID to proveedorDTO
        when(proveedorRepository.findById(anyLong())).thenReturn(Optional.of(proveedor));
        when(proveedorMapper.toEntity(any(ProveedorDTO.class))).thenReturn(proveedor);
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);
        when(proveedorMapper.toDto(any(Proveedor.class))).thenReturn(proveedorDTO);

        // Act
        ProveedorDTO result = proveedorService.updateProveedor(proveedorDTO);

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals("Proveedor Test", result.getNombre(), "The provider name should be 'Proveedor Test'");
        verify(proveedorRepository, times(1)).findById(anyLong());
        verify(proveedorRepository, times(1)).save(any(Proveedor.class));
        verify(proveedorMapper, times(1)).toEntity(any(ProveedorDTO.class));
        verify(proveedorMapper, times(1)).toDto(any(Proveedor.class));
    }

    @Test
    void updateProveedor_ProveedorNotFound() {
        // Arrange
        proveedorDTO.setId(1L); // Assign a valid ID to proveedorDTO
        when(proveedorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> proveedorService.updateProveedor(proveedorDTO));
        assertEquals("Proveedor no encontrado con el id: 1", exception.getMessage(), "The exception message should be 'Proveedor no encontrado con el id: 1'");
        verify(proveedorRepository, times(1)).findById(anyLong());
        verify(proveedorRepository, never()).save(any(Proveedor.class));
    }

    @Test
    void updateProveedor_DataAccessException() {
        // Arrange
        proveedorDTO.setId(1L); // Assign a valid ID to proveedorDTO
        when(proveedorRepository.findById(anyLong())).thenReturn(Optional.of(proveedor));
        when(proveedorMapper.toEntity(any(ProveedorDTO.class))).thenReturn(proveedor);
        when(proveedorRepository.save(any(Proveedor.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> proveedorService.updateProveedor(proveedorDTO));
        assertEquals("Error actualizando Proveedor", exception.getMessage(), "The exception message should be 'Error actualizando Proveedor'");
        verify(proveedorRepository, times(1)).findById(anyLong());
        verify(proveedorRepository, times(1)).save(any(Proveedor.class));
    }

    @Test
    void updateProveedor_InvalidDTO() {
        // Arrange
        doThrow(new RuntimeException("Validation error")).when(validator).validate(any(ProveedorDTO.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> proveedorService.updateProveedor(proveedorDTO));
        assertEquals("Validation error", exception.getMessage(), "The exception message should be 'Validation error'");
        verify(proveedorRepository, never()).findById(anyLong());
        verify(proveedorRepository, never()).save(any(Proveedor.class));
    }

    @Test
    void deleteProveedor_Success() {
        // Arrange
        when(proveedorRepository.existsById(anyLong())).thenReturn(true);

        // Act
        proveedorService.deleteProveedor(1L);

        // Assert
        verify(proveedorRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteProveedor_ProveedorNotFound() {
        // Arrange
        when(proveedorRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> proveedorService.deleteProveedor(1L));
        assertEquals("Proveedor no encontrado con el id: 1", exception.getMessage(), "The exception message should be 'Proveedor no encontrado con el id: 1'");
        verify(proveedorRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteProveedor_DataAccessException() {
        // Arrange
        when(proveedorRepository.existsById(anyLong())).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(proveedorRepository).deleteById(anyLong());

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> proveedorService.deleteProveedor(1L));
        assertEquals("Error eliminando Proveedor", exception.getMessage(), "The exception message should be 'Error eliminando Proveedor'");
        verify(proveedorRepository, times(1)).existsById(anyLong());
        verify(proveedorRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void getProveedor_Success() {
        // Arrange
        when(proveedorRepository.findById(anyLong())).thenReturn(Optional.of(proveedor));
        when(proveedorMapper.toDto(any(Proveedor.class))).thenReturn(proveedorDTO);

        // Act
        ProveedorDTO result = proveedorService.getProveedor(1L);

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals("Proveedor Test", result.getNombre(), "The provider name should be 'Proveedor Test'");
        verify(proveedorRepository, times(1)).findById(anyLong());
    }

    @Test
    void getProveedor_ProveedorNotFound() {
        // Arrange
        when(proveedorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> proveedorService.getProveedor(1L));
        assertEquals("Proveedor no encontrado con el id: 1", exception.getMessage(), "The exception message should be 'Proveedor no encontrado con el id: 1'");
        verify(proveedorRepository, times(1)).findById(anyLong());
    }

    @Test
    void getProveedor_DataAccessException() {
        // Arrange
        when(proveedorRepository.findById(anyLong())).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> proveedorService.getProveedor(1L));
        assertEquals("Error obteniendo Proveedor", exception.getMessage(), "The exception message should be 'Error obteniendo Proveedor'");
        verify(proveedorRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAllProveedores_Success() {
        // Arrange
        Page<Proveedor> proveedores = new PageImpl<>(List.of(proveedor));
        when(proveedorRepository.findAll(any(Pageable.class))).thenReturn(proveedores);
        when(proveedorMapper.toDto(any(Proveedor.class))).thenReturn(proveedorDTO);

        // Act
        Page<ProveedorDTO> result = proveedorService.getAllProveedores(PageRequest.of(0, 10));

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(1, result.getTotalElements(), "The total number of elements should be 1");
        assertEquals("Proveedor Test", result.getContent().get(0).getNombre(), "The provider name should be 'Proveedor Test'");
        verify(proveedorRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getAllProveedores_DataAccessException() {
        // Arrange
        when(proveedorRepository.findAll(any(Pageable.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> proveedorService.getAllProveedores(PageRequest.of(0, 10)));
        assertEquals("Error obteniendo todos los Proveedores con paginación", exception.getMessage(), "The exception message should be 'Error obteniendo todos los Proveedores con paginación'");
        verify(proveedorRepository, times(1)).findAll(any(Pageable.class));
    }
}