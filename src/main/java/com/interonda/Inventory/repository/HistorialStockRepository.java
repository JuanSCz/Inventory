package com.interonda.Inventory.repository;

import com.interonda.Inventory.entity.HistorialStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistorialStockRepository extends JpaRepository<HistorialStock, Long> {

    @Query("SELECT h FROM HistorialStock h WHERE h.producto.id = :productoId")
    List<HistorialStock> findByProductoId(@Param("productoId") Long productoId);

    @Query("SELECT h FROM HistorialStock h WHERE h.usuario.id = :usuarioId")
    List<HistorialStock> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT h FROM HistorialStock h WHERE h.stock.id = :stockId")
    Page<HistorialStock> findByStockId(@Param("stockId") Long stockId, Pageable pageable);

}

