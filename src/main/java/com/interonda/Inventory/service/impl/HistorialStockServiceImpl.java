package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.HistorialStock;
import com.interonda.Inventory.exceptions.BadRequestException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.repository.HistorialStockRepository;
import com.interonda.Inventory.service.HistorialStockService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public class HistorialStockServiceImpl implements HistorialStockService {

    private static final Logger logger = LoggerFactory.getLogger(HistorialStockServiceImpl.class);
    private final HistorialStockRepository historialStockRepository;

    public HistorialStockServiceImpl(HistorialStockRepository historialStockRepository) {
        this.historialStockRepository = historialStockRepository;
    }

    @Override
    public List<HistorialStock> findAll() {
        return historialStockRepository.findAll();
    }

    @Override
    public HistorialStock findById(Long id) {
        return historialStockRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El historial de stock N " + id + " no fue encontrado!"));
    }

    @Override
    public HistorialStock save(HistorialStock historialStock) {
        return historialStockRepository.save(historialStock);
    }

    @Override
    public void deleteById(Long id) {
        historialStockRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<HistorialStock> listarTodos() {

        List<HistorialStock> historiales = historialStockRepository.findAll();

        return historiales;
    }

    @Transactional
    @Override
    public HistorialStock crearHistorialStock(HistorialStock historialStock) {
        // Validar datos de entrada
        if (historialStock == null) {
            throw new IllegalArgumentException("El historial de stock no puede ser nulo.");
        }

        // Asignar fecha actual si no se proporciona
        if (historialStock.getFecha() == null) {
            historialStock.setFecha(LocalDateTime.now());
        }

        // Guardar el historial de stock
        return historialStockRepository.save(historialStock);
    }

    @Transactional
    @Override
    public HistorialStock actualizarHistorialStock(Long id, HistorialStock historialStockActualizado) {
        // Validar que el ID y el objeto actualizado no sean nulos
        if (historialStockActualizado == null) {
            throw new BadRequestException("El objeto actualizado no puede ser nulos.");
        }

        // Buscar el historial de stock existente
        HistorialStock historialStockExistente = historialStockRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontró el historial de stock con ID: " + id));

        if (historialStockExistente == null) {
            throw new ResourceNotFoundException("No se encontró el historial de stock con ID: " + id);
        }

        // Actualizar los atributos necesarios
        historialStockExistente.setCantidadAnterior(historialStockActualizado.getCantidadAnterior());
        historialStockExistente.setCantidadNueva(historialStockActualizado.getCantidadNueva());
        historialStockExistente.setFecha(historialStockActualizado.getFecha());
        historialStockExistente.setMotivo(historialStockActualizado.getMotivo());
        historialStockExistente.setTipoMovimiento(historialStockActualizado.getTipoMovimiento());
        historialStockExistente.setObservacion(historialStockActualizado.getObservacion());
        historialStockExistente.setProducto(historialStockActualizado.getProducto());
        historialStockExistente.setDeposito(historialStockActualizado.getDeposito());
        historialStockExistente.setUsuario(historialStockActualizado.getUsuario());
        historialStockExistente.setStock(historialStockActualizado.getStock());

        // Guardar los cambios en el historial de stock
        return historialStockRepository.save(historialStockExistente);
    }

    @Transactional
    @Override
    public void eliminarHistorialStock(Long id) {

        HistorialStock historialStock = historialStockRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontró el historial de stock con ID: " + id));

        historialStockRepository.delete(historialStock);
    }

    @Transactional(readOnly = true)
    @Override
    public List<HistorialStock> listarPorProducto(Long productoId) {

        List<HistorialStock> historial = historialStockRepository.findByProductoId(productoId);

        return historial;
    }

    @Transactional(readOnly = true)
    @Override
    public List<HistorialStock> listarPorUsuario(Long usuarioId) {

        List<HistorialStock> historial = historialStockRepository.findByUsuarioId(usuarioId);

        return historial;
    }
}
