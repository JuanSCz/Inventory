package com.interonda.Inventory.serviceImplTest;

import com.interonda.Inventory.entity.Rol;
import com.interonda.Inventory.dto.RolDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.RolMapper;
import com.interonda.Inventory.repository.RolRepository;
import com.interonda.Inventory.serviceImplTest.impl.RolServiceImpl;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolServiceImplTest {
    @Mock
    private RolRepository rolRepository;

    @Mock
    private RolMapper rolMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private RolServiceImpl rolServiceImpl;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rolServiceImpl = new RolServiceImpl(rolRepository, rolMapper, validator);
    }

    @Test
    void createRol_createsRolSuccessfully() {
        RolDTO rolDTO = new RolDTO();
        Rol rol = new Rol();
        Rol savedRol = new Rol();
        savedRol.setId(1L);

        when(rolMapper.toEntity(rolDTO)).thenReturn(rol);
        when(rolRepository.save(rol)).thenReturn(savedRol);
        when(rolMapper.toDto(savedRol)).thenReturn(rolDTO);

        RolDTO result = rolServiceImpl.createRol(rolDTO);

        assertNotNull(result);
        assertEquals(rolDTO, result);
        verify(rolRepository, times(1)).save(rol);
    }

    @Test
    void createRol_throwsDataAccessExceptionWhenSaveFails() {
        RolDTO rolDTO = new RolDTO();
        Rol rol = new Rol();

        when(rolMapper.toEntity(rolDTO)).thenReturn(rol);
        when(rolRepository.save(rol)).thenThrow(new DataAccessResourceFailureException("DB error"));

        assertThrows(DataAccessException.class, () -> rolServiceImpl.createRol(rolDTO));
        verify(rolRepository, times(1)).save(rol);
    }

    @Test
    void updateRol_updatesRolSuccessfully() {
        RolDTO rolDTO = new RolDTO();
        rolDTO.setId(1L);
        rolDTO.setNombre("Updated Name");
        Rol rol = new Rol();
        rol.setId(1L);
        Rol updatedRol = new Rol();
        updatedRol.setId(1L);
        updatedRol.setNombre("Updated Name");

        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(rolRepository.save(rol)).thenReturn(updatedRol);
        when(rolMapper.toDto(updatedRol)).thenReturn(rolDTO);

        RolDTO result = rolServiceImpl.updateRol(1L, rolDTO);

        assertNotNull(result);
        assertEquals(rolDTO, result);
        verify(rolRepository, times(1)).findById(1L);
        verify(rolRepository, times(1)).save(rol);
    }

    @Test
    void updateRol_throwsResourceNotFoundExceptionWhenRolNotFound() {
        RolDTO rolDTO = new RolDTO();
        rolDTO.setId(1L);

        when(rolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rolServiceImpl.updateRol(1L, rolDTO));
        verify(rolRepository, times(1)).findById(1L);
        verify(rolRepository, never()).save(any(Rol.class));
    }

    @Test
    void updateRol_throwsDataAccessExceptionWhenSaveFails() {
        RolDTO rolDTO = new RolDTO();
        rolDTO.setId(1L);
        Rol rol = new Rol();
        rol.setId(1L);

        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(rolRepository.save(rol)).thenThrow(new DataAccessResourceFailureException("DB error"));

        assertThrows(DataAccessException.class, () -> rolServiceImpl.updateRol(1L, rolDTO));
        verify(rolRepository, times(1)).findById(1L);
        verify(rolRepository, times(1)).save(rol);
    }

    @Test
    void deleteRol_deletesRolSuccessfully() {
        Long rolId = 1L;

        when(rolRepository.existsById(rolId)).thenReturn(true);

        rolServiceImpl.deleteRol(rolId);

        verify(rolRepository, times(1)).deleteById(rolId);
    }

    @Test
    void deleteRol_throwsResourceNotFoundExceptionWhenRolNotFound() {
        Long rolId = 1L;

        when(rolRepository.existsById(rolId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> rolServiceImpl.deleteRol(rolId));
        verify(rolRepository, never()).deleteById(rolId);
    }

    @Test
    void deleteRol_throwsDataAccessExceptionWhenDeleteFails() {
        Long rolId = 1L;

        when(rolRepository.existsById(rolId)).thenReturn(true);
        doThrow(new DataAccessResourceFailureException("DB error")).when(rolRepository).deleteById(rolId);

        assertThrows(DataAccessException.class, () -> rolServiceImpl.deleteRol(rolId));
        verify(rolRepository, times(1)).deleteById(rolId);
    }

    @Test
    void getRol_returnsRolDTOWhenFound() {
        Long rolId = 1L;
        Rol rol = new Rol();
        rol.setId(rolId);
        RolDTO rolDTO = new RolDTO();
        rolDTO.setId(rolId);

        when(rolRepository.findById(rolId)).thenReturn(Optional.of(rol));
        when(rolMapper.toDto(rol)).thenReturn(rolDTO);

        RolDTO result = rolServiceImpl.getRol(rolId);

        assertNotNull(result);
        assertEquals(rolDTO, result);
        verify(rolRepository, times(1)).findById(rolId);
    }

    @Test
    void getRol_throwsResourceNotFoundExceptionWhenNotFound() {
        Long rolId = 1L;

        when(rolRepository.findById(rolId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rolServiceImpl.getRol(rolId));
        verify(rolRepository, times(1)).findById(rolId);
    }

    @Test
    void getRol_throwsDataAccessExceptionWhenFindFails() {
        Long rolId = 1L;

        when(rolRepository.findById(rolId)).thenThrow(new DataAccessResourceFailureException("DB error"));

        assertThrows(DataAccessException.class, () -> rolServiceImpl.getRol(rolId));
        verify(rolRepository, times(1)).findById(rolId);
    }

    @Test
    void getAllRoles_returnsPageOfRolDTOs() {
        Pageable pageable = mock(Pageable.class);
        Rol rol = new Rol();
        RolDTO rolDTO = new RolDTO();
        Page<Rol> roles = new PageImpl<>(Collections.singletonList(rol), pageable, 1);
        Page<RolDTO> rolDTOs = new PageImpl<>(Collections.singletonList(rolDTO), pageable, 1);

        when(rolRepository.findAll(pageable)).thenReturn(roles);
        when(rolMapper.toDto(rol)).thenReturn(rolDTO);

        Page<RolDTO> result = rolServiceImpl.getAllRoles(pageable);

        assertNotNull(result);
        assertEquals(rolDTOs.getContent(), result.getContent());
        verify(rolRepository, times(1)).findAll(pageable);
        verify(rolMapper, times(1)).toDto(rol);
    }

    @Test
    void getAllRoles_throwsDataAccessExceptionWhenFindAllFails() {
        Pageable pageable = mock(Pageable.class);

        when(rolRepository.findAll(pageable)).thenThrow(new DataAccessResourceFailureException("DB error"));

        assertThrows(DataAccessException.class, () -> rolServiceImpl.getAllRoles(pageable));
        verify(rolRepository, times(1)).findAll(pageable);
    }
}

