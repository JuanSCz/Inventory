package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.DetalleCompra;
import com.interonda.Inventory.exception.ResourceNotFoundException;
import com.interonda.Inventory.repository.DetalleCompraRepository;
import com.interonda.Inventory.service.DetalleCompraService;

import java.util.List;
import java.util.Optional;

public class DetalleCompraServiceImpl implements DetalleCompraService {

    private final DetalleCompraRepository detalleCompraRepository;

    public DetalleCompraServiceImpl(DetalleCompraRepository detalleCompraRepository) {
        this.detalleCompraRepository = detalleCompraRepository;
    }

    @Override
    public List<DetalleCompra> findAll() {
        return detalleCompraRepository.findAll();
    }

    @Override
    public DetalleCompra findById(Long id) {
        return detalleCompraRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El detalle de Compra N " + id + " no fue encontrado!"));
    }

    @Override
    public DetalleCompra save(DetalleCompra detalleCompra) {
        return detalleCompraRepository.save(detalleCompra);
    }

    @Override
    public void deleteById(Long id) {
        detalleCompraRepository.deleteById(id);
    }
}
