package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Deposito;

import java.util.List;
import java.util.Optional;

public interface DepositoService {
    List<Deposito> findAll();

    Optional<Deposito> findById(Long id);

    Deposito save(Deposito deposito);

    void deleteById(Long id);
}
