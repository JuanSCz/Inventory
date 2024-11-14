package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Compra;
import com.interonda.Inventory.entityDTO.CompraDTO;

import java.util.List;

public interface CompraService {

    CompraDTO convertToDto(Compra compra);

    Compra convertToEntity(CompraDTO compraDTO);

    CompraDTO createCompra(CompraDTO compraDTO);

    CompraDTO updateCompra(Long id, CompraDTO compraDTO);

    void deleteCompra(Long id);

    List<CompraDTO> getAllCompras();

    CompraDTO getCompraById(Long id);
}
