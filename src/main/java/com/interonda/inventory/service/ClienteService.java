package com.interonda.inventory.service;

import com.interonda.inventory.entity.Cliente;
import com.interonda.inventory.dto.ClienteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {

    ClienteDTO convertToDto(Cliente cliente);

    Cliente convertToEntity(ClienteDTO clienteDTO);

    ClienteDTO createCliente(ClienteDTO clienteDTO);

    ClienteDTO updateCliente(Long id, ClienteDTO clienteDTO);

    void deleteCliente(Long id);

    Page<ClienteDTO> getAllClientes(Pageable pageable);

    ClienteDTO getClienteById(Long id);


}
