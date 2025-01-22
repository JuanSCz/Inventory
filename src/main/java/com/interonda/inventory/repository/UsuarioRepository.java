package com.interonda.inventory.repository;

import com.interonda.inventory.entity.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Usuario> findByNombreContainingIgnoreCase(@Param("nombre") String nombre, Pageable pageable);

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) = LOWER(:nombre)")
    Optional<Usuario> findByNombre(@Param("nombre") String nombre);
}




