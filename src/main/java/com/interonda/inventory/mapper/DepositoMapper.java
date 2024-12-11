package com.interonda.inventory.mapper;

import com.interonda.inventory.entity.Deposito;
import com.interonda.inventory.dto.DepositoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepositoMapper {

    DepositoDTO toDto(Deposito deposito);

    Deposito toEntity(DepositoDTO depositoDTO);

    void updateEntityFromDto(DepositoDTO depositoDTO, @MappingTarget Deposito deposito);
}