package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Categoria;
import com.interonda.Inventory.entityDTO.CategoriaDTO;
import com.interonda.Inventory.mapper.CategoriaMapper;
import com.interonda.Inventory.repository.CategoriaRepository;
import com.interonda.Inventory.service.CategoriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    private final CategoriaMapper categoriaMapper = CategoriaMapper.INSTANCE;

    @Override
    public CategoriaDTO convertToDto(Categoria categoria) {
        return categoriaMapper.toDto(categoria);
    }

    @Override
    public Categoria convertToEntity(CategoriaDTO categoriaDTO) {
        return categoriaMapper.toEntity(categoriaDTO);
    }
}

