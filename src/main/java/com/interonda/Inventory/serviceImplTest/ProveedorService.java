package com.interonda.Inventory.serviceImplTest;

import com.interonda.Inventory.entity.Proveedor;
import com.interonda.Inventory.dto.ProveedorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProveedorService {

    ProveedorDTO convertToDto(Proveedor proveedor);

    Proveedor convertToEntity(ProveedorDTO proveedorDTO);

    ProveedorDTO createProveedor(ProveedorDTO proveedorDTO);

    ProveedorDTO updateProveedor(ProveedorDTO proveedorDTO);

    void deleteProveedor(Long id);

    ProveedorDTO getProveedor(Long id);

    Page<ProveedorDTO> getAllProveedores(Pageable pageable);

}