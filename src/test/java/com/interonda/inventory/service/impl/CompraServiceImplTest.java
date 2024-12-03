package com.interonda.inventory.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.interonda.inventory.entity.Compra;
import com.interonda.inventory.entity.Proveedor;
import com.interonda.inventory.dto.CompraDTO;
import com.interonda.inventory.dto.DetalleCompraDTO;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.CompraMapper;
import com.interonda.inventory.repository.CompraRepository;
import com.interonda.inventory.repository.ProveedorRepository;
import com.interonda.inventory.service.impl.CompraServiceImpl;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CompraServiceImplTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @Mock
    private CompraRepository compraRepository;

    @Mock
    private Validator validator;

    @Mock
    private CompraMapper compraMapper;

    @InjectMocks
    private CompraServiceImpl compraServiceImpl;

    private CompraDTO compraDTO;
    private Compra compra;
    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        compraDTO = new CompraDTO();
        compraDTO.setId(1L);
        compraDTO.setProveedorId(1L);
        compraDTO.setFecha(LocalDate.now());
        compraDTO.setTotal(BigDecimal.valueOf(100.0));
        compraDTO.setMetodoPago("Credit Card");
        compraDTO.setEstado("Pending");
        compraDTO.setImpuestos(BigDecimal.valueOf(10.0));
        compraDTO.setDetallesCompra(List.of(new DetalleCompraDTO()));

        compra = new Compra();
        compra.setId(1L);

        proveedor = new Proveedor();
        proveedor.setId(1L);
    }

    @Test
    void convertToDto_ShouldReturnCompraDTO_WhenCompraIsValid() {
        // Arrange
        when(compraMapper.toDto(compra)).thenReturn(compraDTO);

        // Act
        CompraDTO result = compraServiceImpl.convertToDto(compra);

        // Assert
        assertNotNull(result);
        assertEquals(compra.getId(), result.getId());
        verify(compraMapper).toDto(compra);
    }

    @Test
    void convertToDto_ShouldReturnNull_WhenCompraIsNull() {
        // Act
        CompraDTO result = compraServiceImpl.convertToDto(null);

        // Assert
        assertNull(result);
    }

    @Test
    void convertToEntity_ShouldReturnCompra_WhenCompraDTOIsValid() {
        // Arrange
        when(compraMapper.toEntity(compraDTO)).thenReturn(compra);
        when(proveedorRepository.findById(compraDTO.getProveedorId())).thenReturn(Optional.of(proveedor));

        // Act
        Compra result = compraServiceImpl.convertToEntity(compraDTO);

        // Assert
        assertNotNull(result);
        assertEquals(compraDTO.getId(), result.getId());
        assertEquals(proveedor.getId(), result.getProveedor().getId());
        verify(compraMapper).toEntity(compraDTO);
        verify(proveedorRepository).findById(compraDTO.getProveedorId());
    }

    @Test
    void convertToEntity_ShouldReturnNull_WhenCompraDTOIsNull() {
        // Act
        Compra result = compraServiceImpl.convertToEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void createCompra_ShouldReturnCompraDTO_WhenCompraDTOIsValid() {
        // Arrange
        when(compraMapper.toEntity(compraDTO)).thenReturn(compra);
        when(proveedorRepository.findById(compraDTO.getProveedorId())).thenReturn(Optional.of(proveedor));
        when(compraRepository.save(compra)).thenReturn(compra);
        when(compraMapper.toDto(compra)).thenReturn(compraDTO);

        // Act
        CompraDTO result = compraServiceImpl.createCompra(compraDTO);

        // Assert
        assertNotNull(result);
        assertEquals(compraDTO.getId(), result.getId());
        verify(compraMapper).toEntity(compraDTO);
        verify(proveedorRepository).findById(compraDTO.getProveedorId());
        verify(compraRepository).save(compra);
        verify(compraMapper).toDto(compra);
    }

    @Test
    void createCompra_ShouldThrowDataAccessException_WhenErrorCreatingCompra() {
        // Arrange
        when(compraMapper.toEntity(compraDTO)).thenReturn(compra);
        when(proveedorRepository.findById(compraDTO.getProveedorId())).thenReturn(Optional.of(proveedor));
        when(compraRepository.save(compra)).thenThrow(new RuntimeException("Error creating compra"));

        // Act & Assert
        Exception exception = assertThrows(DataAccessException.class, () -> {
            compraServiceImpl.createCompra(compraDTO);
        });

        // Assert
        assertEquals("Error creando Compra", exception.getMessage());
        verify(compraMapper).toEntity(compraDTO);
        verify(proveedorRepository).findById(compraDTO.getProveedorId());
        verify(compraRepository).save(compra);
    }

    @Test
    void updateCompra_ShouldReturnCompraDTO_WhenCompraDTOIsValid() {
        // Arrange
        when(compraRepository.findById(compraDTO.getId())).thenReturn(Optional.of(compra));
        when(proveedorRepository.findById(compraDTO.getProveedorId())).thenReturn(Optional.of(proveedor));
        when(compraRepository.save(compra)).thenReturn(compra);
        when(compraMapper.toDto(compra)).thenReturn(compraDTO);

        // Act
        CompraDTO result = compraServiceImpl.updateCompra(compraDTO.getId(), compraDTO);

        // Assert
        assertNotNull(result);
        assertEquals(compraDTO.getId(), result.getId());
        verify(compraRepository).findById(compraDTO.getId());
        verify(proveedorRepository).findById(compraDTO.getProveedorId());
        verify(compraRepository).save(compra);
        verify(compraMapper).toDto(compra);
    }

    @Test
    void updateCompra_ShouldThrowResourceNotFoundException_WhenCompraIsNotFound() {
        // Arrange
        when(compraRepository.findById(compraDTO.getId())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            compraServiceImpl.updateCompra(compraDTO.getId(), compraDTO);
        });

        // Assert
        assertEquals("Compra no encontrada con el id: " + compraDTO.getId(), exception.getMessage());
        verify(compraRepository).findById(compraDTO.getId());
    }

    @Test
    void updateCompra_ShouldThrowDataAccessException_WhenErrorUpdatingCompra() {
        // Arrange
        when(compraRepository.findById(compraDTO.getId())).thenReturn(Optional.of(compra));
        when(proveedorRepository.findById(compraDTO.getProveedorId())).thenReturn(Optional.of(proveedor));
        doThrow(new RuntimeException("Error updating compra")).when(compraRepository).save(compra);

        // Act & Assert
        Exception exception = assertThrows(DataAccessException.class, () -> {
            compraServiceImpl.updateCompra(compraDTO.getId(), compraDTO);
        });

        // Assert
        assertEquals("Error actualizando Compra por id: " + compraDTO.getId(), exception.getMessage());
        verify(compraRepository).findById(compraDTO.getId());
        verify(compraMapper).updateEntityFromDto(compraDTO, compra);
        verify(proveedorRepository).findById(compraDTO.getProveedorId());
        verify(compraRepository).save(compra);
    }

    @Test
    void deleteCompra_ShouldDeleteCompra_WhenCompraExists() {
        // Arrange
        when(compraRepository.existsById(compraDTO.getId())).thenReturn(true);

        // Act
        compraServiceImpl.deleteCompra(compraDTO.getId());

        // Assert
        verify(compraRepository).existsById(compraDTO.getId());
        verify(compraRepository).deleteById(compraDTO.getId());
    }

    @Test
    void deleteCompra_ShouldThrowResourceNotFoundException_WhenCompraIsNotFound() {
        // Arrange
        when(compraRepository.existsById(compraDTO.getId())).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            compraServiceImpl.deleteCompra(compraDTO.getId());
        });

        // Assert
        assertEquals("Compra no encontrada con el id: " + compraDTO.getId(), exception.getMessage());
        verify(compraRepository).existsById(compraDTO.getId());
    }

    @Test
    void getAllCompras_ShouldReturnPageOfCompraDTO() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Compra> compraPage = new PageImpl<>(List.of(compra), pageable, 1);
        when(compraRepository.findAll(pageable)).thenReturn(compraPage);
        when(compraMapper.toDto(compra)).thenReturn(compraDTO);

        // Act
        Page<CompraDTO> result = compraServiceImpl.getAllCompras(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(compraRepository).findAll(pageable);
        verify(compraMapper).toDto(compra);
    }

    @Test
    void getAllCompras_ShouldThrowDataAccessException_WhenErrorGettingAllCompras() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(compraRepository.findAll(pageable)).thenThrow(new RuntimeException("Error getting all compras"));

        // Act & Assert
        Exception exception = assertThrows(DataAccessException.class, () -> {
            compraServiceImpl.getAllCompras(pageable);
        });

        // Assert
        assertEquals("Error obteniendo todas las Compras con paginación", exception.getMessage());
        verify(compraRepository).findAll(pageable);
    }

    @Test
    void getCompraById_ShouldReturnCompraDTO_WhenCompraExists() {
        // Arrange
        when(compraRepository.findById(compraDTO.getId())).thenReturn(Optional.of(compra));
        when(compraMapper.toDto(compra)).thenReturn(compraDTO);

        // Act
        CompraDTO result = compraServiceImpl.getCompraById(compraDTO.getId());

        // Assert
        assertNotNull(result);
        assertEquals(compraDTO.getId(), result.getId());
        verify(compraRepository).findById(compraDTO.getId());
        verify(compraMapper).toDto(compra);
    }

    @Test
    void getCompraById_ShouldThrowResourceNotFoundException_WhenCompraIsNotFound() {
        // Arrange
        when(compraRepository.findById(compraDTO.getId())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            compraServiceImpl.getCompraById(compraDTO.getId());
        });

        // Assert
        assertEquals("Compra no encontrada con el id: " + compraDTO.getId(), exception.getMessage());
        verify(compraRepository).findById(compraDTO.getId());
    }
}
