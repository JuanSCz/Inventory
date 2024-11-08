package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {
    List<Cliente> findAll();

    Cliente findById(Long id);

    Cliente save(Cliente cliente);

    void deleteById(Long id);

    Cliente crearCliente(Cliente cliente);

    Cliente actualizarCliente(Long id, Cliente clienteActualizado);
}
