package com.interonda.Inventory.repository;

import com.interonda.Inventory.entity.HistorialStock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialStockRepository extends JpaRepository<HistorialStock, Long> {

}

