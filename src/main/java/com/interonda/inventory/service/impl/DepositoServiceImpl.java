package com.interonda.inventory.service.impl;

import com.interonda.inventory.entity.Deposito;
import com.interonda.inventory.dto.DepositoDTO;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.DepositoMapper;
import com.interonda.inventory.repository.DepositoRepository;
import com.interonda.inventory.service.DepositoService;

import com.interonda.inventory.utils.ValidatorUtils;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            Deposito deposito = convertToEntity(depositoDTO);
            Deposito savedDeposito = depositoRepository.save(deposito);
            logger.info("Deposito creado exitosamente con id: {}", savedDeposito.getId());
            return convertToDto(savedDeposito);
        } catch (Exception e) {
            logger.error("Error creando Deposito", e);
            throw new DataAccessException("Error creando Deposito", e);
        }
    }

    @Override
    @Transactional
    public DepositoDTO updateDeposito(Long id, DepositoDTO depositoDTO) {
        ValidatorUtils.validateEntity(depositoDTO, validator);
        try {
            logger.info("Actualizando Deposito con id: {}", id);
            Deposito deposito = depositoRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Deposito no encontrado con el id: " + id));
            deposito.setNombre(depositoDTO.getNombre());
            deposito.setDireccion(depositoDTO.getDireccion());
            deposito.setProvincia(depositoDTO.getProvincia());
            deposito.setContactoDeposito(depositoDTO.getContactoDeposito());
            Deposito updatedDeposito = depositoRepository.save(deposito);
            logger.info("Deposito actualizado exitosamente con id: {}", updatedDeposito.getId());
            return convertToDto(updatedDeposito);
        } catch (ResourceNotFoundException e) {
            logger.warn("Deposito no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error actualizando Deposito por id: " + id, e);
            throw new DataAccessException("Error actualizando Deposito por id: " + id, e);
        }
    }

    @Override
    @Transactional
    public void deleteDeposito(Long id) {
        try {
            logger.info("Eliminando Deposito con id: {}", id);
            if (!depositoRepository.existsById(id)) {
                throw new ResourceNotFoundException("Deposito no encontrado con el id: " + id);
            }
            depositoRepository.deleteById(id);
            logger.info("Deposito eliminado exitosamente con id: {}", id);
        } catch (ResourceNotFoundException e) {
            logger.warn("Deposito no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error eliminando Deposito por id: " + id, e);
            throw new DataAccessException("Error eliminando Deposito por id: " + id, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepositoDTO> getAllDepositos(Pageable pageable) {
        try {
            logger.info("Obteniendo todos los Depositos con paginación");
            Page<Deposito> depositos = depositoRepository.findAll(pageable);
            return depositos.map(this::convertToDto);
        } catch (Exception e) {
            logger.error("Error obteniendo todos los Depositos con paginación", e);
            throw new DataAccessException("Error obteniendo todos los Depositos con paginación", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DepositoDTO getDepositoById(Long id) {
        try {
            logger.info("Obteniendo Deposito con id: {}", id);
            Deposito deposito = depositoRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Deposito no encontrado con el id: " + id));
            return convertToDto(deposito);
        } catch (ResourceNotFoundException e) {
            logger.warn("Deposito no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo Deposito por id: " + id, e);
            throw new DataAccessException("Error obteniendo Deposito por id: " + id, e);
        }
    }
}
