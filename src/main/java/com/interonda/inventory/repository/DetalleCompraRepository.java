package com.interonda.inventory.repository;

import com.interonda.inventory.entity.DetalleCompra;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {

    @Query("SELECT d FROM DetalleCompra d WHERE d.compra.id = :compraId")
    List<DetalleCompra> findByCompraId(@Param("compraId") Long compraId);
}
