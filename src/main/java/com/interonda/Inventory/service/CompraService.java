package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Compra;

import java.util.List;;

public interface CompraService {
    List<Compra> findAll();

    Compra findById(Long id);

    Compra save(Compra compra);

    void deleteById(Long id);
}
