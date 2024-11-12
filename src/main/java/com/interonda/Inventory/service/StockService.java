package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Stock;

import java.util.List;
import java.util.Optional;

public interface StockService {
    List<Stock> findAll();

    Stock findById(Long id);

    Stock save(Stock stock);

    void deleteById(Long id);

    void actualizarCantidad(Long stockId, Integer cantidadNueva, String operacion, Long usuarioId, String observacion);

    boolean verificarStockSuficiente(Long stockId, int cantidadRequerida);

    void registrarMovimiento(Long stockId, int cantidadMovida, String operacion, Long usuarioId, String tipoOperacion);

    void validarStockAntesDeMovimiento(int cantidadRequerida, Stock stock);

    void actualizarFechaDeActualizacion(Long stockId);

    Stock obtenerStockDeProducto(Long productoId, Long depositoId);

    void ajustarStock(Long productoId, Long depositoId, int cantidadAjustada, Long usuarioId);
}
