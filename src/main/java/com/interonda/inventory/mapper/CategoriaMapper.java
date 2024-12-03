package com.interonda.inventory.mapper;

import com.interonda.inventory.entity.Categoria;
import com.interonda.inventory.dto.CategoriaDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    CategoriaDTO toDto(Categoria categoria);

    Categoria toEntity(CategoriaDTO categoriaDTO);
}