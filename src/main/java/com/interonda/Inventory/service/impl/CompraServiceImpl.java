package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Compra;
import com.interonda.Inventory.exception.ResourceNotFoundException;
import com.interonda.Inventory.repository.CompraRepository;
import com.interonda.Inventory.service.CompraService;

import java.util.List;
import java.util.Optional;

public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;

    public CompraServiceImpl(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    @Override
    public List<Compra> findAll() {
        return compraRepository.findAll();
    }

    @Override
    public Compra findById(Long id) {
        return compraRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("La compra N " + id + " no fue encontrada!");
        });
    }

    @Override
    public Compra save(Compra compra) {
        return compraRepository.save(compra);
    }

    @Override
    public void deleteById(Long id) {
        compraRepository.deleteById(id);
    }
}
