package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Compra;
import com.interonda.Inventory.entity.DetalleCompra;

import java.time.LocalDate;
import java.util.List;;

public interface CompraService {

    List<Compra> findAll();

    Compra findById(Long id);

    Compra save(Compra compra);

    void deleteById(Long id);

    public Compra crearCompra(Compra compra, List<DetalleCompra> detalles);

    public Compra actualizarCompra(Long idCompra, Compra compraActualizada, List<DetalleCompra> nuevosDetalles);

    public void eliminarCompra(Long idCompra);

    public Compra buscarPorId(Long idCompra);

    public List<Compra> listarCompras();

    public List<Compra> buscarPorProveedor(Long idProveedor);

    public List<Compra> buscarPorFecha(LocalDate fecha);
}
