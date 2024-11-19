package com.interonda.Inventory.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.interonda.Inventory.entity.Compra;
import com.interonda.Inventory.entity.Proveedor;
import com.interonda.Inventory.entityDTO.CompraDTO;
import com.interonda.Inventory.entityDTO.DetalleCompraDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.CompraMapper;
import com.interonda.Inventory.repository.CompraRepository;
import com.interonda.Inventory.repository.ProveedorRepository;
import com.interonda.Inventory.service.impl.CompraServiceImpl;
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
public class CompraServiceImplTest {

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
        when(compraMapper.toDto(compra)).thenReturn(compraDTO);

        CompraDTO result = compraServiceImpl.convertToDto(compra);

        assertNotNull(result);
        assertEquals(compra.getId(), result.getId());
        verify(compraMapper).toDto(compra);
    }

    @Test
    void convertToDto_ShouldReturnNull_WhenCompraIsNull() {
        CompraDTO result = compraServiceImpl.convertToDto(null);

        assertNull(result);
    }

    @Test
    void convertToEntity_ShouldReturnCompra_WhenCompraDTOIsValid() {
        when(compraMapper.toEntity(compraDTO)).thenReturn(compra);
        when(proveedorRepository.findById(compraDTO.getProveedorId())).thenReturn(Optional.of(proveedor));

        Compra result = compraServiceImpl.convertToEntity(compraDTO);

        assertNotNull(result);
        assertEquals(compraDTO.getId(), result.getId());
        assertEquals(proveedor.getId(), result.getProveedor().getId());
        verify(compraMapper).toEntity(compraDTO);
        verify(proveedorRepository).findById(compraDTO.getProveedorId());
    }

    @Test
    void convertToEntity_ShouldReturnNull_WhenCompraDTOIsNull() {
        Compra result = compraServiceImpl.convertToEntity(null);

        assertNull(result);
    }

    @Test
    void createCompra_ShouldReturnCompraDTO_WhenCompraDTOIsValid() {
        when(compraMapper.toEntity(compraDTO)).thenReturn(compra);
        when(proveedorRepository.findById(compraDTO.getProveedorId())).thenReturn(Optional.of(proveedor));
        when(compraRepository.save(compra)).thenReturn(compra);
        when(compraMapper.toDto(compra)).thenReturn(compraDTO);

        CompraDTO result = compraServiceImpl.createCompra(compraDTO);

        assertNotNull(result);
        assertEquals(compraDTO.getId(), result.getId());
        verify(compraMapper).toEntity(compraDTO);
        verify(proveedorRepository).findById(compraDTO.getProveedorId());
        verify(compraRepository).save(compra);
        verify(compraMapper).toDto(compra);
    }

    @Test
    void createCompra_ShouldThrowDataAccessException_WhenErrorCreatingCompra() {
        when(compraMapper.toEntity(compraDTO)).thenReturn(compra);
        when(proveedorRepository.findById(compraDTO.getProveedorId())).thenReturn(Optional.of(proveedor));
        when(compraRepository.save(compra)).thenThrow(new RuntimeException("Error creating compra"));

        Exception exception = assertThrows(DataAccessException.class, () -> {
            compraServiceImpl.createCompra(compraDTO);
        });

        assertEquals("Error creando Compra", exception.getMessage());
        verify(compraMapper).toEntity(compraDTO);
        verify(proveedorRepository).findById(compraDTO.getProveedorId());
        verify(compraRepository).save(compra);
    }

    @Test
    void updateCompra_ShouldReturnCompraDTO_WhenCompraDTOIsValid() {
        when(compraRepository.findById(compraDTO.getId())).thenReturn(Optional.of(compra));
        when(proveedorRepository.findById(compraDTO.getProveedorId())).thenReturn(Optional.of(proveedor));
        when(compraRepository.save(compra)).thenReturn(compra);
        when(compraMapper.toDto(compra)).thenReturn(compraDTO);

        CompraDTO result = compraServiceImpl.updateCompra(compraDTO.getId(), compraDTO);

        assertNotNull(result);
        assertEquals(compraDTO.getId(), result.getId());
        verify(compraRepository).findById(compraDTO.getId());
        verify(proveedorRepository).findById(compraDTO.getProveedorId());
        verify(compraRepository).save(compra);
        verify(compraMapper).toDto(compra);
    }

    @Test
    void updateCompra_ShouldThrowResourceNotFoundException_WhenCompraIsNotFound() {
        when(compraRepository.findById(compraDTO.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            compraServiceImpl.updateCompra(compraDTO.getId(), compraDTO);
        });

        assertEquals("Compra no encontrada con el id: " + compraDTO.getId(), exception.getMessage());
        verify(compraRepository).findById(compraDTO.getId());
    }

    @Test
    void updateCompra_ShouldThrowDataAccessException_WhenErrorUpdatingCompra() {
        when(compraRepository.findById(compraDTO.getId())).thenReturn(Optional.of(compra));
        when(proveedorRepository.findById(compraDTO.getProveedorId())).thenReturn(Optional.of(proveedor));
        doThrow(new RuntimeException("Error updating compra")).when(compraRepository).save(compra);

        Exception exception = assertThrows(DataAccessException.class, () -> {
            compraServiceImpl.updateCompra(compraDTO.getId(), compraDTO);
        });

        assertEquals("Error actualizando Compra por id: " + compraDTO.getId(), exception.getMessage());
        verify(compraRepository).findById(compraDTO.getId());
        verify(compraMapper).updateEntityFromDto(compraDTO, compra);
        verify(proveedorRepository).findById(compraDTO.getProveedorId());
        verify(compraRepository).save(compra);
    }

    @Test
    void deleteCompra_ShouldDeleteCompra_WhenCompraExists() {
        when(compraRepository.existsById(compraDTO.getId())).thenReturn(true);

        compraServiceImpl.deleteCompra(compraDTO.getId());

        verify(compraRepository).existsById(compraDTO.getId());
        verify(compraRepository).deleteById(compraDTO.getId());
    }

    @Test
    void deleteCompra_ShouldThrowResourceNotFoundException_WhenCompraIsNotFound() {
        when(compraRepository.existsById(compraDTO.getId())).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            compraServiceImpl.deleteCompra(compraDTO.getId());
        });

        assertEquals("Compra no encontrada con el id: " + compraDTO.getId(), exception.getMessage());
        verify(compraRepository).existsById(compraDTO.getId());
    }

    @Test
    void getAllCompras_ShouldReturnPageOfCompraDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Compra> compraPage = new PageImpl<>(List.of(compra), pageable, 1);
        when(compraRepository.findAll(pageable)).thenReturn(compraPage);
        when(compraMapper.toDto(compra)).thenReturn(compraDTO);

        Page<CompraDTO> result = compraServiceImpl.getAllCompras(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(compraRepository).findAll(pageable);
        verify(compraMapper).toDto(compra);
    }

    @Test
    void getAllCompras_ShouldThrowDataAccessException_WhenErrorGettingAllCompras() {
        Pageable pageable = PageRequest.of(0, 10);
        when(compraRepository.findAll(pageable)).thenThrow(new RuntimeException("Error getting all compras"));

        Exception exception = assertThrows(DataAccessException.class, () -> {
            compraServiceImpl.getAllCompras(pageable);
        });

        assertEquals("Error obteniendo todas las Compras con paginación", exception.getMessage());
        verify(compraRepository).findAll(pageable);
    }

    @Test
    void getCompraById_ShouldReturnCompraDTO_WhenCompraExists() {
        when(compraRepository.findById(compraDTO.getId())).thenReturn(Optional.of(compra));
        when(compraMapper.toDto(compra)).thenReturn(compraDTO);

        CompraDTO result = compraServiceImpl.getCompraById(compraDTO.getId());

        assertNotNull(result);
        assertEquals(compraDTO.getId(), result.getId());
        verify(compraRepository).findById(compraDTO.getId());
        verify(compraMapper).toDto(compra);
    }

    @Test
    void getCompraById_ShouldThrowResourceNotFoundException_WhenCompraIsNotFound() {
        when(compraRepository.findById(compraDTO.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            compraServiceImpl.getCompraById(compraDTO.getId());
        });

        assertEquals("Compra no encontrada con el id: " + compraDTO.getId(), exception.getMessage());
        verify(compraRepository).findById(compraDTO.getId());
    }
}
