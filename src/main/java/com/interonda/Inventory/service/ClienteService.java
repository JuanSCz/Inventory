package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Cliente;
import com.interonda.Inventory.entityDTO.ClienteDTO;

import java.util.List;

public interface ClienteService {

    ClienteDTO convertToDto(Cliente cliente);

    Cliente convertToEntity(ClienteDTO clienteDTO);

    ClienteDTO createCliente(ClienteDTO clienteDTO);

    ClienteDTO updateCliente(Long id, ClienteDTO clienteDTO);

    void deleteCliente(Long id);

    List<ClienteDTO> getAllClientes();

    ClienteDTO getClienteById(Long id);


}
