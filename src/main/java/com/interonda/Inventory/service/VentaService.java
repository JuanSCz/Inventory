package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VentaService {
    List<Venta> findAll();

    Venta findById(Long id);

    Venta save(Venta venta);

    void deleteById(Long id);


}
