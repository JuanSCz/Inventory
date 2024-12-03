package com.interonda.inventory.repository;

import com.interonda.inventory.entity.Deposito;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositoRepository extends JpaRepository<Deposito, Long> {


}

