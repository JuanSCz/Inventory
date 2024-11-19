package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.HistorialStock;
import com.interonda.Inventory.entityDTO.HistorialStockDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.HistorialStockMapper;
import com.interonda.Inventory.repository.HistorialStockRepository;
import com.interonda.Inventory.service.impl.HistorialStockServiceImpl;
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
public class HistorialStockServiceImplTest {

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
        MockitoAnnotations.openMocks(this);
        historialStockDTO = new HistorialStockDTO();
        historialStock = new HistorialStock();
    }

    @Test
    void createHistorialStock_Success() {
        when(historialStockMapper.toEntity(historialStockDTO)).thenReturn(historialStock);
        when(historialStockRepository.save(historialStock)).thenReturn(historialStock);
        when(historialStockMapper.toDto(historialStock)).thenReturn(historialStockDTO);

        HistorialStockDTO result = historialStockServiceImpl.createHistorialStock(historialStockDTO);

        assertNotNull(result);
        assertEquals(historialStockDTO, result);
        verify(historialStockRepository, times(1)).save(historialStock);
        verify(historialStockMapper, times(1)).toEntity(historialStockDTO);
        verify(historialStockMapper, times(1)).toDto(historialStock);
    }

    @Test
    void createHistorialStock_DataAccessException() {
        when(historialStockMapper.toEntity(historialStockDTO)).thenReturn(historialStock);
        when(historialStockRepository.save(historialStock)).thenThrow(new RuntimeException("Database error"));

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            historialStockServiceImpl.createHistorialStock(historialStockDTO);
        });

        assertEquals("Error guardando HistorialStock", exception.getMessage());
        verify(historialStockRepository, times(1)).save(historialStock);
        verify(historialStockMapper, times(1)).toEntity(historialStockDTO);
        verify(historialStockMapper, never()).toDto(any(HistorialStock.class));
    }

    @Test
    void updateHistorialStock_Success() {
        when(historialStockMapper.toEntity(historialStockDTO)).thenReturn(historialStock);
        when(historialStockRepository.save(historialStock)).thenReturn(historialStock);
        when(historialStockMapper.toDto(historialStock)).thenReturn(historialStockDTO);

        HistorialStockDTO result = historialStockServiceImpl.updateHistorialStock(1L, historialStockDTO);

        assertNotNull(result);
        assertEquals(historialStockDTO, result);
        verify(historialStockRepository, times(1)).save(historialStock);
        verify(historialStockMapper, times(1)).toEntity(historialStockDTO);
        verify(historialStockMapper, times(1)).toDto(historialStock);
    }

    @Test
    void updateHistorialStock_DataAccessException() {
        when(historialStockMapper.toEntity(historialStockDTO)).thenReturn(historialStock);
        when(historialStockRepository.save(historialStock)).thenThrow(new RuntimeException("Database error"));

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            historialStockServiceImpl.updateHistorialStock(1L, historialStockDTO);
        });

        assertEquals("Error actualizando HistorialStock", exception.getMessage());
        verify(historialStockRepository, times(1)).save(historialStock);
        verify(historialStockMapper, times(1)).toEntity(historialStockDTO);
        verify(historialStockMapper, never()).toDto(any(HistorialStock.class));
    }

    @Test
    void deleteHistorialStock_Success() {
        doNothing().when(historialStockRepository).deleteById(1L);

        historialStockServiceImpl.deleteHistorialStock(1L);

        verify(historialStockRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteHistorialStock_DataAccessException() {
        doThrow(new RuntimeException("Database error")).when(historialStockRepository).deleteById(1L);

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            historialStockServiceImpl.deleteHistorialStock(1L);
        });

        assertEquals("Error eliminando HistorialStock", exception.getMessage());
        verify(historialStockRepository, times(1)).deleteById(1L);
    }

    @Test
    void getHistorialStock_Success() {
        when(historialStockRepository.findById(1L)).thenReturn(Optional.of(historialStock));
        when(historialStockMapper.toDto(historialStock)).thenReturn(historialStockDTO);

        HistorialStockDTO result = historialStockServiceImpl.getHistorialStock(1L);

        assertNotNull(result);
        assertEquals(historialStockDTO, result);
        verify(historialStockRepository, times(1)).findById(1L);
        verify(historialStockMapper, times(1)).toDto(historialStock);
    }

    @Test
    void getHistorialStock_NotFound() {
        when(historialStockRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            historialStockServiceImpl.getHistorialStock(1L);
        });

        assertEquals("HistorialStock no encontrado", exception.getMessage());
        verify(historialStockRepository, times(1)).findById(1L);
    }

    @Test
    void getHistorialStock_DataAccessException() {
        when(historialStockRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            historialStockServiceImpl.getHistorialStock(1L);
        });

        assertEquals("Error obteniendo HistorialStock", exception.getMessage());
        verify(historialStockRepository, times(1)).findById(1L);
    }

    @Test
    void getAllHistorialStock_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        HistorialStock historialStock = new HistorialStock();
        historialStock.setId(1L);
        List<HistorialStock> historialStockList = List.of(historialStock);
        Page<HistorialStock> historialStockPage = new PageImpl<>(historialStockList, pageable, historialStockList.size());

        HistorialStockDTO historialStockDTO = new HistorialStockDTO();
        historialStockDTO.setId(1L);

        when(historialStockRepository.findAll(pageable)).thenReturn(historialStockPage);
        when(historialStockMapper.toDto(historialStock)).thenReturn(historialStockDTO);

        Page<HistorialStockDTO> result = historialStockServiceImpl.getAllHistorialStock(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        verify(historialStockRepository, times(1)).findAll(pageable);
    }

    @Test
    void getAllHistorialStock_DataAccessException() {
        Pageable pageable = mock(Pageable.class);
        when(historialStockRepository.findAll(pageable)).thenThrow(new RuntimeException("Database error"));

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            historialStockServiceImpl.getAllHistorialStock(pageable);
        });

        assertEquals("Error obteniendo todos los HistorialStock con paginación", exception.getMessage());
        verify(historialStockRepository, times(1)).findAll(pageable);
    }
}