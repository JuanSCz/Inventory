package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Categoria;
import com.interonda.Inventory.entityDTO.CategoriaDTO;
import org.mapstruct.factory.Mappers;

public interface CategoriaMapper {
    CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);

    CategoriaDTO toDto(Categoria categoria);

    Categoria toEntity(CategoriaDTO categoriaDTO);
}
