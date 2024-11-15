package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Cliente;
import com.interonda.Inventory.entityDTO.ClienteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClienteMapper {
    // Singleton instance of the ClienteMapper
    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

    // Method to convert Cliente entity to ClienteDTO
    ClienteDTO toDto(Cliente cliente);

    // Method to convert ClienteDTO to Cliente entity
    Cliente toEntity(ClienteDTO clienteDTO);
}
