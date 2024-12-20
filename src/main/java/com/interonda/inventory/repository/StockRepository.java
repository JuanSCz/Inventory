package com.interonda.inventory.repository;

import com.interonda.inventory.entity.Stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    void deleteByProductoId(Long productoId);
}
