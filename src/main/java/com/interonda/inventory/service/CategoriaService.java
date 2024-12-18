// CategoriaService.java
package com.interonda.inventory.service;

import com.interonda.inventory.dto.CategoriaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoriaService {
    
    CategoriaDTO getCategoria(Long id);

    CategoriaDTO createCategoria(CategoriaDTO categoriaDTO);

    CategoriaDTO updateCategoria(CategoriaDTO categoriaDTO);

    boolean deleteCategoria(Long id);

    Page<CategoriaDTO> getAllCategorias(Pageable pageable);

    Page<CategoriaDTO> searchCategoriasByName(String nombre, Pageable pageable);

    long countCategorias();
}
