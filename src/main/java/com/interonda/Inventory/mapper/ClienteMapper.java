package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Cliente;
import com.interonda.Inventory.dto.ClienteDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteDTO toDto(Cliente cliente);

    Cliente toEntity(ClienteDTO clienteDTO);
}
