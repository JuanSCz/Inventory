package com.interonda.Inventory.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.interonda.Inventory.entity.Compra;
import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.entity.DetalleCompra;
import com.interonda.Inventory.entityDTO.DetalleCompraDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.DetalleCompraMapper;
import com.interonda.Inventory.repository.CompraRepository;
import com.interonda.Inventory.repository.DetalleCompraRepository;
import com.interonda.Inventory.repository.ProductoRepository;
import com.interonda.Inventory.service.impl.DetalleCompraServiceImpl;
import jakarta.persistence.PersistenceException;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DetalleCompraServiceImplTest {

    @Mock
    private DetalleCompraRepository detalleCompraRepository;

    @Mock
    private CompraRepository compraRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private DetalleCompraMapper detalleCompraMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private DetalleCompraServiceImpl detalleCompraServiceImpl;

    private DetalleCompraDTO detalleCompraDTO;

    @BeforeEach
    void setUp() {
        detalleCompraDTO = new DetalleCompraDTO();
        detalleCompraDTO.setCantidad(10);
        detalleCompraDTO.setPrecioUnitario(new BigDecimal("100.00"));
        detalleCompraDTO.setCompraId(1L);
        detalleCompraDTO.setProductoId(1L);
    }

    @Test
    void createDetalleCompra_Success() {
        // Arrange
        Compra compra = new Compra();
        compra.setId(1L);

        Producto producto = new Producto();
        producto.setId(1L);

        DetalleCompra detalleCompra = new DetalleCompra();
        detalleCompra.setCantidad(10);
        detalleCompra.setPrecioUnitario(new BigDecimal("100.00"));
        detalleCompra.setCompra(compra);
        detalleCompra.setProducto(producto);

        DetalleCompra savedDetalleCompra = new DetalleCompra();
        savedDetalleCompra.setId(1L);
        savedDetalleCompra.setCantidad(10);
        savedDetalleCompra.setPrecioUnitario(new BigDecimal("100.00"));
        savedDetalleCompra.setCompra(compra);
        savedDetalleCompra.setProducto(producto);

        DetalleCompraDTO expectedDetalleCompraDTO = new DetalleCompraDTO();
        expectedDetalleCompraDTO.setId(1L);
        expectedDetalleCompraDTO.setCantidad(10);
        expectedDetalleCompraDTO.setPrecioUnitario(new BigDecimal("100.00"));
        expectedDetalleCompraDTO.setCompraId(1L);
        expectedDetalleCompraDTO.setProductoId(1L);

        when(compraRepository.findById(1L)).thenReturn(Optional.of(compra));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(detalleCompraMapper.toEntity(detalleCompraDTO)).thenReturn(detalleCompra);
        when(detalleCompraRepository.save(detalleCompra)).thenReturn(savedDetalleCompra);
        when(detalleCompraMapper.toDto(savedDetalleCompra)).thenReturn(expectedDetalleCompraDTO);

        // Act
        DetalleCompraDTO result = detalleCompraServiceImpl.createDetalleCompra(detalleCompraDTO);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDetalleCompraDTO.getId(), result.getId());
        assertEquals(expectedDetalleCompraDTO.getCantidad(), result.getCantidad());
        assertEquals(expectedDetalleCompraDTO.getPrecioUnitario(), result.getPrecioUnitario());
        assertEquals(expectedDetalleCompraDTO.getCompraId(), result.getCompraId());
        assertEquals(expectedDetalleCompraDTO.getProductoId(), result.getProductoId());

        // Verify
        verify(compraRepository).findById(1L);
        verify(productoRepository).findById(1L);
        verify(detalleCompraRepository).save(detalleCompra);
        verify(detalleCompraMapper).toEntity(detalleCompraDTO);
        verify(detalleCompraMapper).toDto(savedDetalleCompra);
    }

    @Test
    void createDetalleCompra_Fail() {
        // Arrange
        Compra compra = new Compra();
        compra.setId(1L);

        Producto producto = new Producto();
        producto.setId(1L);

        DetalleCompra detalleCompra = new DetalleCompra();
        detalleCompra.setCantidad(10);
        detalleCompra.setPrecioUnitario(new BigDecimal("100.00"));
        detalleCompra.setCompra(compra);
        detalleCompra.setProducto(producto);

        when(compraRepository.findById(1L)).thenReturn(Optional.of(compra));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(detalleCompraMapper.toEntity(detalleCompraDTO)).thenReturn(detalleCompra);
        when(detalleCompraRepository.save(detalleCompra)).thenThrow(new PersistenceException("Error saving entity"));

        // Act & Assert
        assertThrows(DataAccessException.class, () -> detalleCompraServiceImpl.createDetalleCompra(detalleCompraDTO));

        // Verify
        verify(compraRepository).findById(1L);
        verify(productoRepository).findById(1L);
        verify(detalleCompraRepository).save(detalleCompra);
        verify(detalleCompraMapper).toEntity(detalleCompraDTO);
        verify(detalleCompraMapper, never()).toDto(any());
    }

    @Test
    void createDetalleCompra_NullCompra() {
        // Arrange
        when(compraRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> detalleCompraServiceImpl.createDetalleCompra(detalleCompraDTO));

        // Verify
        verify(compraRepository).findById(1L);
        verify(productoRepository, never()).findById(anyLong());
        verify(detalleCompraRepository, never()).save(any());
        verify(detalleCompraMapper, never()).toEntity(any());
        verify(detalleCompraMapper, never()).toDto(any());
    }

    @Test
    void updateDetalleCompra_Success() {
        // Arrange
        Compra compra = new Compra();
        compra.setId(1L);

        Producto producto = new Producto();
        producto.setId(1L);

        DetalleCompra detalleCompra = new DetalleCompra();
        detalleCompra.setId(1L);
        detalleCompra.setCantidad(10);
        detalleCompra.setPrecioUnitario(new BigDecimal("100.00"));
        detalleCompra.setCompra(compra);
        detalleCompra.setProducto(producto);

        DetalleCompra updatedDetalleCompra = new DetalleCompra();
        updatedDetalleCompra.setId(1L);
        updatedDetalleCompra.setCantidad(20);
        updatedDetalleCompra.setPrecioUnitario(new BigDecimal("200.00"));
        updatedDetalleCompra.setCompra(compra);
        updatedDetalleCompra.setProducto(producto);

        DetalleCompraDTO expectedDetalleCompraDTO = new DetalleCompraDTO();
        expectedDetalleCompraDTO.setId(1L);
        expectedDetalleCompraDTO.setCantidad(20);
        expectedDetalleCompraDTO.setPrecioUnitario(new BigDecimal("200.00"));
        expectedDetalleCompraDTO.setCompraId(1L);
        expectedDetalleCompraDTO.setProductoId(1L);

        when(detalleCompraRepository.findById(1L)).thenReturn(Optional.of(detalleCompra));
        when(compraRepository.findById(1L)).thenReturn(Optional.of(compra));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(detalleCompraRepository.save(detalleCompra)).thenReturn(updatedDetalleCompra);
        when(detalleCompraMapper.toDto(updatedDetalleCompra)).thenReturn(expectedDetalleCompraDTO);

        // Act
        DetalleCompraDTO result = detalleCompraServiceImpl.updateDetalleCompra(1L, detalleCompraDTO);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDetalleCompraDTO.getId(), result.getId());
        assertEquals(expectedDetalleCompraDTO.getCantidad(), result.getCantidad());
        assertEquals(expectedDetalleCompraDTO.getPrecioUnitario(), result.getPrecioUnitario());
        assertEquals(expectedDetalleCompraDTO.getCompraId(), result.getCompraId());
        assertEquals(expectedDetalleCompraDTO.getProductoId(), result.getProductoId());

        // Verify
        verify(detalleCompraRepository).findById(1L);
        verify(compraRepository).findById(1L);
        verify(productoRepository).findById(1L);
        verify(detalleCompraRepository).save(detalleCompra);
        verify(detalleCompraMapper).toDto(updatedDetalleCompra);
    }

    @Test
    void updateDetalleCompra_Fail() {
        // Arrange
        Compra compra = new Compra();
        compra.setId(1L);

        Producto producto = new Producto();
        producto.setId(1L);

        DetalleCompra detalleCompra = new DetalleCompra();
        detalleCompra.setId(1L);
        detalleCompra.setCantidad(10);
        detalleCompra.setPrecioUnitario(new BigDecimal("100.00"));
        detalleCompra.setCompra(compra);
        detalleCompra.setProducto(producto);

        when(detalleCompraRepository.findById(1L)).thenReturn(Optional.of(detalleCompra));
        when(compraRepository.findById(1L)).thenReturn(Optional.of(compra));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(detalleCompraRepository.save(detalleCompra)).thenThrow(new PersistenceException("Error saving entity"));

        // Act & Assert
        assertThrows(DataAccessException.class, () -> detalleCompraServiceImpl.updateDetalleCompra(1L, detalleCompraDTO));

        // Verify
        verify(detalleCompraRepository).findById(1L);
        verify(compraRepository).findById(1L);
        verify(productoRepository).findById(1L);
        verify(detalleCompraRepository).save(detalleCompra);
        verify(detalleCompraMapper, never()).toDto(any());
    }

    @Test
    void updateDetalleCompra_NullDetalleCompra() {
        // Arrange
        when(detalleCompraRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> detalleCompraServiceImpl.updateDetalleCompra(1L, detalleCompraDTO));

        // Verify
        verify(detalleCompraRepository).findById(1L);
        verify(compraRepository, never()).findById(anyLong());
        verify(productoRepository, never()).findById(anyLong());
        verify(detalleCompraRepository, never()).save(any());
        verify(detalleCompraMapper, never()).toDto(any());
    }

    @Test
    void deleteDetalleCompra_Success() {
        // Arrange
        when(detalleCompraRepository.existsById(1L)).thenReturn(true);

        // Act
        detalleCompraServiceImpl.deleteDetalleCompra(1L);

        // Verify
        verify(detalleCompraRepository).existsById(1L);
        verify(detalleCompraRepository).deleteById(1L);
    }

    @Test
    void deleteDetalleCompra_NotFound() {
        // Arrange
        when(detalleCompraRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> detalleCompraServiceImpl.deleteDetalleCompra(1L));

        // Verify
        verify(detalleCompraRepository).existsById(1L);
        verify(detalleCompraRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteDetalleCompra_DataAccessException() {
        // Arrange
        when(detalleCompraRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Unexpected error")).when(detalleCompraRepository).deleteById(1L);

        // Act & Assert
        assertThrows(DataAccessException.class, () -> detalleCompraServiceImpl.deleteDetalleCompra(1L));

        // Verify
        verify(detalleCompraRepository).existsById(1L);
        verify(detalleCompraRepository).deleteById(1L);
    }

    @Test
    void getAllDetalleCompra_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<DetalleCompra> detalleCompraPage = new PageImpl<>(List.of(new DetalleCompra()));
        when(detalleCompraRepository.findAll(pageable)).thenReturn(detalleCompraPage);
        when(detalleCompraMapper.toDto(any(DetalleCompra.class))).thenReturn(new DetalleCompraDTO());

        // Act
        Page<DetalleCompraDTO> result = detalleCompraServiceImpl.getAllDetalleCompra(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(detalleCompraRepository).findAll(pageable);
        verify(detalleCompraMapper).toDto(any(DetalleCompra.class));
    }

    @Test
    void getAllDetalleCompra_EmptyPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<DetalleCompra> detalleCompraPage = new PageImpl<>(Collections.emptyList());
        when(detalleCompraRepository.findAll(pageable)).thenReturn(detalleCompraPage);

        // Act
        Page<DetalleCompraDTO> result = detalleCompraServiceImpl.getAllDetalleCompra(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(detalleCompraRepository).findAll(pageable);
        verify(detalleCompraMapper, never()).toDto(any(DetalleCompra.class));
    }

    @Test
    void getAllDetalleCompra_DataAccessException() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(detalleCompraRepository.findAll(pageable)).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        assertThrows(DataAccessException.class, () -> detalleCompraServiceImpl.getAllDetalleCompra(pageable));

        // Verify
        verify(detalleCompraRepository).findAll(pageable);
        verify(detalleCompraMapper, never()).toDto(any(DetalleCompra.class));
    }

    @Test
    void getDetalleCompraById_Success() {
        // Arrange
        DetalleCompra detalleCompra = new DetalleCompra();
        detalleCompra.setId(1L);
        when(detalleCompraRepository.findById(1L)).thenReturn(Optional.of(detalleCompra));
        when(detalleCompraMapper.toDto(detalleCompra)).thenReturn(new DetalleCompraDTO());

        // Act
        DetalleCompraDTO result = detalleCompraServiceImpl.getDetalleCompraById(1L);

        // Assert
        assertNotNull(result);
        verify(detalleCompraRepository).findById(1L);
        verify(detalleCompraMapper).toDto(detalleCompra);
    }

    @Test
    void getDetalleCompraById_NotFound() {
        // Arrange
        when(detalleCompraRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> detalleCompraServiceImpl.getDetalleCompraById(1L));

        // Verify
        verify(detalleCompraRepository).findById(1L);
        verify(detalleCompraMapper, never()).toDto(any());
    }

    @Test
    void getDetalleCompraById_DataAccessException() {
        // Arrange
        when(detalleCompraRepository.findById(1L)).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        assertThrows(DataAccessException.class, () -> detalleCompraServiceImpl.getDetalleCompraById(1L));

        // Verify
        verify(detalleCompraRepository).findById(1L);
        verify(detalleCompraMapper, never()).toDto(any());
    }
}