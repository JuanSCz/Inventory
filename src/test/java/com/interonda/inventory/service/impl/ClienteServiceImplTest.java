package com.interonda.inventory.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.interonda.inventory.entity.Cliente;
import com.interonda.inventory.dto.ClienteDTO;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.ClienteMapper;
import com.interonda.inventory.repository.ClienteRepository;
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
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private Validator validator;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private com.interonda.inventory.service.impl.ClienteServiceImpl ClienteServiceImpl;

    private ClienteDTO clienteDTO;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setNombre("Cliente Test");
        clienteDTO.setPais("Pais Test");
        clienteDTO.setCiudad("Ciudad Test");
        clienteDTO.setDireccion("Direccion Test");
        clienteDTO.setContactoCliente("Contacto Test");
        clienteDTO.seteMailCliente("email@test.com");

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Cliente Test");
        cliente.setPais("Pais Test");
        cliente.setCiudad("Ciudad Test");
        cliente.setDireccion("Direccion Test");
        cliente.setContactoCliente("Contacto Test");
        cliente.seteMailCliente("email@test.com");
    }

    @Test
    void createCliente_ShouldReturnClienteDTO_WhenClienteDTOIsValid() {
        // Arrange
        when(clienteMapper.toEntity(clienteDTO)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteMapper.toDto(cliente)).thenReturn(clienteDTO);

        // Act
        ClienteDTO result = ClienteServiceImpl.createCliente(clienteDTO);

        // Assert
        assertNotNull(result);
        assertEquals(clienteDTO.getId(), result.getId());
        assertEquals(clienteDTO.getNombre(), result.getNombre());
        verify(clienteMapper).toEntity(clienteDTO);
        verify(clienteRepository).save(cliente);
        verify(clienteMapper).toDto(cliente);
    }

    @Test
    void createCliente_ShouldThrowDataAccessException_WhenErrorSavingCliente() {
        // Arrange
        when(clienteMapper.toEntity(clienteDTO)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenThrow(new RuntimeException());

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> ClienteServiceImpl.createCliente(clienteDTO));

        // Verify
        assertEquals("Error guardando Cliente", exception.getMessage());
        verify(clienteMapper).toEntity(clienteDTO);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void updateCliente_ShouldReturnClienteDTO_WhenClienteDTOIsValid() {
        // Arrange
        when(clienteRepository.findById(clienteDTO.getId())).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteMapper.toDto(cliente)).thenReturn(clienteDTO);

        // Act
        ClienteDTO result = ClienteServiceImpl.updateCliente(clienteDTO.getId(), clienteDTO);

        // Assert
        assertNotNull(result);
        assertEquals(clienteDTO.getId(), result.getId());
        assertEquals(clienteDTO.getNombre(), result.getNombre());
        verify(clienteRepository).findById(clienteDTO.getId());
        verify(clienteRepository).save(cliente);
        verify(clienteMapper).toDto(cliente);
    }

    @Test
    void updateCliente_ShouldThrowResourceNotFoundException_WhenClienteNotFound() {
        // Arrange
        when(clienteRepository.findById(clienteDTO.getId())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> ClienteServiceImpl.updateCliente(clienteDTO.getId(), clienteDTO));

        // Verify
        assertEquals("Cliente no encontrado con el id: " + clienteDTO.getId(), exception.getMessage());
        verify(clienteRepository).findById(clienteDTO.getId());
    }

    @Test
    void updateCliente_ShouldThrowDataAccessException_WhenErrorSavingCliente() {
        // Arrange
        when(clienteRepository.findById(clienteDTO.getId())).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenThrow(new RuntimeException());

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> ClienteServiceImpl.updateCliente(clienteDTO.getId(), clienteDTO));

        // Verify
        assertEquals("Error actualizando Cliente", exception.getMessage());
        verify(clienteRepository).findById(clienteDTO.getId());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deleteCliente_ShouldDeleteCliente_WhenClienteExists() {
        // Arrange
        when(clienteRepository.existsById(clienteDTO.getId())).thenReturn(true);

        // Act
        ClienteServiceImpl.deleteCliente(clienteDTO.getId());

        // Assert
        verify(clienteRepository).existsById(clienteDTO.getId());
        verify(clienteRepository).deleteById(clienteDTO.getId());
    }

    @Test
    void deleteCliente_ShouldThrowResourceNotFoundException_WhenClienteNotFound() {
        // Arrange
        when(clienteRepository.existsById(clienteDTO.getId())).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> ClienteServiceImpl.deleteCliente(clienteDTO.getId()));

        // Verify
        assertEquals("Cliente no encontrado con el id: " + clienteDTO.getId(), exception.getMessage());
        verify(clienteRepository).existsById(clienteDTO.getId());
    }

    @Test
    void deleteCliente_ShouldThrowDataAccessException_WhenErrorDeletingCliente() {
        // Arrange
        when(clienteRepository.existsById(clienteDTO.getId())).thenReturn(true);
        doThrow(new RuntimeException()).when(clienteRepository).deleteById(clienteDTO.getId());

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> ClienteServiceImpl.deleteCliente(clienteDTO.getId()));

        // Verify
        assertEquals("Error eliminando Cliente", exception.getMessage());
        verify(clienteRepository).existsById(clienteDTO.getId());
        verify(clienteRepository).deleteById(clienteDTO.getId());
    }

    @Test
    void getAllClientes_ShouldReturnPageOfClienteDTO_WhenClientesExist() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Cliente> clientePage = new PageImpl<>(List.of(cliente), pageable, 1);
        when(clienteRepository.findAll(pageable)).thenReturn(clientePage);
        when(clienteMapper.toDto(cliente)).thenReturn(clienteDTO);

        // Act
        Page<ClienteDTO> result = ClienteServiceImpl.getAllClientes(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(clienteDTO.getId(), result.getContent().get(0).getId());
        verify(clienteRepository).findAll(pageable);
        verify(clienteMapper).toDto(cliente);
    }

    @Test
    void getAllClientes_ShouldThrowDataAccessException_WhenErrorGettingClientes() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(clienteRepository.findAll(pageable)).thenThrow(new RuntimeException());

        // Act & Assert
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            ClienteServiceImpl.getAllClientes(pageable);
        });

        // Verify
        assertEquals("Error obteniendo todos los Clientes con paginación", exception.getMessage());
        verify(clienteRepository).findAll(pageable);
    }

    @Test
    void getClienteById_ShouldReturnClienteDTO_WhenClienteExists() {
        // Arrange
        when(clienteRepository.findById(clienteDTO.getId())).thenReturn(Optional.of(cliente));
        when(clienteMapper.toDto(cliente)).thenReturn(clienteDTO);

        // Act
        ClienteDTO result = ClienteServiceImpl.getClienteById(clienteDTO.getId());

        // Assert
        assertNotNull(result);
        assertEquals(clienteDTO.getId(), result.getId());
        assertEquals(clienteDTO.getNombre(), result.getNombre());
        verify(clienteRepository).findById(clienteDTO.getId());
        verify(clienteMapper).toDto(cliente);
    }

    @Test
    void getClienteById_ShouldThrowResourceNotFoundException_WhenClienteNotFound() {
        // Arrange
        when(clienteRepository.findById(clienteDTO.getId())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> ClienteServiceImpl.getClienteById(clienteDTO.getId()));

        // Verify
        assertEquals("Cliente no encontrado con el id: " + clienteDTO.getId(), exception.getMessage());
        verify(clienteRepository).findById(clienteDTO.getId());
    }
}
