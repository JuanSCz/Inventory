package com.interonda.inventory.repository;

import com.interonda.inventory.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Producto> findByNombreContainingIgnoreCase(@Param("nombre") String nombre, Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.deposito.id = :depositoId")
    Page<Producto> findByDepositoId(@Param("depositoId") Long depositoId, Pageable pageable);
}