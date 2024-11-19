package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.DetalleVenta;
import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.entity.Venta;
import com.interonda.Inventory.entityDTO.DetalleVentaDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.DetalleVentaMapper;
import com.interonda.Inventory.repository.DetalleVentaRepository;
import com.interonda.Inventory.repository.ProductoRepository;
import com.interonda.Inventory.repository.VentaRepository;
import com.interonda.Inventory.service.impl.DetalleVentaServiceImpl;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DetalleVentaServiceImplTest {

    @Mock
    private DetalleVentaRepository detalleVentaRepository;

    @Mock
    private Validator validator;

    @Mock
    private DetalleVentaMapper detalleVentaMapper;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private DetalleVentaServiceImpl detalleVentaServiceImpl;

    private DetalleVentaDTO detalleVentaDTO;
    private DetalleVenta detalleVenta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        detalleVentaDTO = new DetalleVentaDTO();
        detalleVenta = new DetalleVenta();
    }

    @Test
    void createDetalleVenta_Success() {
        when(detalleVentaMapper.toEntity(detalleVentaDTO)).thenReturn(detalleVenta);
        when(detalleVentaRepository.save(detalleVenta)).thenReturn(detalleVenta);
        when(detalleVentaMapper.toDto(detalleVenta)).thenReturn(detalleVentaDTO);

        DetalleVentaDTO result = detalleVentaServiceImpl.createDetalleVenta(detalleVentaDTO);

        assertNotNull(result);
        assertEquals(detalleVentaDTO, result);
        verify(detalleVentaRepository, times(1)).save(detalleVenta);
        verify(detalleVentaMapper, times(1)).toEntity(detalleVentaDTO);
        verify(detalleVentaMapper, times(1)).toDto(detalleVenta);
    }

    @Test
    void createDetalleVenta_DataAccessException() {
        when(detalleVentaMapper.toEntity(detalleVentaDTO)).thenReturn(detalleVenta);
        when(detalleVentaRepository.save(detalleVenta)).thenThrow(new RuntimeException("Database error"));

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            detalleVentaServiceImpl.createDetalleVenta(detalleVentaDTO);
        });

        assertEquals("Error creando DetalleVenta", exception.getMessage());
        verify(detalleVentaRepository, times(1)).save(detalleVenta);
        verify(detalleVentaMapper, times(1)).toEntity(detalleVentaDTO);
        verify(detalleVentaMapper, never()).toDto(any(DetalleVenta.class));
    }

    @Test
    void updateDetalleVenta_Success() {
        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(detalleVenta));
        when(ventaRepository.findById(detalleVentaDTO.getVentaId())).thenReturn(Optional.of(new Venta()));
        when(productoRepository.findById(detalleVentaDTO.getProductoId())).thenReturn(Optional.of(new Producto()));
        when(detalleVentaRepository.save(detalleVenta)).thenReturn(detalleVenta);
        when(detalleVentaMapper.toDto(detalleVenta)).thenReturn(detalleVentaDTO);

        DetalleVentaDTO result = detalleVentaServiceImpl.updateDetalleVenta(1L, detalleVentaDTO);

        assertNotNull(result);
        assertEquals(detalleVentaDTO, result);
        verify(detalleVentaRepository, times(1)).save(detalleVenta);
        verify(detalleVentaMapper, times(1)).toDto(detalleVenta);
    }

    @Test
    void updateDetalleVenta_DetalleVentaNotFound() {
        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            detalleVentaServiceImpl.updateDetalleVenta(1L, detalleVentaDTO);
        });

        verify(detalleVentaRepository, never()).save(any(DetalleVenta.class));
    }

    @Test
    void updateDetalleVenta_VentaNotFound() {
        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(detalleVenta));
        when(ventaRepository.findById(detalleVentaDTO.getVentaId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            detalleVentaServiceImpl.updateDetalleVenta(1L, detalleVentaDTO);
        });

        verify(detalleVentaRepository, never()).save(any(DetalleVenta.class));
    }

    @Test
    void updateDetalleVenta_ProductoNotFound() {
        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(detalleVenta));
        when(ventaRepository.findById(detalleVentaDTO.getVentaId())).thenReturn(Optional.of(new Venta()));
        when(productoRepository.findById(detalleVentaDTO.getProductoId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            detalleVentaServiceImpl.updateDetalleVenta(1L, detalleVentaDTO);
        });

        verify(detalleVentaRepository, never()).save(any(DetalleVenta.class));
    }

    @Test
    void updateDetalleVenta_DataAccessException() {
        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(detalleVenta));
        when(ventaRepository.findById(detalleVentaDTO.getVentaId())).thenReturn(Optional.of(new Venta()));
        when(productoRepository.findById(detalleVentaDTO.getProductoId())).thenReturn(Optional.of(new Producto()));
        when(detalleVentaRepository.save(detalleVenta)).thenThrow(new RuntimeException("Database error"));

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            detalleVentaServiceImpl.updateDetalleVenta(1L, detalleVentaDTO);
        });

        assertEquals("Error actualizando DetalleVenta por id: 1", exception.getMessage());
        verify(detalleVentaRepository, times(1)).save(detalleVenta);
    }

    @Test
    void deleteDetalleVenta_Success() {
        when(detalleVentaRepository.existsById(1L)).thenReturn(true);

        detalleVentaServiceImpl.deleteDetalleVenta(1L);

        verify(detalleVentaRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDetalleVenta_DetalleVentaNotFound() {
        when(detalleVentaRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            detalleVentaServiceImpl.deleteDetalleVenta(1L);
        });

        assertEquals("DetalleVenta no encontrado con el id: 1", exception.getMessage());
        verify(detalleVentaRepository, never()).deleteById(1L);
    }

    @Test
    void deleteDetalleVenta_DataAccessException() {
        when(detalleVentaRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(detalleVentaRepository).deleteById(1L);

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            detalleVentaServiceImpl.deleteDetalleVenta(1L);
        });

        assertEquals("Error eliminando DetalleVenta por id: 1", exception.getMessage());
        verify(detalleVentaRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAllDetalleVentas_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        DetalleVenta detalleVenta = new DetalleVenta();
        detalleVenta.setId(1L);
        List<DetalleVenta> detalleVentaList = List.of(detalleVenta);
        Page<DetalleVenta> detalleVentaPage = new PageImpl<>(detalleVentaList, pageable, detalleVentaList.size());

        DetalleVentaDTO detalleVentaDTO = new DetalleVentaDTO();
        detalleVentaDTO.setId(1L);

        when(detalleVentaRepository.findAll(pageable)).thenReturn(detalleVentaPage);
        when(detalleVentaMapper.toDto(detalleVenta)).thenReturn(detalleVentaDTO);

        Page<DetalleVentaDTO> result = detalleVentaServiceImpl.getAllDetalleVentas(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        verify(detalleVentaRepository, times(1)).findAll(pageable);
    }

    @Test
    void getAllDetalleVentas_DataAccessException() {
        Pageable pageable = mock(Pageable.class);
        when(detalleVentaRepository.findAll(pageable)).thenThrow(new RuntimeException("Database error"));

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            detalleVentaServiceImpl.getAllDetalleVentas(pageable);
        });

        assertEquals("Error obteniendo todos los DetalleVenta con paginación", exception.getMessage());
        verify(detalleVentaRepository, times(1)).findAll(pageable);
    }

    @Test
    void getDetalleVentaById_Success() {
        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(detalleVenta));
        when(detalleVentaMapper.toDto(detalleVenta)).thenReturn(detalleVentaDTO);

        DetalleVentaDTO result = detalleVentaServiceImpl.getDetalleVentaById(1L);

        assertNotNull(result);
        assertEquals(detalleVentaDTO, result);
        verify(detalleVentaRepository, times(1)).findById(1L);
        verify(detalleVentaMapper, times(1)).toDto(detalleVenta);
    }

    @Test
    void getDetalleVentaById_NotFound() {
        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            detalleVentaServiceImpl.getDetalleVentaById(1L);
        });

        assertEquals("DetalleVenta no encontrado con el id: 1", exception.getMessage());
        verify(detalleVentaRepository, times(1)).findById(1L);
    }

    @Test
    void getDetalleVentaById_DataAccessException() {
        when(detalleVentaRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            detalleVentaServiceImpl.getDetalleVentaById(1L);
        });

        assertEquals("Error obteniendo DetalleVenta por id: 1", exception.getMessage());
        verify(detalleVentaRepository, times(1)).findById(1L);
    }
}
