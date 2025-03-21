package com.interonda.inventory.repository;

import com.interonda.inventory.entity.DetalleCompra;
import com.interonda.inventory.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    @Query("SELECT d FROM DetalleVenta d WHERE d.venta.id = :ventaId")
    List<DetalleVenta> findByVentaId(@Param("ventaId") Long ventaId);
}
