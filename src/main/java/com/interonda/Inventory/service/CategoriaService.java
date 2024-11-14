package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Categoria;
import com.interonda.Inventory.entityDTO.CategoriaDTO;

import java.util.List;

public interface CategoriaService {

    CategoriaDTO convertToDto(Categoria categoria);

    Categoria convertToEntity(CategoriaDTO categoriaDTO);

    CategoriaDTO createCategoria(CategoriaDTO categoriaDTO);

    CategoriaDTO updateCategoria(Long id, CategoriaDTO categoriaDTO);

    void deleteCategoria(Long id);

    List<CategoriaDTO> getAllCategorias();

    CategoriaDTO getCategoriaById(Long id);


}
