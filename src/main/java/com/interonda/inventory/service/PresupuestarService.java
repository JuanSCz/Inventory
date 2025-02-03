package com.interonda.inventory.service;

import com.interonda.inventory.dto.VentaDTO;
import com.interonda.inventory.entity.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Map;

public interface PresupuestarService {
    VentaDTO convertToDto(Venta venta);

    Venta convertToEntity(VentaDTO ventaDTO);

    VentaDTO getVenta(Long id);

    Page<VentaDTO> getAllVentas(Pageable pageable);

    Page<VentaDTO> searchVentasByFecha(LocalDate fecha, Pageable pageable);

    Page<VentaDTO> searchVentasByClienteNombre(String nombreCliente, Pageable pageable);

    long countVentas();

    byte[] generatePdf(Long id);

    Page<Map<String, Object>> getAllPresupuestarAsMap(Pageable pageable);

}