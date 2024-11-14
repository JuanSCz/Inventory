package com.interonda.Inventory.repository;

import com.interonda.Inventory.entity.Deposito;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositoRepository extends JpaRepository<Deposito, Long> {


}

