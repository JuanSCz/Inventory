package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Proveedor;
import com.interonda.Inventory.exception.ResourceNotFoundException;
import com.interonda.Inventory.repository.ProveedorRepository;
import com.interonda.Inventory.service.ProveedorService;

import java.util.List;
import java.util.Optional;

public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    public List<Proveedor> findAll() {
        return proveedorRepository.findAll();
    }

    @Override
    public Proveedor findById(Long id) {
        return proveedorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El proveedor N " + id + " no fue encontrado!"));
    }

    @Override
    public Proveedor save(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public void deleteById(Long id) {
        proveedorRepository.deleteById(id);
    }
}
