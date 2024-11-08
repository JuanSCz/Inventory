package com.interonda.Inventory.repository;

import com.interonda.Inventory.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {


    @Query("SELECT COUNT(p) > 0 FROM Proveedor p WHERE LOWER(p.nombre) = LOWER(:nombre)")
    boolean existsByNombre(@Param("nombre") String nombre);

    // JPQL con la consulta personalizada para buscar por nombre sin importar mayúsculas o minúsculas
    @Query("SELECT p FROM Proveedor p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Proveedor> buscarProveedoresPorNombre(@Param("nombre") String nombre);
}
