package com.interonda.Inventory.repository;

import com.interonda.Inventory.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Buscar categorías por nombre (coincidencia parcial e insensible a mayúsculas)
    @Query("SELECT c FROM Categoria c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Categoria> buscarPorNombre(@Param("nombre") String nombre);

}
