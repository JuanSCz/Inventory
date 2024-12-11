package com.interonda.inventory.repository;

import com.interonda.inventory.entity.Deposito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositoRepository extends JpaRepository<Deposito, Long> {

    @Query("SELECT d FROM Deposito d WHERE LOWER(d.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Deposito> findByNombreContainingIgnoreCase(@Param("nombre") String nombre, Pageable pageable);

}