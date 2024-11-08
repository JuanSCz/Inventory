package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Venta;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.repository.VentaRepository;
import com.interonda.Inventory.service.VentaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VentaServiceImpl implements VentaService {

    private static final Logger logger = LoggerFactory.getLogger(VentaServiceImpl.class);

    private final VentaRepository ventaRepository;

    public VentaServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta findById(Long id) {
        return ventaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("La venta N " + id + " no fue encontrada!"));
    }

    @Override
    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public void deleteById(Long id) {
        ventaRepository.deleteById(id);
    }
}
