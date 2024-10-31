package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Compra;

import java.util.List;
import java.util.Optional;

public interface CompraService {
    List<Compra> findAll();

    Optional<Compra> findById(Long id);

    Compra save(Compra compra);

    void deleteById(Long id);
}
