package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Rol;
import com.interonda.Inventory.entityDTO.RolDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RolService {
    RolDTO convertToDto(Rol rol);

    Rol convertToEntity(RolDTO rolDTO);

    RolDTO createRol(RolDTO rolDTO);

    RolDTO updateRol(Long id, RolDTO rolDTO);

    void deleteRol(Long id);

    RolDTO getRol(Long id);

    Page<RolDTO> getAllRoles(Pageable pageable);
}
