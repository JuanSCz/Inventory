package com.interonda.Inventory.serviceImplTest;

import com.interonda.Inventory.entity.DetalleVenta;
import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.entity.Venta;
import com.interonda.Inventory.dto.DetalleVentaDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.DetalleVentaMapper;
import com.interonda.Inventory.repository.DetalleVentaRepository;
import com.interonda.Inventory.repository.ProductoRepository;
import com.interonda.Inventory.repository.VentaRepository;
import com.interonda.Inventory.serviceImplTest.impl.DetalleVentaServiceImpl;
import jakarta.persistence.PersistenceException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetalleVentaServiceImplTest {

    @Mock
    private DetalleVentaRepository detalleVentaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private DetalleVentaMapper detalleVentaMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private DetalleVentaServiceImpl detalleVentaService;

    private DetalleVentaDTO detalleVentaDTO;
    private DetalleVenta detalleVenta;
    private Venta venta;
    private Producto producto;

    @BeforeEach
    void setUp() {
        detalleVentaDTO = new DetalleVentaDTO();
        detalleVentaDTO.setVentaId(1L);
        detalleVentaDTO.setProductoId(1L);
        detalleVenta = new DetalleVenta();
        venta = new Venta();
        producto = new Producto();
    }

    @Test
    void createDetalleVenta_Success() {
        // Arrange
        when(ventaRepository.findById(detalleVentaDTO.getVentaId())).thenReturn(Optional.of(venta));
        when(productoRepository.findById(detalleVentaDTO.getProductoId())).thenReturn(Optional.of(producto));
        when(detalleVentaMapper.toEntity(detalleVentaDTO)).thenReturn(detalleVenta);
        when(detalleVentaRepository.save(any(DetalleVenta.class))).thenReturn(detalleVenta);
        when(detalleVentaMapper.toDto(detalleVenta)).thenReturn(detalleVentaDTO);

        // Act
        DetalleVentaDTO result = detalleVentaService.createDetalleVenta(detalleVentaDTO);

        // Assert
        assertAll("Validar creación de DetalleVenta", () -> assertNotNull(result, "El resultado no debe ser nulo"), () -> assertEquals(detalleVentaDTO, result, "El DTO devuelto debe coincidir con el DTO de entrada"), () -> verify(ventaRepository).findById(detalleVentaDTO.getVentaId()), () -> verify(productoRepository).findById(detalleVentaDTO.getProductoId()), () -> verify(detalleVentaRepository).save(detalleVenta));
    }

    @Test
    void createDetalleVenta_VentaNotFound() {
        // Arrange
        when(ventaRepository.findById(detalleVentaDTO.getVentaId())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            detalleVentaService.createDetalleVenta(detalleVentaDTO);
        });

        // Assert
        assertAll("Validar excepción de Venta no encontrada", () -> assertEquals("Venta no encontrada con el id: 1", exception.getMessage(), "El mensaje de la excepción debe ser correcto"), () -> verify(ventaRepository).findById(detalleVentaDTO.getVentaId()), () -> verify(productoRepository, never()).findById(detalleVentaDTO.getProductoId()));
    }

    @Test
    void createDetalleVenta_ProductoNotFound() {
        // Arrange
        when(ventaRepository.findById(detalleVentaDTO.getVentaId())).thenReturn(Optional.of(venta));
        when(productoRepository.findById(detalleVentaDTO.getProductoId())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            detalleVentaService.createDetalleVenta(detalleVentaDTO);
        });

        // Assert
        assertAll("Validar excepción de Producto no encontrado", () -> assertEquals("Producto no encontrado con el id: 1", exception.getMessage(), "El mensaje de la excepción debe ser correcto"), () -> verify(ventaRepository).findById(detalleVentaDTO.getVentaId()), () -> verify(productoRepository).findById(detalleVentaDTO.getProductoId()));
    }

    @Test
    void createDetalleVenta_PersistenceException() {
        // Arrange
        when(ventaRepository.findById(detalleVentaDTO.getVentaId())).thenReturn(Optional.of(venta));
        when(productoRepository.findById(detalleVentaDTO.getProductoId())).thenReturn(Optional.of(producto));
        when(detalleVentaMapper.toEntity(detalleVentaDTO)).thenReturn(detalleVenta);
        when(detalleVentaRepository.save(any(DetalleVenta.class))).thenThrow(new PersistenceException());

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            detalleVentaService.createDetalleVenta(detalleVentaDTO);
        });

        // Assert
        assertAll("Validar excepción de persistencia", () -> assertEquals("Error creando DetalleVenta", exception.getMessage(), "El mensaje de la excepción debe ser correcto"), () -> verify(ventaRepository).findById(detalleVentaDTO.getVentaId()), () -> verify(productoRepository).findById(detalleVentaDTO.getProductoId()), () -> verify(detalleVentaRepository).save(detalleVenta));
    }

    @Test
    void updateDetalleVenta_Success() {
        // Arrange
        Long id = 1L;
        detalleVentaDTO.setId(id);
        when(detalleVentaRepository.findById(id)).thenReturn(Optional.of(detalleVenta));
        when(detalleVentaMapper.toDto(detalleVenta)).thenReturn(detalleVentaDTO);
        when(ventaRepository.findById(detalleVentaDTO.getVentaId())).thenReturn(Optional.of(venta));
        when(productoRepository.findById(detalleVentaDTO.getProductoId())).thenReturn(Optional.of(producto));
        when(detalleVentaRepository.save(detalleVenta)).thenReturn(detalleVenta);

        // Act
        DetalleVentaDTO result = detalleVentaService.updateDetalleVenta(id, detalleVentaDTO);

        // Assert
        assertAll("Validar actualización de DetalleVenta", () -> assertNotNull(result, "El resultado no debe ser nulo"), () -> assertEquals(detalleVentaDTO, result, "El DTO devuelto debe coincidir con el DTO de entrada"), () -> verify(detalleVentaRepository).findById(id), () -> verify(ventaRepository).findById(detalleVentaDTO.getVentaId()), () -> verify(productoRepository).findById(detalleVentaDTO.getProductoId()), () -> verify(detalleVentaRepository).save(detalleVenta));
    }

    @Test
    @DisplayName("updateDetalleVenta - DetalleVenta Not Found")
    void updateDetalleVenta_DetalleVentaNotFound() {
        // Arrange
        Long id = 1L;
        when(detalleVentaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            detalleVentaService.updateDetalleVenta(id, detalleVentaDTO);
        });

        // Assert
        assertAll("Validar excepción de DetalleVenta no encontrado", () -> assertEquals("DetalleVenta no encontrado con el id: 1", exception.getMessage(), "El mensaje de la excepción debe ser correcto"), () -> verify(detalleVentaRepository).findById(id), () -> verify(ventaRepository, never()).findById(detalleVentaDTO.getVentaId()), () -> verify(productoRepository, never()).findById(detalleVentaDTO.getProductoId()), () -> verify(detalleVentaRepository, never()).save(detalleVenta));
    }

    @Test
    @DisplayName("updateDetalleVenta - Venta Not Found")
    void updateDetalleVenta_VentaNotFound() {
        // Arrange
        Long id = 1L;
        detalleVentaDTO.setId(id);
        when(detalleVentaRepository.findById(id)).thenReturn(Optional.of(detalleVenta));
        when(ventaRepository.findById(detalleVentaDTO.getVentaId())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            detalleVentaService.updateDetalleVenta(id, detalleVentaDTO);
        });

        // Assert
        assertAll("Validar excepción de Venta no encontrada", () -> assertEquals("Venta no encontrada con el id: 1", exception.getMessage(), "El mensaje de la excepción debe ser correcto"), () -> verify(detalleVentaRepository).findById(id), () -> verify(ventaRepository).findById(detalleVentaDTO.getVentaId()), () -> verify(productoRepository, never()).findById(detalleVentaDTO.getProductoId()), () -> verify(detalleVentaRepository, never()).save(detalleVenta));
    }

    @Test
    void updateDetalleVenta_ProductoNotFound() {
        // Arrange
        Long id = 1L;
        detalleVentaDTO.setId(id);
        when(detalleVentaRepository.findById(id)).thenReturn(Optional.of(detalleVenta));
        when(ventaRepository.findById(detalleVentaDTO.getVentaId())).thenReturn(Optional.of(venta));
        when(productoRepository.findById(detalleVentaDTO.getProductoId())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            detalleVentaService.updateDetalleVenta(id, detalleVentaDTO);
        });

        // Assert
        assertAll("Validar excepción de Producto no encontrado", () -> assertEquals("Producto no encontrado con el id: 1", exception.getMessage(), "El mensaje de la excepción debe ser correcto"), () -> verify(detalleVentaRepository).findById(id), () -> verify(ventaRepository).findById(detalleVentaDTO.getVentaId()), () -> verify(productoRepository).findById(detalleVentaDTO.getProductoId()), () -> verify(detalleVentaRepository, never()).save(detalleVenta));
    }

    @Test
    void updateDetalleVenta_PersistenceException() {
        // Arrange
        Long id = 1L;
        detalleVentaDTO.setId(id);
        when(detalleVentaRepository.findById(id)).thenReturn(Optional.of(detalleVenta));
        when(ventaRepository.findById(detalleVentaDTO.getVentaId())).thenReturn(Optional.of(venta));
        when(productoRepository.findById(detalleVentaDTO.getProductoId())).thenReturn(Optional.of(producto));
        when(detalleVentaRepository.save(detalleVenta)).thenThrow(new PersistenceException());

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            detalleVentaService.updateDetalleVenta(id, detalleVentaDTO);
        });

        // Assert
        assertAll("Validar excepción de persistencia", () -> assertEquals("Error actualizando DetalleVenta por id: " + id, exception.getMessage(), "El mensaje de la excepción debe ser correcto"), () -> verify(detalleVentaRepository).findById(id), () -> verify(ventaRepository).findById(detalleVentaDTO.getVentaId()), () -> verify(productoRepository).findById(detalleVentaDTO.getProductoId()), () -> verify(detalleVentaRepository).save(detalleVenta));
    }

    @Test
    void deleteDetalleVenta_Success() {
        // Arrange
        Long id = 1L;
        when(detalleVentaRepository.existsById(id)).thenReturn(true);

        // Act
        detalleVentaService.deleteDetalleVenta(id);

        // Assert
        assertAll("Validar eliminación de DetalleVenta", () -> verify(detalleVentaRepository).existsById(id), () -> verify(detalleVentaRepository).deleteById(id));
    }

    @Test
    void deleteDetalleVenta_DetalleVentaNotFound() {
        // Arrange
        Long id = 1L;
        when(detalleVentaRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            detalleVentaService.deleteDetalleVenta(id);
        });

        // Assert
        assertAll("Validar excepción de DetalleVenta no encontrado", () -> assertEquals("DetalleVenta no encontrado con el id: 1", exception.getMessage(), "El mensaje de la excepción debe ser correcto"), () -> verify(detalleVentaRepository).existsById(id), () -> verify(detalleVentaRepository, never()).deleteById(id));
    }

    @Test
    void getAllDetalleVentas_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<DetalleVenta> detalleVentaPage = new PageImpl<>(List.of(detalleVenta));
        when(detalleVentaRepository.findAll(pageable)).thenReturn(detalleVentaPage);
        when(detalleVentaMapper.toDto(detalleVenta)).thenReturn(detalleVentaDTO);

        // Act
        Page<DetalleVentaDTO> result = detalleVentaService.getAllDetalleVentas(pageable);

        // Assert
        assertAll("Validar obtención de todos los DetalleVenta", () -> assertNotNull(result, "El resultado no debe ser nulo"), () -> assertEquals(1, result.getTotalElements(), "El número total de elementos debe ser 1"), () -> verify(detalleVentaRepository).findAll(pageable), () -> verify(detalleVentaMapper).toDto(detalleVenta));
    }

    @Test
    void getAllDetalleVentas_EmptyPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<DetalleVenta> detalleVentaPage = new PageImpl<>(Collections.emptyList());
        when(detalleVentaRepository.findAll(pageable)).thenReturn(detalleVentaPage);

        // Act
        Page<DetalleVentaDTO> result = detalleVentaService.getAllDetalleVentas(pageable);

        // Assert
        assertAll("Validar obtención de todos los DetalleVenta con página vacía", () -> assertNotNull(result, "El resultado no debe ser nulo"), () -> assertEquals(0, result.getTotalElements(), "El número total de elementos debe ser 0"), () -> verify(detalleVentaRepository).findAll(pageable), () -> verify(detalleVentaMapper, never()).toDto(any(DetalleVenta.class)));
    }

    @Test
    void getAllDetalleVentas_DataAccessException() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(detalleVentaRepository.findAll(pageable)).thenThrow(new PersistenceException());

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            detalleVentaService.getAllDetalleVentas(pageable);
        });

        // Assert
        assertAll("Validar excepción de acceso a datos", () -> assertEquals("Error obteniendo todos los DetalleVenta con paginación", exception.getMessage(), "El mensaje de la excepción debe ser correcto"), () -> verify(detalleVentaRepository).findAll(pageable));
    }

    @Test
    void getDetalleVentaById_Success() {
        // Arrange
        Long id = 1L;
        when(detalleVentaRepository.findById(id)).thenReturn(Optional.of(detalleVenta));
        when(detalleVentaMapper.toDto(detalleVenta)).thenReturn(detalleVentaDTO);

        // Act
        DetalleVentaDTO result = detalleVentaService.getDetalleVentaById(id);

        // Assert
        assertAll("Validar obtención de DetalleVenta por id", () -> assertNotNull(result, "El resultado no debe ser nulo"), () -> assertEquals(detalleVentaDTO, result, "El DTO devuelto debe coincidir con el esperado"), () -> verify(detalleVentaRepository).findById(id), () -> verify(detalleVentaMapper).toDto(detalleVenta));
    }

    @Test
    void getDetalleVentaById_NotFound() {
        // Arrange
        Long id = 1L;
        when(detalleVentaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            detalleVentaService.getDetalleVentaById(id);
        });

        // Assert
        assertAll("Validar excepción de DetalleVenta no encontrado", () -> assertEquals("DetalleVenta no encontrado con el id: 1", exception.getMessage(), "El mensaje de la excepción debe ser correcto"), () -> verify(detalleVentaRepository).findById(id), () -> verify(detalleVentaMapper, never()).toDto(any(DetalleVenta.class)));
    }

    @Test
    void getDetalleVentaById_DataAccessException() {
        // Arrange
        Long id = 1L;
        when(detalleVentaRepository.findById(id)).thenThrow(new PersistenceException());

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            detalleVentaService.getDetalleVentaById(id);
        });

        // Assert
        assertAll("Validar excepción de acceso a datos", () -> assertEquals("Error obteniendo DetalleVenta por id: " + id, exception.getMessage(), "El mensaje de la excepción debe ser correcto"), () -> verify(detalleVentaRepository).findById(id));
    }


}
