package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.DetalleCompra;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.repository.DetalleCompraRepository;
import com.interonda.Inventory.service.DetalleCompraService;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DetalleCompraServiceImpl implements DetalleCompraService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DetalleCompraServiceImpl.class);

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
