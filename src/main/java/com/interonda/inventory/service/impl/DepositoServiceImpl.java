package com.interonda.inventory.service.impl;

import com.interonda.inventory.entity.Deposito;
import com.interonda.inventory.dto.DepositoDTO;
import com.interonda.inventory.entity.Stock;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.DepositoMapper;
import com.interonda.inventory.repository.DepositoRepository;
import com.interonda.inventory.service.DepositoService;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.utils.ValidatorUtils;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepositoServiceImpl implements DepositoService {

    private static final Logger logger = LoggerFactory.getLogger(DepositoServiceImpl.class);

    private final DepositoRepository depositoRepository;
    private final DepositoMapper depositoMapper;
    private final Validator validator;

    @Autowired
    public DepositoServiceImpl(DepositoRepository depositoRepository, DepositoMapper depositoMapper, Validator validator) {
        this.depositoRepository = depositoRepository;
        this.depositoMapper = depositoMapper;
        this.validator = validator;
    }

    @Override
    public DepositoDTO convertToDto(Deposito deposito) {
        return depositoMapper.toDto(deposito);
    }

    @Override
    public Deposito convertToEntity(DepositoDTO depositoDTO) {
        return depositoMapper.toEntity(depositoDTO);
    }

    @Override
    @Transactional
    public DepositoDTO createDeposito(DepositoDTO depositoDTO) {
        ValidatorUtils.validateEntity(depositoDTO, validator);
        try {
            logger.info("Creando nuevo Deposito");
            Deposito deposito = depositoMapper.toEntity(depositoDTO);

            // Initialize the stocks collection if it is null
            if (deposito.getStocks() == null) {
                deposito.setStocks(new ArrayList<>());
            }

            deposito.getStocks().forEach(stock -> stock.setDeposito(deposito)); // Set the reference of the deposito in stocks
            Deposito savedDeposito = depositoRepository.save(deposito);
            logger.info("Deposito creado exitosamente con id: {}", savedDeposito.getId());
            return depositoMapper.toDto(savedDeposito);
        } catch (Exception e) {
            logger.error("Error guardando Deposito", e);
            throw new DataAccessException("Error guardando Deposito", e);
        }
    }

    @Override
    @Transactional
    public DepositoDTO updateDeposito(DepositoDTO depositoDTO) {
        ValidatorUtils.validateEntity(depositoDTO, validator);
        try {
            logger.info("Actualizando Deposito con id: {}", depositoDTO.getId());
            Deposito deposito = depositoRepository.findById(depositoDTO.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Deposito no encontrado con el id: " + depositoDTO.getId()));

            // Preserve the existing stocks collection
            List<Stock> existingStocks = deposito.getStocks();

            depositoMapper.updateEntityFromDto(depositoDTO, deposito);

            // Restore the existing stocks collection
            deposito.setStocks(existingStocks);

            Deposito updatedDeposito = depositoRepository.save(deposito);
            logger.info("Deposito actualizado exitosamente con id: {}", updatedDeposito.getId());
            return depositoMapper.toDto(updatedDeposito);
        } catch (ResourceNotFoundException e) {
            logger.warn("Deposito no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error actualizando Deposito", e);
            throw new DataAccessException("Error actualizando Deposito", e);
        }
    }

    @Override
    @Transactional
    public boolean deleteDeposito(Long id) {
        try {
            logger.info("Eliminando Deposito con id: {}", id);
            if (!depositoRepository.existsById(id)) {
                throw new ResourceNotFoundException("Deposito no encontrado con el id: " + id);
            }
            depositoRepository.deleteById(id);
            logger.info("Deposito eliminado exitosamente con id: {}", id);
            return true;
        } catch (ResourceNotFoundException e) {
            logger.warn("Deposito no encontrado: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error eliminando Deposito", e);
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DepositoDTO getDeposito(Long id) {
        try {
            logger.info("Obteniendo Deposito con id: {}", id);
            Deposito deposito = depositoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Deposito no encontrado con el id: " + id));
            return depositoMapper.toDto(deposito);
        } catch (ResourceNotFoundException e) {
            logger.warn("Deposito no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo Deposito", e);
            throw new DataAccessException("Error obteniendo Deposito", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepositoDTO> getAllDepositos(Pageable pageable) {
        try {
            logger.info("Obteniendo todos los Depositos con paginación");
            Pageable sortedByIdDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());
            Page<Deposito> depositos = depositoRepository.findAll(sortedByIdDesc);
            return depositos.map(depositoMapper::toDto);
        } catch (Exception e) {
            logger.error("Error obteniendo todos los Depositos con paginación", e);
            throw new DataAccessException("Error obteniendo todos los Depositos con paginación", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countDepositos() {
        try {
            long total = depositoRepository.count();
            logger.info("Total de depositos: {}", total);
            return total;
        } catch (Exception e) {
            logger.error("Error contando todas los Depositos", e);
            throw new DataAccessException("Error contando todos los Depositos", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepositoDTO> searchDepositosByName(String nombre, Pageable pageable) {
        try {
            logger.info("Buscando Depositos por nombre: {}", nombre);
            Page<Deposito> depositos = depositoRepository.findByNombreContainingIgnoreCase(nombre, pageable);
            return depositos.map(depositoMapper::toDto);
        } catch (Exception e) {
            logger.error("Error buscando Depositos por nombre", e);
            throw new DataAccessException("Error buscando Depositos por nombre", e);
        }
    }
}