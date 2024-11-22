package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Categoria;
import com.interonda.Inventory.dto.CategoriaDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    CategoriaDTO toDto(Categoria categoria);

    Categoria toEntity(CategoriaDTO categoriaDTO);
}