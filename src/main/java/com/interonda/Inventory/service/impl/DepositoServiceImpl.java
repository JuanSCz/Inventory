package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Deposito;
import com.interonda.Inventory.exception.ResourceNotFoundException;
import com.interonda.Inventory.repository.DepositoRepository;
import com.interonda.Inventory.service.DepositoService;

import java.util.List;
import java.util.Optional;

public class DepositoServiceImpl implements DepositoService {

    private final DepositoRepository depositoRepository;

    public DepositoServiceImpl(DepositoRepository depositoRepository) {
        this.depositoRepository = depositoRepository;
    }

    @Override
    public List<Deposito> findAll() {
        return depositoRepository.findAll();
    }

    @Override
    public Deposito findById(Long id) {
        return depositoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El deposito N " + id + " no fue encontrado!"));
    }

    @Override
    public Deposito save(Deposito deposito) {
        return depositoRepository.save(deposito);
    }

    @Override
    public void deleteById(Long id) {
        depositoRepository.deleteById(id);
    }
}
