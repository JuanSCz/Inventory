package com.interonda.inventory.repository;

import com.interonda.inventory.entity.HistorialStock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialStockRepository extends JpaRepository<HistorialStock, Long> {
    
}

