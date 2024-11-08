package com.interonda.Inventory.repository;

import com.interonda.Inventory.entity.Compra;
import com.interonda.Inventory.entity.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    @Query("SELECT c FROM Compra c WHERE c.proveedor.id = :idProveedor")
    List<Compra> findByProveedorId(@Param("idProveedor") Long idProveedor);

    @Query("SELECT c FROM Compra c WHERE c.fecha = :fecha")
    List<Compra> findByFecha(@Param("fecha") LocalDate fecha);

}
