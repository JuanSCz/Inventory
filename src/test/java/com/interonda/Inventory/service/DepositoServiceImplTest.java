package com.interonda.Inventory.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.interonda.Inventory.entity.Deposito;
import com.interonda.Inventory.entityDTO.DepositoDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.DepositoMapper;
import com.interonda.Inventory.repository.DepositoRepository;
import com.interonda.Inventory.service.impl.DepositoServiceImpl;
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

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DepositoServiceImplTest {

    @Mock
    private DepositoRepository depositoRepository;

    @Mock
    private Validator validator;

    @Mock
    private DepositoMapper depositoMapper;

    @InjectMocks
    private DepositoServiceImpl depositoServiceImpl;

    private DepositoDTO depositoDTO;
    private Deposito deposito;

    @BeforeEach
    void setUp() {
        depositoDTO = new DepositoDTO();
        depositoDTO.setId(1L);
        depositoDTO.setNombre("Deposito Test");
        depositoDTO.setProvincia("Provincia Test");
        depositoDTO.setDireccion("Direccion Test");
        depositoDTO.setContactoDeposito("Contacto Test");

        deposito = new Deposito();
        deposito.setId(1L);
        deposito.setNombre("Deposito Test");
        deposito.setProvincia("Provincia Test");
        deposito.setDireccion("Direccion Test");
        deposito.setContactoDeposito("Contacto Test");
    }

    @Test
    void createDepositoSuccessfully() {
        // Arrange
        when(depositoMapper.toEntity(depositoDTO)).thenReturn(deposito);
        when(depositoRepository.save(deposito)).thenReturn(deposito);
        when(depositoMapper.toDto(deposito)).thenReturn(depositoDTO);

        // Act
        DepositoDTO result = depositoServiceImpl.createDeposito(depositoDTO);

        // Assert
        assertNotNull(result);
        assertEquals(depositoDTO.getId(), result.getId());
        verify(depositoMapper).toEntity(depositoDTO);
        verify(depositoRepository).save(deposito);
        verify(depositoMapper).toDto(deposito);
    }

    @Test
    void createDepositoThrowsDataAccessExceptionWhenErrorSaving() {
        // Arrange
        when(depositoMapper.toEntity(depositoDTO)).thenReturn(deposito);
        when(depositoRepository.save(deposito)).thenThrow(new RuntimeException());

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> depositoServiceImpl.createDeposito(depositoDTO));

        // Assert
        assertEquals("Error creando Deposito", exception.getMessage());
        verify(depositoMapper).toEntity(depositoDTO);
        verify(depositoRepository).save(deposito);
    }

    @Test
    void updateDepositoSuccessfully() {
        // Arrange
        when(depositoRepository.findById(depositoDTO.getId())).thenReturn(Optional.of(deposito));
        when(depositoRepository.save(deposito)).thenReturn(deposito);
        when(depositoMapper.toDto(deposito)).thenReturn(depositoDTO);

        // Act
        DepositoDTO result = depositoServiceImpl.updateDeposito(depositoDTO.getId(), depositoDTO);

        // Assert
        assertNotNull(result);
        assertEquals(depositoDTO.getId(), result.getId());
        assertEquals(depositoDTO.getNombre(), result.getNombre());
        verify(depositoRepository).findById(depositoDTO.getId());
        verify(depositoRepository).save(deposito);
        verify(depositoMapper).toDto(deposito);
    }

    @Test
    void updateDepositoThrowsResourceNotFoundExceptionWhenDepositoNotFound() {
        // Arrange
        when(depositoRepository.findById(depositoDTO.getId())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> depositoServiceImpl.updateDeposito(depositoDTO.getId(), depositoDTO));

        // Assert
        assertEquals("Deposito no encontrado con el id: " + depositoDTO.getId(), exception.getMessage());
        verify(depositoRepository).findById(depositoDTO.getId());
    }

    @Test
    void updateDepositoThrowsDataAccessExceptionWhenErrorSaving() {
        // Arrange
        when(depositoRepository.findById(depositoDTO.getId())).thenReturn(Optional.of(deposito));
        when(depositoRepository.save(deposito)).thenThrow(new RuntimeException());

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> depositoServiceImpl.updateDeposito(depositoDTO.getId(), depositoDTO));

        // Assert
        assertEquals("Error actualizando Deposito por id: " + depositoDTO.getId(), exception.getMessage());
        verify(depositoRepository).findById(depositoDTO.getId());
        verify(depositoRepository).save(deposito);
    }

    @Test
    void deleteDepositoSuccessfully() {
        // Arrange
        when(depositoRepository.existsById(depositoDTO.getId())).thenReturn(true);

        // Act
        depositoServiceImpl.deleteDeposito(depositoDTO.getId());

        // Assert
        verify(depositoRepository).existsById(depositoDTO.getId());
        verify(depositoRepository).deleteById(depositoDTO.getId());
    }

    @Test
    void deleteDepositoThrowsResourceNotFoundExceptionWhenDepositoNotFound() {
        // Arrange
        when(depositoRepository.existsById(depositoDTO.getId())).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> depositoServiceImpl.deleteDeposito(depositoDTO.getId()));

        // Assert
        assertEquals("Deposito no encontrado con el id: " + depositoDTO.getId(), exception.getMessage());
        verify(depositoRepository).existsById(depositoDTO.getId());
    }

    @Test
    void deleteDepositoThrowsDataAccessExceptionWhenErrorDeleting() {
        // Arrange
        when(depositoRepository.existsById(depositoDTO.getId())).thenReturn(true);
        doThrow(new RuntimeException()).when(depositoRepository).deleteById(depositoDTO.getId());

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> depositoServiceImpl.deleteDeposito(depositoDTO.getId()));

        // Assert
        assertEquals("Error eliminando Deposito por id: " + depositoDTO.getId(), exception.getMessage());
        verify(depositoRepository).existsById(depositoDTO.getId());
        verify(depositoRepository).deleteById(depositoDTO.getId());
    }

    @Test
    void getAllDepositosSuccessfully() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Deposito> depositoPage = new PageImpl<>(List.of(deposito), pageable, 1);
        when(depositoRepository.findAll(pageable)).thenReturn(depositoPage);
        when(depositoMapper.toDto(deposito)).thenReturn(depositoDTO);

        // Act
        Page<DepositoDTO> result = depositoServiceImpl.getAllDepositos(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(depositoDTO.getId(), result.getContent().get(0).getId());
        verify(depositoRepository).findAll(pageable);
        verify(depositoMapper).toDto(deposito);
    }

    @Test
    void getAllDepositosThrowsDataAccessExceptionWhenErrorGetting() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(depositoRepository.findAll(pageable)).thenThrow(new RuntimeException());

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> depositoServiceImpl.getAllDepositos(pageable));

        // Assert
        assertEquals("Error obteniendo todos los Depositos con paginación", exception.getMessage());
    }

    @Test
    void getDepositoByIdSuccessfully() {
        // Arrange
        when(depositoRepository.findById(depositoDTO.getId())).thenReturn(Optional.of(deposito));
        when(depositoMapper.toDto(deposito)).thenReturn(depositoDTO);

        // Act
        DepositoDTO result = depositoServiceImpl.getDepositoById(depositoDTO.getId());

        // Assert
        assertNotNull(result);
        assertEquals(depositoDTO.getId(), result.getId());
        verify(depositoRepository).findById(depositoDTO.getId());
        verify(depositoMapper).toDto(deposito);
    }

    @Test
    void getDepositoByIdThrowsResourceNotFoundExceptionWhenDepositoNotFound() {
        // Arrange
        when(depositoRepository.findById(depositoDTO.getId())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> depositoServiceImpl.getDepositoById(depositoDTO.getId()));

        // Assert
        assertEquals("Deposito no encontrado con el id: " + depositoDTO.getId(), exception.getMessage());
        verify(depositoRepository).findById(depositoDTO.getId());
    }

    @Test
    void getDepositoByIdThrowsDataAccessExceptionWhenErrorGetting() {
        // Arrange
        when(depositoRepository.findById(depositoDTO.getId())).thenThrow(new RuntimeException());

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> depositoServiceImpl.getDepositoById(depositoDTO.getId()));

        // Assert
        assertEquals("Error obteniendo Deposito por id: " + depositoDTO.getId(), exception.getMessage());
        verify(depositoRepository).findById(depositoDTO.getId());
    }
}