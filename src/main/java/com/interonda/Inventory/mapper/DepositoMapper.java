package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Deposito;
import com.interonda.Inventory.entityDTO.DepositoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DepositoMapper {
    DepositoMapper INSTANCE = Mappers.getMapper(DepositoMapper.class);

    DepositoDTO toDto(Deposito deposito);

    Deposito toEntity(DepositoDTO depositoDTO);
}