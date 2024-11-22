package com.interonda.Inventory.serviceImplTest;

import com.interonda.Inventory.entity.Venta;
import com.interonda.Inventory.dto.VentaDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.VentaMapper;
import com.interonda.Inventory.repository.VentaRepository;
import com.interonda.Inventory.serviceImplTest.impl.VentaServiceImpl;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VentaServiceImplTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private VentaMapper ventaMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private VentaServiceImpl ventaServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createVenta_Success() {
        VentaDTO ventaDTO = new VentaDTO();
        Venta venta = new Venta();
        Venta savedVenta = new Venta();
        savedVenta.setId(1L);

        when(ventaMapper.toEntity(any(VentaDTO.class))).thenReturn(venta);
        when(ventaRepository.save(any(Venta.class))).thenReturn(savedVenta);
        when(ventaMapper.toDto(any(Venta.class))).thenReturn(new VentaDTO());

        VentaDTO result = ventaServiceImpl.createVenta(ventaDTO);

        assertNotNull(result);
        verify(ventaRepository, times(1)).save(venta);
    }

    @Test
    void createVenta_ValidationFailure() {
        VentaDTO ventaDTO = new VentaDTO();

        doThrow(new DataAccessException("Validation failed")).when(validator).validate(any());

        assertThrows(DataAccessException.class, () -> ventaServiceImpl.createVenta(ventaDTO));
    }

    @Test
    void createVenta_DataAccessException() {
        VentaDTO ventaDTO = new VentaDTO();
        Venta venta = new Venta();

        when(ventaMapper.toEntity(any(VentaDTO.class))).thenReturn(venta);
        when(ventaRepository.save(any(Venta.class))).thenThrow(new DataAccessResourceFailureException("Database error"));

        assertThrows(DataAccessException.class, () -> ventaServiceImpl.createVenta(ventaDTO));
    }

    @Test
    void updateVenta_Success() {
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setId(1L);
        Venta venta = new Venta();
        Venta updatedVenta = new Venta();
        updatedVenta.setId(1L);

        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        when(ventaMapper.toEntity(any(VentaDTO.class))).thenReturn(venta);
        when(ventaRepository.save(any(Venta.class))).thenReturn(updatedVenta);
        when(ventaMapper.toDto(any(Venta.class))).thenReturn(new VentaDTO());

        VentaDTO result = ventaServiceImpl.updateVenta(ventaDTO);

        assertNotNull(result);
        verify(ventaRepository, times(1)).save(venta);
    }

    @Test
    void updateVenta_NotFound() {
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setId(1L);

        when(ventaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ventaServiceImpl.updateVenta(ventaDTO));
    }

    @Test
    void updateVenta_DataAccessException() {
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setId(1L);
        Venta venta = new Venta();

        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        when(ventaMapper.toEntity(any(VentaDTO.class))).thenReturn(venta);
        when(ventaRepository.save(any(Venta.class))).thenThrow(new DataAccessResourceFailureException("Database error"));

        assertThrows(DataAccessException.class, () -> ventaServiceImpl.updateVenta(ventaDTO));
    }

    @Test
    void deleteVenta_Success() {
        Long ventaId = 1L;

        when(ventaRepository.existsById(ventaId)).thenReturn(true);

        assertDoesNotThrow(() -> ventaServiceImpl.deleteVenta(ventaId));
        verify(ventaRepository, times(1)).deleteById(ventaId);
    }

    @Test
    void deleteVenta_NotFound() {
        Long ventaId = 1L;

        when(ventaRepository.existsById(ventaId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> ventaServiceImpl.deleteVenta(ventaId));
        verify(ventaRepository, never()).deleteById(ventaId);
    }

    @Test
    void deleteVenta_DataAccessException() {
        Long ventaId = 1L;

        when(ventaRepository.existsById(ventaId)).thenReturn(true);
        doThrow(new DataAccessResourceFailureException("Database error")).when(ventaRepository).deleteById(ventaId);

        assertThrows(DataAccessException.class, () -> ventaServiceImpl.deleteVenta(ventaId));
        verify(ventaRepository, times(1)).deleteById(ventaId);
    }

    @Test
    void getVenta_Success() {
        Long ventaId = 1L;
        Venta venta = new Venta();
        venta.setId(ventaId);

        when(ventaRepository.findById(ventaId)).thenReturn(Optional.of(venta));
        when(ventaMapper.toDto(any(Venta.class))).thenReturn(new VentaDTO());

        VentaDTO result = ventaServiceImpl.getVenta(ventaId);

        assertNotNull(result);
        verify(ventaRepository, times(1)).findById(ventaId);
    }

    @Test
    void getVenta_NotFound() {
        Long ventaId = 1L;

        when(ventaRepository.findById(ventaId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ventaServiceImpl.getVenta(ventaId));
        verify(ventaRepository, times(1)).findById(ventaId);
    }

    @Test
    void getVenta_DataAccessException() {
        Long ventaId = 1L;

        when(ventaRepository.findById(ventaId)).thenThrow(new DataAccessResourceFailureException("Database error"));

        assertThrows(DataAccessException.class, () -> ventaServiceImpl.getVenta(ventaId));
        verify(ventaRepository, times(1)).findById(ventaId);
    }

    @Test
    void getAllVentas_Success() {
        Pageable pageable = mock(Pageable.class);
        Page<Venta> ventasPage = mock(Page.class);
        when(ventaRepository.findAll(pageable)).thenReturn(ventasPage);
        when(ventasPage.map(any())).thenReturn(mock(Page.class));

        Page<VentaDTO> result = ventaServiceImpl.getAllVentas(pageable);

        assertNotNull(result);
        verify(ventaRepository, times(1)).findAll(pageable);
    }

    @Test
    void getAllVentas_DataAccessException() {
        Pageable pageable = mock(Pageable.class);

        when(ventaRepository.findAll(pageable)).thenThrow(new DataAccessResourceFailureException("Database error"));

        assertThrows(DataAccessException.class, () -> ventaServiceImpl.getAllVentas(pageable));
        verify(ventaRepository, times(1)).findAll(pageable);
    }
}