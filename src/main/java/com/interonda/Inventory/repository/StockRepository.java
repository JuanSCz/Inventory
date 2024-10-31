package com.interonda.Inventory.repository;

import com.interonda.Inventory.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {

}
