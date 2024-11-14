package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Rol;
import com.interonda.Inventory.entityDTO.RolDTO;

import java.util.List;

public interface RolService {
    RolDTO convertToDto(Rol rol);

    Rol convertToEntity(RolDTO rolDTO);

    RolDTO createRol(RolDTO rolDTO);

    RolDTO updateRol(Long id, RolDTO rolDTO);

    void deleteRol(Long id);

    RolDTO getRol(Long id);

    List<RolDTO> getAllRoles();
}
