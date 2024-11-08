package com.interonda.Inventory.repository;

import com.interonda.Inventory.entity.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepositoRepository extends JpaRepository<Deposito, Long> {

    @Query("SELECT COUNT(d) > 0 FROM Deposito d WHERE LOWER(d.nombre) = LOWER(:nombre)")
    boolean existsByNombre(@Param("nombre") String nombre);

    @Query("SELECT COUNT(d) > 0 FROM Deposito d WHERE d.nombre = :nombre AND d.id <> :id")
    boolean existsByNameAndIdNot(@Param("nombre") String nombre, @Param("id") Long id);
}

