package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Deposito;
import com.interonda.Inventory.entity.Stock;
import com.interonda.Inventory.exceptions.BadRequestException;
import com.interonda.Inventory.exceptions.ConflictException;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.repository.DepositoRepository;
import com.interonda.Inventory.service.DepositoService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class DepositoServiceImpl implements DepositoService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DepositoServiceImpl.class);

    private final DepositoRepository depositoRepository;

    @Autowired
    public DepositoServiceImpl(DepositoRepository depositoRepository) {
        this.depositoRepository = depositoRepository;
    }

    @Override
    public List<Deposito> findAll() {
        return depositoRepository.findAll();
    }

    @Override
    public Deposito findById(Long id) {
        return depositoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El deposito ", id, " no fue encontrado!"));
    }

    @Override
    public Deposito save(Deposito deposito) {
        return depositoRepository.save(deposito);
    }

    @Override
    public void deleteById(Long id) {
        depositoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByNombre(String nombre) {
        return false;
    }

    @Transactional
    @Override
    public Deposito crearDeposito(Deposito deposito) {

        // Validar existencia de depósito con el mismo nombre
        if (depositoRepository.existsByNombre(deposito.getNombre())) {
            throw new ConflictException("Ya existe un depósito con el nombre: " + deposito.getNombre());
        }

        // Registrar la creación del depósito
        logger.info("Creando nuevo depósito: Nombre = {}, Dirección = {}", deposito.getNombre(), deposito.getDireccion());

        // Guardar el depósito en el repositorio
        return depositoRepository.save(deposito);
    }

    @Transactional(readOnly = true)
    public List<Deposito> listarDepositos() {
        List<Deposito> depositos = depositoRepository.findAll();
        return depositos;
    }


    @Transactional
    public Deposito actualizarDeposito(Long id, Deposito depositoActualizado) {
        Deposito depositoExistente = depositoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ya existe un depósito con el nombre: " + depositoActualizado));

        if (depositoRepository.existsByNameAndIdNot(depositoActualizado.getNombre(), id)) {
            throw new ResourceNotFoundException("Ya existe un depósito con el nombre " + depositoActualizado);
        }

        logger.info("Actualizando depósito con ID: {}", id);

        depositoExistente.setNombre(depositoActualizado.getNombre());
        depositoExistente.setDireccion(depositoActualizado.getDireccion());

        Deposito depositoActualizadoFinal = depositoRepository.save(depositoExistente);

        logger.info("Depósito actualizado exitosamente: {}", depositoActualizadoFinal);

        return depositoActualizadoFinal;
    }

    @Transactional
    public void eliminarDeposito(Long id) {
        // Buscar el depósito existente o lanzar excepción
        Deposito deposito = depositoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El deposito no existe!"));

        depositoRepository.delete(deposito);
        logger.info("Eliminando depósito con ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<Stock> listarStocksPorDeposito(Long depositoId) {
        logger.info("Listando stocks para el depósito con ID: {}", depositoId);

        Deposito deposito;
        deposito = depositoRepository.findById(depositoId).orElseThrow(() -> new ResourceNotFoundException("No se pudo encontrar el depósito con ID " + depositoId));

        // Obtener la lista de stocks asociados al depósito
        List<Stock> stocks = deposito.getStocks();

        if (stocks.isEmpty()) {
            logger.warn("El depósito con ID {} no tiene stocks asociados.", depositoId);
        }

        return stocks;
    }
}
