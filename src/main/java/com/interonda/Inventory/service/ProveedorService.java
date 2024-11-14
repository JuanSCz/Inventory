package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Proveedor;
import com.interonda.Inventory.entityDTO.ProveedorDTO;

import java.util.List;

public interface ProveedorService {

    ProveedorDTO convertToDto(Proveedor proveedor);

    Proveedor convertToEntity(ProveedorDTO proveedorDTO);

    ProveedorDTO createProveedor(ProveedorDTO proveedorDTO);

    ProveedorDTO updateProveedor(ProveedorDTO proveedorDTO);

    void deleteProveedor(Long id);

    ProveedorDTO getProveedor(Long id);

    List<ProveedorDTO> getAllProveedores();

}
