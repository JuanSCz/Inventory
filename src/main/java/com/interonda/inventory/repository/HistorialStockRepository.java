package com.interonda.inventory.repository;

import com.interonda.inventory.entity.HistorialStock;

import com.interonda.inventory.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialStockRepository extends JpaRepository<HistorialStock, Long> {

}
