package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Cliente;
import com.interonda.Inventory.exception.ResourceNotFoundException;
import com.interonda.Inventory.repository.ClienteRepository;
import com.interonda.Inventory.service.ClienteService;

import java.util.List;
import java.util.Optional;


public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente findById(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El cliente N " + id + " no fue encontrado!"));
    }

    @Override
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }
}
