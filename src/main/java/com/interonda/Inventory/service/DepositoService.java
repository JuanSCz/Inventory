package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Deposito;
import com.interonda.Inventory.entityDTO.DepositoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepositoService {

    DepositoDTO convertToDto(Deposito deposito);

    Deposito convertToEntity(DepositoDTO depositoDTO);

    DepositoDTO createDeposito(DepositoDTO depositoDTO);

    DepositoDTO updateDeposito(Long id, DepositoDTO depositoDTO);

    void deleteDeposito(Long id);

    Page<DepositoDTO> getAllDepositos(Pageable pageable);

    DepositoDTO getDepositoById(Long id);
}

