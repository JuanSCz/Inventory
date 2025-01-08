package com.interonda.inventory.repository;

import com.interonda.inventory.entity.Stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    void deleteByProductoId(Long productoId);

    Optional<Stock> findByProductoIdAndDepositoId(Long productoId, Long depositoId);

    @Query("SELECT s FROM Stock s WHERE s.producto.id = :productoId AND s.deposito.id = :depositoId")
    List<Stock> findByProductoIdAndDepositoIdList(@Param("productoId") Long productoId, @Param("depositoId") Long depositoId);
}
