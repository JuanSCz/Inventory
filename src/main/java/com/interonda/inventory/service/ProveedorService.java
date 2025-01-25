package com.interonda.inventory.service;

import com.interonda.inventory.entity.Proveedor;
import com.interonda.inventory.dto.ProveedorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

public interface ProveedorService {

    ProveedorDTO convertToDto(Proveedor proveedor);

    Proveedor convertToEntity(ProveedorDTO proveedorDTO);

    ProveedorDTO createProveedor(ProveedorDTO proveedorDTO);

    ProveedorDTO updateProveedor(ProveedorDTO proveedorDTO);

    ProveedorDTO getProveedor(Long id);

    Page<ProveedorDTO> getAllProveedores(Pageable pageable, Sort sort);

    Page<ProveedorDTO> searchProveedoresByName(String nombre, Pageable pageable);

    boolean deleteProveedor(Long id);

    long countProveedores();

    Page<Map<String, Object>> getAllProveedoresAsMap(Pageable pageable);
}
