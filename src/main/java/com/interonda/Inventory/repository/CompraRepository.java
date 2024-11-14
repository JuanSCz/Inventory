package com.interonda.Inventory.repository;

import com.interonda.Inventory.entity.Compra;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

}
