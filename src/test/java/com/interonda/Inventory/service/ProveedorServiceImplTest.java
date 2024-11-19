package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Proveedor;
import com.interonda.Inventory.entityDTO.ProveedorDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.ProveedorMapper;
import com.interonda.Inventory.repository.ProveedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProveedorServiceImplTest {

    private static final Long PROVEEDOR_ID = 1L;
    private static final String DB_ERROR_MESSAGE = "DB error";

    @Mock
    private ProveedorRepository proveedorRepository;

    @Mock
    private ProveedorMapper proveedorMapper;

    @InjectMocks
    private com.interonda.Inventory.service.impl.ProveedorServiceImpl proveedorServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProveedor_createsProveedorSuccessfully() {
        ProveedorDTO proveedorDTO = new ProveedorDTO();
        Proveedor proveedor = new Proveedor();
        Proveedor savedProveedor = new Proveedor();
        savedProveedor.setId(PROVEEDOR_ID);

        when(proveedorMapper.toEntity(proveedorDTO)).thenReturn(proveedor);
        when(proveedorRepository.save(proveedor)).thenReturn(savedProveedor);
        when(proveedorMapper.toDto(savedProveedor)).thenReturn(proveedorDTO);

        ProveedorDTO result = proveedorServiceImpl.createProveedor(proveedorDTO);

        assertNotNull(result);
        verify(proveedorRepository, times(1)).save(proveedor);
    }

    @Test
    void createProveedor_throwsDataAccessExceptionWhenSaveFails() {
        ProveedorDTO proveedorDTO = new ProveedorDTO();
        Proveedor proveedor = new Proveedor();

        when(proveedorMapper.toEntity(proveedorDTO)).thenReturn(proveedor);
        when(proveedorRepository.save(proveedor)).thenThrow(new DataAccessResourceFailureException(DB_ERROR_MESSAGE));

        assertThrows(DataAccessException.class, () -> proveedorServiceImpl.createProveedor(proveedorDTO));
        verify(proveedorRepository, times(1)).save(proveedor);
    }

    @Test
    void updateProveedor_updatesProveedorSuccessfully() {
        ProveedorDTO proveedorDTO = new ProveedorDTO();
        proveedorDTO.setId(PROVEEDOR_ID);
        Proveedor proveedor = new Proveedor();
        Proveedor updatedProveedor = new Proveedor();
        updatedProveedor.setId(PROVEEDOR_ID);

        when(proveedorRepository.findById(proveedorDTO.getId())).thenReturn(Optional.of(proveedor));
        when(proveedorMapper.toEntity(proveedorDTO)).thenReturn(proveedor);
        when(proveedorRepository.save(proveedor)).thenReturn(updatedProveedor);
        when(proveedorMapper.toDto(updatedProveedor)).thenReturn(proveedorDTO);

        ProveedorDTO result = proveedorServiceImpl.updateProveedor(proveedorDTO);

        assertNotNull(result);
        assertEquals(proveedorDTO, result);
        verify(proveedorRepository, times(1)).findById(proveedorDTO.getId());
        verify(proveedorRepository, times(1)).save(proveedor);
    }

    @Test
    void updateProveedor_throwsResourceNotFoundExceptionWhenProveedorNotFound() {
        ProveedorDTO proveedorDTO = new ProveedorDTO();
        proveedorDTO.setId(PROVEEDOR_ID);

        when(proveedorRepository.findById(proveedorDTO.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> proveedorServiceImpl.updateProveedor(proveedorDTO));
        verify(proveedorRepository, times(1)).findById(proveedorDTO.getId());
        verify(proveedorRepository, never()).save(any(Proveedor.class));
    }

    @Test
    void updateProveedor_throwsDataAccessExceptionWhenSaveFails() {
        ProveedorDTO proveedorDTO = new ProveedorDTO();
        proveedorDTO.setId(PROVEEDOR_ID);
        Proveedor proveedor = new Proveedor();

        when(proveedorRepository.findById(proveedorDTO.getId())).thenReturn(Optional.of(proveedor));
        when(proveedorMapper.toEntity(proveedorDTO)).thenReturn(proveedor);
        when(proveedorRepository.save(proveedor)).thenThrow(new DataAccessResourceFailureException(DB_ERROR_MESSAGE));

        assertThrows(DataAccessException.class, () -> proveedorServiceImpl.updateProveedor(proveedorDTO));
        verify(proveedorRepository, times(1)).findById(proveedorDTO.getId());
        verify(proveedorRepository, times(1)).save(proveedor);
    }

    @Test
    void deleteProveedor_deletesProveedorSuccessfully() {
        when(proveedorRepository.existsById(PROVEEDOR_ID)).thenReturn(true);

        proveedorServiceImpl.deleteProveedor(PROVEEDOR_ID);

        verify(proveedorRepository, times(1)).deleteById(PROVEEDOR_ID);
    }

    @Test
    void deleteProveedor_throwsResourceNotFoundExceptionWhenProveedorNotFound() {
        when(proveedorRepository.existsById(PROVEEDOR_ID)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> proveedorServiceImpl.deleteProveedor(PROVEEDOR_ID));
        verify(proveedorRepository, never()).deleteById(PROVEEDOR_ID);
    }

    @Test
    void deleteProveedor_throwsDataAccessExceptionWhenDeleteFails() {
        when(proveedorRepository.existsById(PROVEEDOR_ID)).thenReturn(true);
        doThrow(new DataAccessResourceFailureException(DB_ERROR_MESSAGE)).when(proveedorRepository).deleteById(PROVEEDOR_ID);

        assertThrows(DataAccessException.class, () -> proveedorServiceImpl.deleteProveedor(PROVEEDOR_ID));
        verify(proveedorRepository, times(1)).deleteById(PROVEEDOR_ID);
    }

    @Test
    void getProveedor_returnsProveedorDTOWhenFound() {
        Proveedor proveedor = new Proveedor();
        proveedor.setId(PROVEEDOR_ID);
        ProveedorDTO proveedorDTO = new ProveedorDTO();
        proveedorDTO.setId(PROVEEDOR_ID);

        when(proveedorRepository.findById(PROVEEDOR_ID)).thenReturn(Optional.of(proveedor));
        when(proveedorMapper.toDto(proveedor)).thenReturn(proveedorDTO);

        ProveedorDTO result = proveedorServiceImpl.getProveedor(PROVEEDOR_ID);

        assertNotNull(result);
        assertEquals(proveedorDTO, result);
        verify(proveedorRepository, times(1)).findById(PROVEEDOR_ID);
    }

    @Test
    void getProveedor_throwsResourceNotFoundExceptionWhenNotFound() {
        when(proveedorRepository.findById(PROVEEDOR_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> proveedorServiceImpl.getProveedor(PROVEEDOR_ID));
        verify(proveedorRepository, times(1)).findById(PROVEEDOR_ID);
    }

    @Test
    void getProveedor_throwsDataAccessExceptionWhenFindFails() {
        when(proveedorRepository.findById(PROVEEDOR_ID)).thenThrow(new DataAccessResourceFailureException(DB_ERROR_MESSAGE));

        assertThrows(DataAccessException.class, () -> proveedorServiceImpl.getProveedor(PROVEEDOR_ID));
        verify(proveedorRepository, times(1)).findById(PROVEEDOR_ID);
    }

    @Test
    void getAllProveedores_returnsPageOfProveedorDTOs() {
        Pageable pageable = mock(Pageable.class);
        Proveedor proveedor = new Proveedor();
        ProveedorDTO proveedorDTO = new ProveedorDTO();
        Page<Proveedor> proveedores = new PageImpl<>(Collections.singletonList(proveedor), pageable, 1);
        Page<ProveedorDTO> proveedorDTOs = new PageImpl<>(Collections.singletonList(proveedorDTO), pageable, 1);

        when(proveedorRepository.findAll(pageable)).thenReturn(proveedores);
        when(proveedorMapper.toDto(proveedor)).thenReturn(proveedorDTO);

        Page<ProveedorDTO> result = proveedorServiceImpl.getAllProveedores(pageable);

        assertNotNull(result, "El resultado no debe ser nulo");
        assertEquals(proveedorDTOs.getContent(), result.getContent(), "El contenido del resultado debe coincidir con el mock de proveedorDTOs");
        verify(proveedorRepository, times(1)).findAll(pageable);
        verify(proveedorMapper, times(1)).toDto(proveedor);
    }

    @Test
    void getAllProveedores_throwsDataAccessExceptionWhenFindAllFails() {
        Pageable pageable = mock(Pageable.class);

        when(proveedorRepository.findAll(pageable)).thenThrow(new DataAccessResourceFailureException(DB_ERROR_MESSAGE));

        assertThrows(DataAccessException.class, () -> proveedorServiceImpl.getAllProveedores(pageable));
        verify(proveedorRepository, times(1)).findAll(pageable);
    }
}