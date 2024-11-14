package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Categoria;
import com.interonda.Inventory.entityDTO.CategoriaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoriaMapper {
    // Singleton instance of the CategoriaMapper
    CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);

    // Method to convert Categoria entity to CategoriaDTO
    CategoriaDTO toDto(Categoria categoria);

    // Method to convert CategoriaDTO to Categoria entity
    Categoria toEntity(CategoriaDTO categoriaDTO);
}
