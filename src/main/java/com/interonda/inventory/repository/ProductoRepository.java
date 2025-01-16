package com.interonda.inventory.repository;

import com.interonda.inventory.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND p.activo = true")
    Page<Producto> findByNombreContainingIgnoreCase(@Param("nombre") String nombre, Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.deposito.id = :depositoId AND p.activo = true")
    Page<Producto> findByDepositoId(@Param("depositoId") Long depositoId, Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.activo = true")
    Page<Producto> findByActivoTrue(Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND p.activo = true")
    Page<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(@Param("nombre") String nombre, Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.activo = false AND p.fechaBaja < :fecha")
    Page<Producto> findByActivoFalseAndFechaBajaBefore(@Param("fecha") LocalDateTime fecha, Pageable pageable);
}