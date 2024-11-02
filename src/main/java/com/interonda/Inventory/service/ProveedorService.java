package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {
    List<Proveedor> findAll();

    Proveedor findById(Long id);

    Proveedor save(Proveedor proveedor);

    void deleteById(Long id);
}
