package com.interonda.inventory.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.interonda.inventory.entity.Producto;
import com.interonda.inventory.dto.ProductoDTO;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.ProductoMapper;
import com.interonda.inventory.repository.ProductoRepository;
import com.interonda.inventory.service.impl.ProductoServiceImpl;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProductoMapper productoMapper;

    @Mock
    private Validator validator;

    @Mock
    private Appender<ILoggingEvent> mockAppender;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @Captor
    private ArgumentCaptor<ILoggingEvent> captorLoggingEvent;

    private ProductoDTO productoDTO;
    private Producto producto;

    @BeforeEach
    void setUp() {
        productoDTO = new ProductoDTO();
        productoDTO.setId(1L);
        producto = new Producto();
        producto.setId(1L);

        Logger logger = (Logger) LoggerFactory.getLogger(ProductoServiceImpl.class);
        logger.addAppender(mockAppender);
    }

    @Test
    void createProducto_Success() {
        when(productoMapper.toEntity(productoDTO)).thenReturn(producto);
        when(productoRepository.save(producto)).thenReturn(producto);
        when(productoMapper.toDto(producto)).thenReturn(productoDTO);

        ProductoDTO result = productoService.createProducto(productoDTO);

        assertNotNull(result);
        assertEquals(productoDTO.getId(), result.getId());
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        assertEquals("Creando nuevo Producto", captorLoggingEvent.getAllValues().get(0).getFormattedMessage());
        assertEquals("Producto creado exitosamente con id: 1", captorLoggingEvent.getAllValues().get(1).getFormattedMessage());
    }

    @Test
    void createProducto_NullProductoDTO() {
        assertThrows(IllegalArgumentException.class, () -> productoService.createProducto(null));
    }

    @Test
    void createProducto_DataAccessException() {
        when(productoMapper.toEntity(productoDTO)).thenReturn(producto);
        when(productoRepository.save(producto)).thenThrow(new RuntimeException("Database error"));

        assertThrows(DataAccessException.class, () -> productoService.createProducto(productoDTO));
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        assertTrue(captorLoggingEvent.getAllValues().stream().anyMatch(event -> event.getFormattedMessage().contains("Error guardando Producto")));
    }

    @Test
    void updateProducto_Success() {
        when(productoRepository.findById(productoDTO.getId())).thenReturn(Optional.of(producto));
        when(productoMapper.toEntity(productoDTO)).thenReturn(producto);
        when(productoRepository.save(producto)).thenReturn(producto);
        when(productoMapper.toDto(producto)).thenReturn(productoDTO);

        ProductoDTO result = productoService.updateProducto(productoDTO);

        assertNotNull(result);
        assertEquals(productoDTO.getId(), result.getId());
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        assertEquals("Actualizando Producto con id: 1", captorLoggingEvent.getAllValues().get(0).getFormattedMessage());
        assertEquals("Producto actualizado exitosamente con id: 1", captorLoggingEvent.getAllValues().get(1).getFormattedMessage());
    }

    @Test
    void updateProducto_ProductoNotFound() {
        when(productoRepository.findById(productoDTO.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoService.updateProducto(productoDTO));
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        assertTrue(captorLoggingEvent.getAllValues().stream().anyMatch(event -> event.getFormattedMessage().contains("Producto no encontrado")));
    }

    @Test
    void updateProducto_DataAccessException() {
        when(productoRepository.findById(productoDTO.getId())).thenReturn(Optional.of(producto));
        when(productoMapper.toEntity(productoDTO)).thenReturn(producto);
        when(productoRepository.save(producto)).thenThrow(new RuntimeException("Database error"));

        assertThrows(DataAccessException.class, () -> productoService.updateProducto(productoDTO));
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        assertTrue(captorLoggingEvent.getAllValues().stream().anyMatch(event -> event.getFormattedMessage().contains("Error actualizando Producto")));
    }

    @Test
    void deleteProducto_Success() {
        when(productoRepository.existsById(1L)).thenReturn(true);

        productoService.deleteProducto(1L);

        verify(productoRepository, times(1)).deleteById(1L);
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        assertEquals("Eliminando Producto con id: 1", captorLoggingEvent.getAllValues().get(0).getFormattedMessage());
        assertEquals("Producto eliminado exitosamente con id: 1", captorLoggingEvent.getAllValues().get(1).getFormattedMessage());
    }

    @Test
    void deleteProducto_ProductoNotFound() {
        when(productoRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> productoService.deleteProducto(1L));
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        assertTrue(captorLoggingEvent.getAllValues().stream().anyMatch(event -> event.getFormattedMessage().contains("Producto no encontrado")));
    }

    @Test
    void deleteProducto_DataAccessException() {
        when(productoRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(productoRepository).deleteById(1L);

        assertThrows(DataAccessException.class, () -> productoService.deleteProducto(1L));
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        assertTrue(captorLoggingEvent.getAllValues().stream().anyMatch(event -> event.getFormattedMessage().contains("Error eliminando Producto")));
    }

    @Test
    void getProducto_Success() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoMapper.toDto(producto)).thenReturn(productoDTO);

        ProductoDTO result = productoService.getProducto(1L);

        assertNotNull(result);
        assertEquals(productoDTO.getId(), result.getId());
        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
        assertEquals("Obteniendo Producto con id: 1", captorLoggingEvent.getAllValues().get(0).getFormattedMessage());
    }

    @Test
    void getProducto_ProductoNotFound() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoService.getProducto(1L));
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        assertTrue(captorLoggingEvent.getAllValues().stream().anyMatch(event -> event.getFormattedMessage().contains("Producto no encontrado")));
    }

    @Test
    void getProducto_DataAccessException() {
        when(productoRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        assertThrows(DataAccessException.class, () -> productoService.getProducto(1L));
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        assertTrue(captorLoggingEvent.getAllValues().stream().anyMatch(event -> event.getFormattedMessage().contains("Error obteniendo Producto")));
    }

    @Test
    void getAllProductos_Success() {
        Page<Producto> productos = new PageImpl<>(List.of(producto));
        when(productoRepository.findAll(any(Pageable.class))).thenReturn(productos);
        when(productoMapper.toDto(producto)).thenReturn(productoDTO);

        Page<ProductoDTO> result = productoService.getAllProductos(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
        assertEquals("Obteniendo todos los Productos con paginación", captorLoggingEvent.getAllValues().get(0).getFormattedMessage());
    }

    @Test
    void getAllProductos_DataAccessException() {
        when(productoRepository.findAll(any(Pageable.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(DataAccessException.class, () -> productoService.getAllProductos(PageRequest.of(0, 10)));
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        assertTrue(captorLoggingEvent.getAllValues().stream()
                .anyMatch(event -> event.getFormattedMessage().contains("Error obteniendo todos los Productos con paginación")));
    }
}


