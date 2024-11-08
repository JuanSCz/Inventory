package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Cliente;
import com.interonda.Inventory.exceptions.ConflictException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.repository.ClienteRepository;
import com.interonda.Inventory.service.ClienteService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ClienteServiceImpl.class);

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
        return clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El cliente no fue encontrado!"));
    }

    @Override
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Cliente crearCliente(Cliente cliente) {

        if (clienteRepository.existsByContacto(cliente.getContacto())) {
            logger.warn("Intento de crear cliente con contacto duplicado: {}", cliente.getContacto());
            throw new ConflictException("Ya existe un cliente con el mismo contacto.");
        }

        Cliente nuevoCliente = clienteRepository.save(cliente);

        return nuevoCliente;
    }

    @Override
    @Transactional
    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        Cliente clienteExistente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());

        // Actualizar los atributos necesarios
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setContacto(clienteActualizado.getContacto());
        clienteExistente.setDireccion(clienteActualizado.getDireccion());
        clienteExistente.setPais(clienteActualizado.getPais());
        clienteExistente.setCiudad(clienteActualizado.getCiudad());

        Cliente clienteGuardado = clienteRepository.save(clienteExistente);

        return clienteGuardado;
    }
}


