package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Deposito;
import com.interonda.Inventory.dto.DepositoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepositoMapper {

    DepositoDTO toDto(Deposito deposito);

    Deposito toEntity(DepositoDTO depositoDTO);
}