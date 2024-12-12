package com.interonda.inventory.repository;

import com.interonda.inventory.entity.Compra;

import com.interonda.inventory.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    Page<Compra> findByFecha(LocalDate fecha, Pageable pageable);

}
