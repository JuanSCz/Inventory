package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Categoria;
import com.interonda.Inventory.entityDTO.CategoriaDTO;

public interface CategoriaService {


    Categoria convertToEntity(CategoriaDTO categoriaDTO);
}
