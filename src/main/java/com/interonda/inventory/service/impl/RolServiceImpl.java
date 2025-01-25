package com.interonda.inventory.service.impl;

import com.interonda.inventory.entity.Rol;
import com.interonda.inventory.dto.RolDTO;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.RolMapper;
import com.interonda.inventory.repository.RolRepository;
import com.interonda.inventory.service.RolService;

import com.interonda.inventory.utils.ValidatorUtils;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolServiceImpl implements RolService {

    private static final Logger logger = LoggerFactory.getLogger(RolServiceImpl.class);

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;
    private final Validator validator;

    @Autowired
    public RolServiceImpl(RolRepository rolRepository, RolMapper rolMapper, Validator validator) {
        this.rolRepository = rolRepository;
        this.rolMapper = rolMapper;
        this.validator = validator;
    }

    @Override
    public RolDTO convertToDto(Rol rol) {
        return rolMapper.toDto(rol);
    }

    @Override
    public Rol convertToEntity(RolDTO rolDTO) {
        return rolMapper.toEntity(rolDTO);
    }

    @Override
    @Transactional
    public RolDTO createRol(RolDTO rolDTO) {
        ValidatorUtils.validateEntity(rolDTO, validator);
        try {
            logger.info("Creando nuevo Rol");
            Rol rol = rolMapper.toEntity(rolDTO);
            Rol savedRol = rolRepository.save(rol);
            logger.info("Rol creado exitosamente con id: {}", savedRol.getId());
            return rolMapper.toDto(savedRol);
        } catch (Exception e) {
            logger.error("Error guardando Rol", e);
            throw new DataAccessException("Error guardando Rol", e);
        }
    }

    @Override
    @Transactional
    public RolDTO updateRol(Long id, RolDTO rolDTO) {
        ValidatorUtils.validateEntity(rolDTO, validator);
        try {
            logger.info("Actualizando Rol con id: {}", id);
            Rol rol = rolRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con el id: " + id));
            rol.setNombre(rolDTO.getNombre());
            Rol updatedRol = rolRepository.save(rol);
            logger.info("Rol actualizado exitosamente con id: {}", updatedRol.getId());
            return rolMapper.toDto(updatedRol);
        } catch (ResourceNotFoundException e) {
            logger.warn("Rol no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error actualizando Rol", e);
            throw new DataAccessException("Error actualizando Rol", e);
        }
    }


    @Override
    @Transactional
    public void deleteRol(Long id) {
        try {
            logger.info("Eliminando Rol con id: {}", id);
            if (!rolRepository.existsById(id)) {
                throw new ResourceNotFoundException("Rol no encontrado con el id: " + id);
            }
            rolRepository.deleteById(id);
            logger.info("Rol eliminado exitosamente con id: {}", id);
        } catch (ResourceNotFoundException e) {
            logger.warn("Rol no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error eliminando Rol", e);
            throw new DataAccessException("Error eliminando Rol", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RolDTO getRol(Long id) {
        try {
            logger.info("Obteniendo Rol con id: {}", id);
            Rol rol = rolRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con el id: " + id));
            return rolMapper.toDto(rol);
        } catch (ResourceNotFoundException e) {
            logger.warn("Rol no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo Rol", e);
            throw new DataAccessException("Error obteniendo Rol", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RolDTO> getAllRoles(Pageable pageable) {
        try {
            logger.info("Obteniendo todos los Roles con paginación");
            Page<Rol> roles = rolRepository.findAll(pageable);
            return roles.map(rolMapper::toDto);
        } catch (Exception e) {
            logger.error("Error obteniendo todos los Roles con paginación", e);
            throw new DataAccessException("Error obteniendo todos los Roles con paginación", e);
        }
    }


}

