package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.HistorialStock;

import java.util.List;

public interface HistorialStockService {
    List<HistorialStock> findAll();

    HistorialStock findById(Long id);

    HistorialStock save(HistorialStock historialStock);

    void deleteById(Long id);

    List<HistorialStock> listarTodos();

    HistorialStock crearHistorialStock(HistorialStock historialStock);

    HistorialStock actualizarHistorialStock(Long id, HistorialStock historialStockActualizado);

    void eliminarHistorialStock(Long id);

    List<HistorialStock> listarPorProducto(Long productoId);

    List<HistorialStock> listarPorUsuario(Long usuarioId);
}
