package com.interonda.Inventory.repository;

import com.interonda.Inventory.entity.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT v FROM Venta v WHERE v.cliente.id = :clienteId")
    Page<Venta> obtenerVentasDeCliente(@Param("clienteId") Long clienteId, Pageable pageable);

}
