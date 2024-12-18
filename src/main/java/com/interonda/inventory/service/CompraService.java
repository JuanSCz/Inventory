package com.interonda.inventory.service;

import com.interonda.inventory.dto.CompraDTO;
import com.interonda.inventory.dto.DetalleCompraDTO;
import com.interonda.inventory.entity.Compra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CompraService {

    CompraDTO convertToDto(Compra compra);

    Compra convertToEntity(CompraDTO compraDTO);

    CompraDTO createCompra(CompraDTO compraDTO);

    CompraDTO updateCompra(CompraDTO compraDTO);

    boolean deleteCompra(Long id);

    CompraDTO getCompra(Long id);

    Page<CompraDTO> getAllCompras(Pageable pageable);

    Page<CompraDTO> searchComprasByFecha(LocalDate fecha, Pageable pageable);

    Page<CompraDTO> searchComprasByProveedorNombre(String nombreProveedor, Pageable pageable);
}
