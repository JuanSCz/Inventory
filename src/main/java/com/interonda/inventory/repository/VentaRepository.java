package com.interonda.inventory.repository;

import com.interonda.inventory.entity.Compra;
import com.interonda.inventory.entity.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    Page<Venta> findByFecha(LocalDate fecha, Pageable pageable);

    @Query("SELECT v FROM Venta v WHERE v.cliente.nombre LIKE %:nombreCliente%")
    Page<Venta> findByClienteNombre(@Param("nombreCliente") String nombreCliente, Pageable pageable);

    }
