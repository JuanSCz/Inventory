package com.interonda.Inventory.serviceImplTest;

import com.interonda.Inventory.entity.*;
import com.interonda.Inventory.dto.HistorialStockDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.HistorialStockMapper;
import com.interonda.Inventory.repository.*;
import com.interonda.Inventory.serviceImplTest.impl.HistorialStockServiceImpl;
import jakarta.persistence.PersistenceException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistorialStockServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private DepositoRepository depositoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private HistorialStockRepository historialStockRepository;

    @Mock
    private HistorialStockMapper historialStockMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private HistorialStockServiceImpl historialStockServiceImpl;

    private HistorialStockDTO historialStockDTO;
    private HistorialStock historialStock;

    @BeforeEach
    void setUp() {
        historialStockDTO = new HistorialStockDTO();
        historialStockDTO.setProductoId(1L);
        historialStockDTO.setDepositoId(1L);
        historialStockDTO.setUsuarioId(1L);
        historialStockDTO.setStockId(1L);
        historialStockDTO.setCantidadAnterior(10);
        historialStockDTO.setCantidadNueva(20);
        historialStockDTO.setFechaActualizacion(LocalDateTime.now());
        historialStockDTO.setMotivo("Motivo de prueba");
        historialStockDTO.setTipoMovimiento("Movimiento de prueba");
        historialStockDTO.setObservacion("Observación de prueba");

        historialStock = new HistorialStock();
        historialStock.setId(1L);
        historialStock.setCantidadAnterior(10);
        historialStock.setCantidadNueva(20);
        historialStock.setFecha(LocalDateTime.now());
        historialStock.setMotivo("Motivo de prueba");
        historialStock.setTipoMovimiento("Movimiento de prueba");
        historialStock.setObservacion("Observación de prueba");
    }

    @Test
    void createHistorialStock_Success() {
        // Arrange
        when(historialStockMapper.toEntity(any(HistorialStockDTO.class))).thenReturn(historialStock);
        when(historialStockRepository.save(any(HistorialStock.class))).thenReturn(historialStock);
        when(historialStockMapper.toDto(any(HistorialStock.class))).thenReturn(historialStockDTO);

        // Act
        HistorialStockDTO result = historialStockServiceImpl.createHistorialStock(historialStockDTO);

        // Assert
        assertNotNull(result, "El resultado no debería ser nulo");
        assertEquals(historialStockDTO, result, "El resultado debería coincidir con el DTO esperado");
        verify(historialStockMapper).toEntity(historialStockDTO);
        verify(historialStockRepository).save(historialStock);
        verify(historialStockMapper).toDto(historialStock);
    }

    @Test
    void createHistorialStock_ValidationFails() {
        // Arrange
        doThrow(new RuntimeException("Validation failed")).when(validator).validate(any());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            historialStockServiceImpl.createHistorialStock(historialStockDTO);
        });

        // Assert
        assertEquals("Validation failed", exception.getMessage());
        verify(historialStockMapper, never()).toEntity(any());
        verify(historialStockRepository, never()).save(any());
        verify(historialStockMapper, never()).toDto(any());
    }

    @Test
    void createHistorialStock_PersistenceException() {
        // Arrange
        when(historialStockMapper.toEntity(any(HistorialStockDTO.class))).thenReturn(historialStock);
        when(historialStockRepository.save(any(HistorialStock.class))).thenThrow(new PersistenceException("Error guardando HistorialStock"));

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            historialStockServiceImpl.createHistorialStock(historialStockDTO);
        });

        // Assert
        assertEquals("Error guardando HistorialStock", exception.getMessage());
        verify(historialStockMapper).toEntity(historialStockDTO);
        verify(historialStockRepository).save(historialStock);
        verify(historialStockMapper, never()).toDto(any());
    }

    @Test
    void updateHistorialStock_Success() {
        // Arrange
        when(historialStockRepository.findById(anyLong())).thenReturn(Optional.of(historialStock));
        when(productoRepository.findById(anyLong())).thenReturn(Optional.of(new Producto()));
        when(depositoRepository.findById(anyLong())).thenReturn(Optional.of(new Deposito()));
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(new Usuario()));
        when(stockRepository.findById(anyLong())).thenReturn(Optional.of(new Stock()));
        when(historialStockRepository.save(any(HistorialStock.class))).thenReturn(historialStock);
        when(historialStockMapper.toDto(any(HistorialStock.class))).thenReturn(historialStockDTO);

        // Act
        HistorialStockDTO result = historialStockServiceImpl.updateHistorialStock(1L, historialStockDTO);

        // Assert
        assertNotNull(result, "El resultado no debería ser nulo");
        assertEquals(historialStockDTO, result, "El resultado debería coincidir con el DTO esperado");
        verify(historialStockRepository).findById(1L);
        verify(productoRepository).findById(historialStockDTO.getProductoId());
        verify(depositoRepository).findById(historialStockDTO.getDepositoId());
        verify(usuarioRepository).findById(historialStockDTO.getUsuarioId());
        verify(stockRepository).findById(historialStockDTO.getStockId());
        verify(historialStockRepository).save(historialStock);
        verify(historialStockMapper).toDto(historialStock);
    }

    @Test
    void updateHistorialStock_ValidationFails() {
        // Arrange
        doThrow(new RuntimeException("Validation failed")).when(validator).validate(any());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            historialStockServiceImpl.updateHistorialStock(1L, historialStockDTO);
        });

        // Assert
        assertEquals("Validation failed", exception.getMessage());
        verify(historialStockRepository, never()).findById(anyLong());
        verify(productoRepository, never()).findById(anyLong());
        verify(depositoRepository, never()).findById(anyLong());
        verify(usuarioRepository, never()).findById(anyLong());
        verify(stockRepository, never()).findById(anyLong());
        verify(historialStockRepository, never()).save(any());
        verify(historialStockMapper, never()).toDto(any());
    }

    @Test
    void updateHistorialStock_NotFound() {
        // Arrange
        when(historialStockRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            historialStockServiceImpl.updateHistorialStock(1L, historialStockDTO);
        });

        // Assert
        assertEquals("HistorialStock no encontrado con el id: 1", exception.getMessage());
        verify(historialStockRepository).findById(1L);
        verify(productoRepository, never()).findById(anyLong());
        verify(depositoRepository, never()).findById(anyLong());
        verify(usuarioRepository, never()).findById(anyLong());
        verify(stockRepository, never()).findById(anyLong());
        verify(historialStockRepository, never()).save(any());
        verify(historialStockMapper, never()).toDto(any());
    }

    @Test
    void updateHistorialStock_PersistenceException() {
        // Arrange
        when(historialStockRepository.findById(anyLong())).thenReturn(Optional.of(historialStock));
        when(productoRepository.findById(anyLong())).thenReturn(Optional.of(new Producto()));
        when(depositoRepository.findById(anyLong())).thenReturn(Optional.of(new Deposito()));
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(new Usuario()));
        when(stockRepository.findById(anyLong())).thenReturn(Optional.of(new Stock()));
        when(historialStockRepository.save(any(HistorialStock.class))).thenThrow(new PersistenceException("Error actualizando HistorialStock"));

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            historialStockServiceImpl.updateHistorialStock(1L, historialStockDTO);
        });

        // Assert
        assertEquals("Error actualizando HistorialStock", exception.getMessage());
        verify(historialStockRepository).findById(1L);
        verify(productoRepository).findById(historialStockDTO.getProductoId());
        verify(depositoRepository).findById(historialStockDTO.getDepositoId());
        verify(usuarioRepository).findById(historialStockDTO.getUsuarioId());
        verify(stockRepository).findById(historialStockDTO.getStockId());
        verify(historialStockRepository).save(historialStock);
        verify(historialStockMapper, never()).toDto(any());
    }
}