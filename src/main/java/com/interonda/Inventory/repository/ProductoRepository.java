package com.interonda.Inventory.repository;

import com.interonda.Inventory.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
