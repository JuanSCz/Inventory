package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Deposito;
import com.interonda.Inventory.entity.Stock;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DepositoService {
    List<Deposito> findAll();

    Deposito findById(Long id);

    Deposito save(Deposito deposito);

    void deleteById(Long id);

    boolean existsByNombre(String nombre);

    public Deposito crearDeposito(Deposito deposito);

    public Deposito actualizarDeposito(Long id, Deposito depositoActualizado);

    public void eliminarDeposito(Long id);

    public List<Stock> listarStocksPorDeposito(Long depositoId);
}

