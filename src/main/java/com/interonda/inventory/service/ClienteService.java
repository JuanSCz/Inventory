package com.interonda.inventory.service;

import com.interonda.inventory.dto.ProveedorDTO;
import com.interonda.inventory.entity.Cliente;
import com.interonda.inventory.dto.ClienteDTO;
import com.interonda.inventory.entity.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {

    ClienteDTO convertToDto(Cliente cliente);

    Cliente convertToEntity(ClienteDTO clienteDTO);

    ClienteDTO createCliente(ClienteDTO clienteDTO);

    ClienteDTO updateCliente(ClienteDTO clienteDTO);

    boolean deleteCliente(Long id);

    ClienteDTO getCliente(Long id);

    Page<ClienteDTO> getAllClientes(Pageable pageable);

    Page<ClienteDTO> searchClientesByName(String nombre, Pageable pageable);


}
