package com.interonda.Inventory.serviceImplTest;

import com.interonda.Inventory.entity.Compra;
import com.interonda.Inventory.dto.CompraDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompraService {

    CompraDTO convertToDto(Compra compra);

    Compra convertToEntity(CompraDTO compraDTO);

    CompraDTO createCompra(CompraDTO compraDTO);

    CompraDTO updateCompra(Long id, CompraDTO compraDTO);

    void deleteCompra(Long id);

    Page<CompraDTO> getAllCompras(Pageable pageable);

    CompraDTO getCompraById(Long id);
}
