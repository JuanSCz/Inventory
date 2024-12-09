package com.interonda.inventory.service;

import com.interonda.inventory.entity.Rol;
import com.interonda.inventory.dto.RolDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RolService {
    RolDTO convertToDto(Rol rol);

    Rol convertToEntity(RolDTO rolDTO);

    RolDTO createRol(RolDTO rolDTO);

    RolDTO updateRol(Long id, RolDTO rolDTO);

    void deleteRol(Long id);

    RolDTO getRol(Long id);

    Page<RolDTO> getAllRoles(Pageable pageable);
}
