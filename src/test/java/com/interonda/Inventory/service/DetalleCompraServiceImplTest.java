package com.interonda.Inventory.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.interonda.Inventory.entity.Compra;
import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.repository.CompraRepository;
import com.interonda.Inventory.repository.ProductoRepository;
import com.interonda.Inventory.service.impl.DetalleCompraServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.interonda.Inventory.entity.DetalleCompra;
import com.interonda.Inventory.entityDTO.DetalleCompraDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.mapper.DetalleCompraMapper;
import com.interonda.Inventory.repository.DetalleCompraRepository;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    @InjectMocks
    private DetalleCompraServiceImpl detalleCompraServiceImpl;

    private DetalleCompraDTO detalleCompraDTO;
    private DetalleCompra detalleCompra;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        detalleCompraDTO = new DetalleCompraDTO();
        detalleCompra = new DetalleCompra();
    }

    @Test
    void createDetalleCompra_Success() {
        when(detalleCompraMapper.toEntity(detalleCompraDTO)).thenReturn(detalleCompra);
        when(detalleCompraRepository.save(detalleCompra)).thenReturn(detalleCompra);
        when(detalleCompraMapper.toDto(detalleCompra)).thenReturn(detalleCompraDTO);

        DetalleCompraDTO result = detalleCompraServiceImpl.createDetalleCompra(detalleCompraDTO);

        assertNotNull(result);
        verify(detalleCompraRepository, times(1)).save(detalleCompra);
    }

    @Test
    void createDetalleCompra_DataAccessException() {
        when(detalleCompraMapper.toEntity(detalleCompraDTO)).thenReturn(detalleCompra);
        when(detalleCompraRepository.save(detalleCompra)).thenThrow(new RuntimeException());

        assertThrows(DataAccessException.class, () -> {
            detalleCompraServiceImpl.createDetalleCompra(detalleCompraDTO);
        });

        verify(detalleCompraRepository, times(1)).save(detalleCompra);
    }

    @Test
    void updateDetalleCompra_Success() {
        when(detalleCompraRepository.findById(1L)).thenReturn(Optional.of(detalleCompra));
        when(compraRepository.findById(detalleCompraDTO.getCompraId())).thenReturn(Optional.of(new Compra()));
        when(productoRepository.findById(detalleCompraDTO.getProductoId())).thenReturn(Optional.of(new Producto()));
        when(detalleCompraRepository.save(detalleCompra)).thenReturn(detalleCompra);
        when(detalleCompraMapper.toDto(detalleCompra)).thenReturn(detalleCompraDTO);

        DetalleCompraDTO result = detalleCompraServiceImpl.updateDetalleCompra(1L, detalleCompraDTO);

        assertNotNull(result);
        verify(detalleCompraRepository, times(1)).save(detalleCompra);
    }

    @Test
    void updateDetalleCompra_DetalleCompraNotFound() {
        when(detalleCompraRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            detalleCompraServiceImpl.updateDetalleCompra(1L, detalleCompraDTO);
        });

        verify(detalleCompraRepository, never()).save(any(DetalleCompra.class));
    }

    @Test
    void updateDetalleCompra_CompraNotFound() {
        when(detalleCompraRepository.findById(1L)).thenReturn(Optional.of(detalleCompra));
        when(compraRepository.findById(detalleCompraDTO.getCompraId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            detalleCompraServiceImpl.updateDetalleCompra(1L, detalleCompraDTO);
        });

        verify(detalleCompraRepository, never()).save(any(DetalleCompra.class));
    }

    @Test
    void updateDetalleCompra_ProductoNotFound() {
        when(detalleCompraRepository.findById(1L)).thenReturn(Optional.of(detalleCompra));
        when(compraRepository.findById(detalleCompraDTO.getCompraId())).thenReturn(Optional.of(new Compra()));
        when(productoRepository.findById(detalleCompraDTO.getProductoId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            detalleCompraServiceImpl.updateDetalleCompra(1L, detalleCompraDTO);
        });

        verify(detalleCompraRepository, never()).save(any(DetalleCompra.class));
    }

    @Test
    void updateDetalleCompra_DataAccessException() {
        when(detalleCompraRepository.findById(1L)).thenReturn(Optional.of(detalleCompra));
        when(compraRepository.findById(detalleCompraDTO.getCompraId())).thenReturn(Optional.of(new Compra()));
        when(productoRepository.findById(detalleCompraDTO.getProductoId())).thenReturn(Optional.of(new Producto()));
        when(detalleCompraRepository.save(detalleCompra)).thenThrow(new RuntimeException());

        assertThrows(DataAccessException.class, () -> {
            detalleCompraServiceImpl.updateDetalleCompra(1L, detalleCompraDTO);
        });

        verify(detalleCompraRepository, times(1)).save(detalleCompra);
    }

    @Test
    void deleteDetalleCompra_Success() {
        when(detalleCompraRepository.existsById(1L)).thenReturn(true);

        detalleCompraServiceImpl.deleteDetalleCompra(1L);

        verify(detalleCompraRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDetalleCompra_DetalleCompraNotFound() {
        when(detalleCompraRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            detalleCompraServiceImpl.deleteDetalleCompra(1L);
        });

        verify(detalleCompraRepository, never()).deleteById(1L);
    }

    @Test
    void deleteDetalleCompra_DataAccessException() {
        when(detalleCompraRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException()).when(detalleCompraRepository).deleteById(1L);

        assertThrows(DataAccessException.class, () -> {
            detalleCompraServiceImpl.deleteDetalleCompra(1L);
        });

        verify(detalleCompraRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAllDetalleCompra_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        DetalleCompra detalleCompra = new DetalleCompra();
        detalleCompra.setId(1L);
        List<DetalleCompra> detalleCompraList = List.of(detalleCompra);
        Page<DetalleCompra> detalleCompraPage = new PageImpl<>(detalleCompraList, pageable, detalleCompraList.size());

        DetalleCompraDTO detalleCompraDTO = new DetalleCompraDTO();
        detalleCompraDTO.setId(1L);

        when(detalleCompraRepository.findAll(pageable)).thenReturn(detalleCompraPage);
        when(detalleCompraMapper.toDto(detalleCompra)).thenReturn(detalleCompraDTO);

        Page<DetalleCompraDTO> result = detalleCompraServiceImpl.getAllDetalleCompra(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        verify(detalleCompraRepository, times(1)).findAll(pageable);
    }

    @Test
    void getAllDetalleCompra_DataAccessException() {
        Pageable pageable = mock(Pageable.class);
        when(detalleCompraRepository.findAll(pageable)).thenThrow(new RuntimeException());

        assertThrows(DataAccessException.class, () -> {
            detalleCompraServiceImpl.getAllDetalleCompra(pageable);
        });

        verify(detalleCompraRepository, times(1)).findAll(pageable);
    }

    @Test
    void getDetalleCompraById_Success() {
        when(detalleCompraRepository.findById(1L)).thenReturn(Optional.of(detalleCompra));
        when(detalleCompraMapper.toDto(detalleCompra)).thenReturn(detalleCompraDTO);

        DetalleCompraDTO result = detalleCompraServiceImpl.getDetalleCompraById(1L);

        assertNotNull(result);
        verify(detalleCompraRepository, times(1)).findById(1L);
    }

    @Test
    void getDetalleCompraById_NotFound() {
        when(detalleCompraRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            detalleCompraServiceImpl.getDetalleCompraById(1L);
        });

        verify(detalleCompraRepository, times(1)).findById(1L);
    }

    @Test
    void getDetalleCompraById_DataAccessException() {
        when(detalleCompraRepository.findById(1L)).thenThrow(new RuntimeException());

        assertThrows(DataAccessException.class, () -> {
            detalleCompraServiceImpl.getDetalleCompraById(1L);
        });

        verify(detalleCompraRepository, times(1)).findById(1L);
    }
}