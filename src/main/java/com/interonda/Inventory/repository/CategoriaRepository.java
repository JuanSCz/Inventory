package com.interonda.Inventory.repository;

import com.interonda.Inventory.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Categoria c WHERE c.nombre = :nombre")
    boolean existsByNombre(String nombre);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Categoria c WHERE c.nombre = :nombre AND c.id <> :id")
    boolean existsByNombreAndIdNot(String nombre, Long id);

}

