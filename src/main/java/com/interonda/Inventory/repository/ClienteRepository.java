package com.interonda.Inventory.repository;

import com.interonda.Inventory.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {


    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE c.contacto = :contacto")
    boolean existsByContacto(@Param("contacto") String contacto);


}
