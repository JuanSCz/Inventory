package com.interonda.inventory.service;

import com.interonda.inventory.dto.VentaDTO;
import com.interonda.inventory.entity.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface VentaService {

    VentaDTO convertToDto(Venta venta);

    Venta convertToEntity(VentaDTO ventaDTO);

    VentaDTO createVenta(VentaDTO ventaDTO);

    VentaDTO updateVenta(VentaDTO ventaDTO);

    boolean deleteVenta(Long id);

    VentaDTO getVenta(Long id);

    Page<VentaDTO> getAllVentas(Pageable pageable);

    Page<VentaDTO> searchVentasByFecha(LocalDate fecha, Pageable pageable);

    Page<VentaDTO> searchVentasByClienteNombre(String nombreCliente, Pageable pageable);

    long countVentas();

    String formatTotal(BigDecimal total);
}
