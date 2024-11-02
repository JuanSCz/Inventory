package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.DetalleCompra;

import java.util.List;
import java.util.Optional;

public interface DetalleCompraService {
    List<DetalleCompra> findAll();

    DetalleCompra findById(Long id);
    
    DetalleCompra save(DetalleCompra detalleCompra);

    void deleteById(Long id);
}
