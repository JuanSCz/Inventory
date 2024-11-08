package com.interonda.Inventory.repository;

import com.interonda.Inventory.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Buscar categorías por nombre (coincidencia parcial e insensible a mayúsculas)
    @Query("SELECT c FROM Categoria c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Categoria> buscarPorNombre(@Param("nombre") String nombre);

    // Verificar si existe una categoría con el mismo nombre
    @Query("SELECT COUNT(c) > 0 FROM Categoria c WHERE LOWER(c.nombre) = LOWER(:nombre)")
    boolean existsByNombre(@Param("nombre") String nombre);

    // Actualizar el nombre de una categoría
    @Modifying
    @Query("UPDATE Categoria c SET c.nombre = :nuevoNombre WHERE c.id = :idCategoria")
    void updateNombre(@Param("idCategoria") Long idCategoria, @Param("nuevoNombre") String nuevoNombre);
}
