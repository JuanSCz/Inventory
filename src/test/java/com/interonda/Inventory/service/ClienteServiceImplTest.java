package com.interonda.Inventory.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.interonda.Inventory.entity.Cliente;
import com.interonda.Inventory.entityDTO.ClienteDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.ClienteMapper;
import com.interonda.Inventory.repository.ClienteRepository;
import com.interonda.Inventory.service.impl.ClienteServiceImpl;
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
    private ClienteServiceImpl ClienteServiceImpl;

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
        when(clienteMapper.toEntity(clienteDTO)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteMapper.toDto(cliente)).thenReturn(clienteDTO);

        ClienteDTO result = ClienteServiceImpl.createCliente(clienteDTO);

        assertNotNull(result);
        assertEquals(clienteDTO.getId(), result.getId());
        assertEquals(clienteDTO.getNombre(), result.getNombre());
        verify(clienteMapper).toEntity(clienteDTO);
        verify(clienteRepository).save(cliente);
        verify(clienteMapper).toDto(cliente);
    }

    @Test
    void createCliente_ShouldThrowDataAccessException_WhenErrorSavingCliente() {
        when(clienteMapper.toEntity(clienteDTO)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenThrow(new RuntimeException());

        DataAccessException exception = assertThrows(DataAccessException.class, () -> ClienteServiceImpl.createCliente(clienteDTO));

        assertEquals("Error guardando Cliente", exception.getMessage());
        verify(clienteMapper).toEntity(clienteDTO);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void updateCliente_ShouldReturnClienteDTO_WhenClienteDTOIsValid() {
        when(clienteRepository.findById(clienteDTO.getId())).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteMapper.toDto(cliente)).thenReturn(clienteDTO);

        ClienteDTO result = ClienteServiceImpl.updateCliente(clienteDTO.getId(), clienteDTO);

        assertNotNull(result);
        assertEquals(clienteDTO.getId(), result.getId());
        assertEquals(clienteDTO.getNombre(), result.getNombre());
        verify(clienteRepository).findById(clienteDTO.getId());
        verify(clienteRepository).save(cliente);
        verify(clienteMapper).toDto(cliente);
    }

    @Test
    void updateCliente_ShouldThrowResourceNotFoundException_WhenClienteNotFound() {
        when(clienteRepository.findById(clienteDTO.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> ClienteServiceImpl.updateCliente(clienteDTO.getId(), clienteDTO));

        assertEquals("Cliente no encontrado con el id: " + clienteDTO.getId(), exception.getMessage());
        verify(clienteRepository).findById(clienteDTO.getId());
    }

    @Test
    void updateCliente_ShouldThrowDataAccessException_WhenErrorSavingCliente() {
        when(clienteRepository.findById(clienteDTO.getId())).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenThrow(new RuntimeException());

        DataAccessException exception = assertThrows(DataAccessException.class, () -> ClienteServiceImpl.updateCliente(clienteDTO.getId(), clienteDTO));

        assertEquals("Error actualizando Cliente", exception.getMessage());
        verify(clienteRepository).findById(clienteDTO.getId());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deleteCliente_ShouldDeleteCliente_WhenClienteExists() {
        when(clienteRepository.existsById(clienteDTO.getId())).thenReturn(true);

        ClienteServiceImpl.deleteCliente(clienteDTO.getId());

        verify(clienteRepository).existsById(clienteDTO.getId());
        verify(clienteRepository).deleteById(clienteDTO.getId());
    }

    @Test
    void deleteCliente_ShouldThrowResourceNotFoundException_WhenClienteNotFound() {
        when(clienteRepository.existsById(clienteDTO.getId())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> ClienteServiceImpl.deleteCliente(clienteDTO.getId()));

        assertEquals("Cliente no encontrado con el id: " + clienteDTO.getId(), exception.getMessage());
        verify(clienteRepository).existsById(clienteDTO.getId());
    }

    @Test
    void deleteCliente_ShouldThrowDataAccessException_WhenErrorDeletingCliente() {
        when(clienteRepository.existsById(clienteDTO.getId())).thenReturn(true);
        doThrow(new RuntimeException()).when(clienteRepository).deleteById(clienteDTO.getId());

        DataAccessException exception = assertThrows(DataAccessException.class, () -> ClienteServiceImpl.deleteCliente(clienteDTO.getId()));

        assertEquals("Error eliminando Cliente", exception.getMessage());
        verify(clienteRepository).existsById(clienteDTO.getId());
        verify(clienteRepository).deleteById(clienteDTO.getId());
    }

    @Test
    void getAllClientes_ShouldReturnPageOfClienteDTO_WhenClientesExist() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Cliente> clientePage = new PageImpl<>(List.of(cliente), pageable, 1);
        when(clienteRepository.findAll(pageable)).thenReturn(clientePage);
        when(clienteMapper.toDto(cliente)).thenReturn(clienteDTO);

        Page<ClienteDTO> result = ClienteServiceImpl.getAllClientes(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(clienteDTO.getId(), result.getContent().get(0).getId());
        verify(clienteRepository).findAll(pageable);
        verify(clienteMapper).toDto(cliente);
    }

    @Test
    void getAllClientes_ShouldThrowDataAccessException_WhenErrorGettingClientes() {
        Pageable pageable = PageRequest.of(0, 10);
        when(clienteRepository.findAll(pageable)).thenThrow(new RuntimeException());

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            ClienteServiceImpl.getAllClientes(pageable);
        });

        assertEquals("Error obteniendo todos los Clientes con paginación", exception.getMessage());
        verify(clienteRepository).findAll(pageable);
    }

    @Test
    void getClienteById_ShouldReturnClienteDTO_WhenClienteExists() {
        when(clienteRepository.findById(clienteDTO.getId())).thenReturn(Optional.of(cliente));
        when(clienteMapper.toDto(cliente)).thenReturn(clienteDTO);

        ClienteDTO result = ClienteServiceImpl.getClienteById(clienteDTO.getId());

        assertNotNull(result);
        assertEquals(clienteDTO.getId(), result.getId());
        assertEquals(clienteDTO.getNombre(), result.getNombre());
        verify(clienteRepository).findById(clienteDTO.getId());
        verify(clienteMapper).toDto(cliente);
    }

    @Test
    void getClienteById_ShouldThrowResourceNotFoundException_WhenClienteNotFound() {
        when(clienteRepository.findById(clienteDTO.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> ClienteServiceImpl.getClienteById(clienteDTO.getId()));

        assertEquals("Cliente no encontrado con el id: " + clienteDTO.getId(), exception.getMessage());
        verify(clienteRepository).findById(clienteDTO.getId());
    }
}
