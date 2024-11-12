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

    public Cliente crearCliente(Cliente cliente);

    public Cliente actualizarCliente(Long id, Cliente clienteActualizado);

    public void eliminarCliente(Long id);

    public Page<Cliente> listarClientes(Pageable pageable);

    public Page<Cliente> buscarClientesPorNombre(String nombre, Pageable pageable);

    public Page<Venta> obtenerVentasDeCliente(Long clienteId, Pageable pageable);

    public boolean existeClientePorContacto(String contacto);


}
