package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Deposito;
import com.interonda.Inventory.entityDTO.DepositoDTO;

import java.util.List;

public interface DepositoService {

    DepositoDTO convertToDto(Deposito deposito);

    Deposito convertToEntity(DepositoDTO depositoDTO);

    DepositoDTO createDeposito(DepositoDTO depositoDTO);

    DepositoDTO updateDeposito(Long id, DepositoDTO depositoDTO);

    void deleteDeposito(Long id);

    List<DepositoDTO> getAllDepositos();

    DepositoDTO getDepositoById(Long id);
}

