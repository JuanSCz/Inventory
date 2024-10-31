package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.DetalleVenta;

import java.util.List;
import java.util.Optional;

public interface DetalleVentaService {
    List<DetalleVenta> findAll();

    Optional<DetalleVenta> findById(Long id);

    DetalleVenta save(DetalleVenta detalleVenta);

    void deleteById(Long id);
}
