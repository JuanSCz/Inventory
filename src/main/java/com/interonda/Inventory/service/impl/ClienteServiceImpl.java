package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Cliente;
import com.interonda.Inventory.entity.Venta;
import com.interonda.Inventory.exceptions.ConflictException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.repository.ClienteRepository;
import com.interonda.Inventory.repository.VentaRepository;
import com.interonda.Inventory.service.ClienteService;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ClienteServiceImpl.class);

    private final ClienteRepository clienteRepository;
    private final VentaRepository ventaRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository, VentaRepository ventaRepository) {
        this.clienteRepository = clienteRepository;
        this.ventaRepository = ventaRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Cliente findById(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El cliente no fue encontrado!"));
    }

    @Transactional
    @Override
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Cliente crearCliente(Cliente cliente) {
        // Verificar si ya existe un cliente con el mismo contacto para evitar duplicados
        if (clienteRepository.existsByContacto(cliente.getContactoCliente())) {
            throw new ConflictException("Ya existe un cliente con el mismo contacto: " + cliente.getContactoCliente());
        }

        return clienteRepository.save(cliente);
    }

    @Transactional
    @Override
    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        // Verificar si el cliente existe en la base de datos
        Cliente clienteExistente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        // Validación de duplicado para el campo "contactoCliente" si se intenta actualizar
        if (!clienteExistente.getContactoCliente().equals(clienteActualizado.getContactoCliente()) && clienteRepository.existsByContacto(clienteActualizado.getContactoCliente())) {
            throw new ConflictException("Ya existe otro cliente con el contacto: " + clienteActualizado.getContactoCliente());
        }

        // Actualizar los campos necesarios (nombre, país, ciudad, dirección, contacto y email)
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setPais(clienteActualizado.getPais());
        clienteExistente.setCiudad(clienteActualizado.getCiudad());
        clienteExistente.setDireccion(clienteActualizado.getDireccion());
        clienteExistente.setContactoCliente(clienteActualizado.getContactoCliente());
        clienteExistente.seteMailCliente(clienteActualizado.geteMailCliente());

        // Guardar y devolver el cliente actualizado
        return clienteRepository.save(clienteExistente);
    }

    @Transactional
    @Override
    public void eliminarCliente(Long id) {
        // Verificar si el cliente existe en la base de datos
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        // Eliminar el cliente
        clienteRepository.delete(cliente);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Cliente> listarClientes(Pageable pageable) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Cliente> buscarClientesPorNombre(String nombre, Pageable pageable) {
        return clienteRepository.findByNombre(nombre, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Venta> obtenerVentasDeCliente(Long clienteId, Pageable pageable) {
        // Verificamos si el cliente existe
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + clienteId));

        // Recuperamos y retornamos las ventas paginadas
        return ventaRepository.findByClienteId(clienteId, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existeClientePorContacto(String contacto) {
        // Usamos el método del repositorio para verificar la existencia
        return clienteRepository.existsByContacto(contacto);
    }


}


