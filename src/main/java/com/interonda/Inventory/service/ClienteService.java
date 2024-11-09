package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Cliente;
import com.interonda.Inventory.entity.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClienteService {
    List<Cliente> findAll();

    Cliente findById(Long id);

    Cliente save(Cliente cliente);

    void deleteById(Long id);

    Cliente crearCliente(Cliente cliente);

    Cliente actualizarCliente(Long id, Cliente clienteActualizado);

    void eliminarCliente(Long id);

    Page<Cliente> listarClientes(Pageable pageable);

    Page<Cliente> buscarClientesPorNombre(String nombre, Pageable pageable);

    Page<Venta> obtenerVentasDeCliente(Long clienteId, Pageable pageable);

    boolean existeClientePorContacto(String contacto);
}
