package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.entityDTO.ProductoDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.ProductoMapper;
import com.interonda.Inventory.repository.ProductoRepository;
import com.interonda.Inventory.service.impl.ProductoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    private static final Long PRODUCTO_ID = 1L;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoServiceImpl productoServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProducto_Success() {
        ProductoDTO productoDTO = new ProductoDTO();
        Producto producto = new Producto();
        when(productoMapper.toEntity(productoDTO)).thenReturn(producto);
        when(productoRepository.save(producto)).thenReturn(producto);
        when(productoMapper.toDto(producto)).thenReturn(productoDTO);

        ProductoDTO result = productoServiceImpl.createProducto(productoDTO);

        assertAll(() -> assertNotNull(result), () -> verify(productoMapper, times(1)).toEntity(productoDTO), () -> verify(productoRepository, times(1)).save(producto), () -> verify(productoMapper, times(1)).toDto(producto), () -> verifyNoMoreInteractions(productoRepository, productoMapper));
    }

    @Test
    void createProducto_DataAccessException() {
        ProductoDTO productoDTO = new ProductoDTO();
        Producto producto = new Producto();
        when(productoMapper.toEntity(productoDTO)).thenReturn(producto);
        when(productoRepository.save(producto)).thenThrow(new RuntimeException());

        assertThrows(DataAccessException.class, () -> productoServiceImpl.createProducto(productoDTO));

        verify(productoMapper, times(1)).toEntity(productoDTO);
        verify(productoRepository, times(1)).save(producto);
        verifyNoMoreInteractions(productoRepository, productoMapper);
    }

    @Test
    void createProducto_NullProductoDTO() {
        assertThrows(IllegalArgumentException.class, () -> productoServiceImpl.createProducto(null));
    }

    @Test
    void updateProducto_Success() {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(PRODUCTO_ID);
        Producto producto = new Producto();
        when(productoRepository.findById(PRODUCTO_ID)).thenReturn(Optional.of(producto));
        when(productoMapper.toEntity(productoDTO)).thenReturn(producto);
        when(productoRepository.save(producto)).thenReturn(producto);
        when(productoMapper.toDto(producto)).thenReturn(productoDTO);

        ProductoDTO result = productoServiceImpl.updateProducto(productoDTO);

        assertAll(() -> assertNotNull(result), () -> verify(productoRepository, times(1)).findById(PRODUCTO_ID), () -> verify(productoMapper, times(1)).toEntity(productoDTO), () -> verify(productoRepository, times(1)).save(producto), () -> verify(productoMapper, times(1)).toDto(producto), () -> verifyNoMoreInteractions(productoRepository, productoMapper));
    }

    @Test
    void updateProducto_NotFound() {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(PRODUCTO_ID);
        when(productoRepository.findById(PRODUCTO_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoServiceImpl.updateProducto(productoDTO));

        verify(productoRepository, times(1)).findById(PRODUCTO_ID);
        verifyNoMoreInteractions(productoRepository, productoMapper);
    }

    @Test
    void updateProducto_DataAccessException() {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(PRODUCTO_ID);
        Producto producto = new Producto();
        when(productoRepository.findById(PRODUCTO_ID)).thenReturn(Optional.of(producto));
        when(productoMapper.toEntity(productoDTO)).thenReturn(producto);
        when(productoRepository.save(producto)).thenThrow(new RuntimeException());

        assertThrows(DataAccessException.class, () -> productoServiceImpl.updateProducto(productoDTO));

        verify(productoRepository, times(1)).findById(PRODUCTO_ID);
        verify(productoMapper, times(1)).toEntity(productoDTO);
        verify(productoRepository, times(1)).save(producto);
        verifyNoMoreInteractions(productoRepository, productoMapper);
    }

    @Test
    void deleteProducto_Success() {
        when(productoRepository.existsById(PRODUCTO_ID)).thenReturn(true);

        productoServiceImpl.deleteProducto(PRODUCTO_ID);

        verify(productoRepository, times(1)).existsById(PRODUCTO_ID);
        verify(productoRepository, times(1)).deleteById(PRODUCTO_ID);
        verifyNoMoreInteractions(productoRepository);
    }

    @Test
    void deleteProducto_NotFound() {
        when(productoRepository.existsById(PRODUCTO_ID)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> productoServiceImpl.deleteProducto(PRODUCTO_ID));

        verify(productoRepository, times(1)).existsById(PRODUCTO_ID);
        verifyNoMoreInteractions(productoRepository);
    }

    @Test
    void deleteProducto_DataAccessException() {
        when(productoRepository.existsById(PRODUCTO_ID)).thenReturn(true);
        doThrow(new RuntimeException()).when(productoRepository).deleteById(PRODUCTO_ID);

        assertThrows(DataAccessException.class, () -> productoServiceImpl.deleteProducto(PRODUCTO_ID));

        verify(productoRepository, times(1)).existsById(PRODUCTO_ID);
        verify(productoRepository, times(1)).deleteById(PRODUCTO_ID);
        verifyNoMoreInteractions(productoRepository);
    }

    @Test
    void getProducto_Success() {
        Producto producto = new Producto();
        ProductoDTO productoDTO = new ProductoDTO();
        when(productoRepository.findById(PRODUCTO_ID)).thenReturn(Optional.of(producto));
        when(productoMapper.toDto(producto)).thenReturn(productoDTO);

        ProductoDTO result = productoServiceImpl.getProducto(PRODUCTO_ID);

        assertAll(() -> assertNotNull(result), () -> verify(productoRepository, times(1)).findById(PRODUCTO_ID), () -> verify(productoMapper, times(1)).toDto(producto), () -> verifyNoMoreInteractions(productoRepository, productoMapper));
    }

    @Test
    void getProducto_NotFound() {
        when(productoRepository.findById(PRODUCTO_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoServiceImpl.getProducto(PRODUCTO_ID));

        verify(productoRepository, times(1)).findById(PRODUCTO_ID);
        verifyNoMoreInteractions(productoRepository, productoMapper);
    }

    @Test
    void getProducto_DataAccessException() {
        when(productoRepository.findById(PRODUCTO_ID)).thenThrow(new RuntimeException());

        assertThrows(DataAccessException.class, () -> productoServiceImpl.getProducto(PRODUCTO_ID));

        verify(productoRepository, times(1)).findById(PRODUCTO_ID);
        verifyNoMoreInteractions(productoRepository, productoMapper);
    }

    @Test
    void getAllProductos_Success() {
        Pageable pageable = mock(Pageable.class);
        Producto producto = new Producto();
        ProductoDTO productoDTO = new ProductoDTO();
        Page<Producto> productoPage = new PageImpl<>(Collections.singletonList(producto), pageable, 1);
        Page<ProductoDTO> productoDTOPage = new PageImpl<>(Collections.singletonList(productoDTO), pageable, 1);

        when(productoRepository.findAll(pageable)).thenReturn(productoPage);
        when(productoMapper.toDto(producto)).thenReturn(productoDTO);

        Page<ProductoDTO> result = productoServiceImpl.getAllProductos(pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(productoRepository, times(1)).findAll(pageable);
        verify(productoMapper, times(1)).toDto(producto);
        verifyNoMoreInteractions(productoRepository, productoMapper);
    }

    @Test
    void getAllProductos_EmptyPage() {
        Pageable pageable = mock(Pageable.class);
        Page<Producto> productoPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(productoRepository.findAll(pageable)).thenReturn(productoPage);

        Page<ProductoDTO> result = productoServiceImpl.getAllProductos(pageable);

        assertAll(() -> assertNotNull(result), () -> assertTrue(result.isEmpty()), () -> verify(productoRepository, times(1)).findAll(pageable), () -> verifyNoMoreInteractions(productoRepository, productoMapper));
    }

    @Test
    void getAllProductos_DataAccessException() {
        Pageable pageable = mock(Pageable.class);
        when(productoRepository.findAll(pageable)).thenThrow(new RuntimeException());

        assertThrows(DataAccessException.class, () -> productoServiceImpl.getAllProductos(pageable));

        verify(productoRepository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(productoRepository, productoMapper);
    }
}

