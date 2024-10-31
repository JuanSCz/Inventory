package com.interonda.Inventory.repository;

import com.interonda.Inventory.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
