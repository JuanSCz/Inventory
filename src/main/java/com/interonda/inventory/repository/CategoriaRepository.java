package com.interonda.inventory.repository;

import com.interonda.inventory.entity.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Categoria c WHERE c.nombre = :nombre")
    boolean existsByNombre(String nombre);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Categoria c WHERE c.nombre = :nombre AND c.id <> :id")
    boolean existsByNombreAndIdNot(String nombre, Long id);

    @Query("SELECT c FROM Categoria c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Categoria> findByNombreContainingIgnoreCase(@Param("nombre") String nombre, Pageable pageable);
}

