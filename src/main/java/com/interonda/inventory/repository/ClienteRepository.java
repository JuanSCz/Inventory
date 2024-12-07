package com.interonda.inventory.repository;

import com.interonda.inventory.entity.Cliente;

import com.interonda.inventory.entity.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query("SELECT p FROM Cliente p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Cliente> findByNombreContainingIgnoreCase(@Param("nombre") String nombre, Pageable pageable);

}
