package com.interonda.Inventory.repository;

import com.interonda.Inventory.entity.Stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("SELECT s FROM Stock s WHERE s.producto.id = :productoId AND s.deposito.id = :depositoId")
    Optional<Stock> obtenerStockPorProductoYDeposito(@Param("productoId") Long productoId, @Param("depositoId") Long depositoId);


}
